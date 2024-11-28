package com.skku.skkuduler.infrastructure.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.skkuduler.domain.calender.QCalendar;
import com.skku.skkuduler.domain.calender.QCalendarEvent;
import com.skku.skkuduler.domain.calender.QEvent;
import com.skku.skkuduler.domain.calender.QImage;
import com.skku.skkuduler.domain.comment.QComment;
import com.skku.skkuduler.domain.department.QDepartment;
import com.skku.skkuduler.domain.like.QLikes;
import com.skku.skkuduler.domain.user.QPermission;
import com.skku.skkuduler.domain.user.QUser;
import com.skku.skkuduler.dto.request.SortType;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.dto.response.DepartmentEventSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class EventCustomRepositoryImpl implements EventCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CalendarEventDetailDto getEventDetail(Long eventId, Long userId) {
        QEvent event = QEvent.event;
        QUser user = QUser.user;
        QDepartment department = QDepartment.department;
        QImage image = QImage.image;
        QLikes like = QLikes.likes;
        QCalendar calendar = QCalendar.calendar;
        QCalendarEvent calendarEvent = QCalendarEvent.calendarEvent;
        QPermission permission = QPermission.permission;
        BooleanExpression hasPermission =
                event.isUserEvent.isTrue().and(event.userId.eq(userId))
                        .or(JPAExpressions.selectOne()
                                .from(permission)
                                .where(permission.user.userId.eq(userId)
                                        .and(permission.departmentId.eq(event.departmentId)))
                                .exists());
        BooleanExpression isLiked =
                JPAExpressions.selectOne()
                        .from(like)
                        .where(like.userId.eq(userId))
                        .exists();
        List<CalendarEventDetailDto> response = jpaQueryFactory
                .selectFrom(event)
                .distinct()
                .leftJoin(user).on(event.userId.eq(user.userId))
                .leftJoin(department).on(event.departmentId.eq(department.departmentId))
                .leftJoin(event.image, image)
                .where(event.eventId.eq(eventId))
                .transform(groupBy(event.eventId).list(Projections.constructor(CalendarEventDetailDto.class,
                        event.eventId,
                        event.createdAt,
                        event.isUserEvent.when(true).then(false).otherwise(true),
                        event.departmentId.isNull().and(event.userId.isNull()),
                        hasPermission,
                        JPAExpressions.selectOne()
                                .from(calendarEvent)
                                .join(calendarEvent.calendar, calendar)
                                .where(calendar.user.userId.eq(userId)
                                        .and(calendarEvent.event.eventId.eq(eventId)))
                                .exists(),
                        isLiked,
                        user.userId,
                        user.name,
                        department.departmentId,
                        department.name,
                        JPAExpressions.select(like.count().coalesce(0L))
                                .from(like)
                                .where(like.eventId.eq(eventId)),
                        Projections.constructor(CalendarEventDetailDto.EventInfo.class,
                                event.eventId,
                                event.title,
                                event.content,
                                event.colorCode,
                                event.startDateTime,
                                event.endDateTime
                        ),
                        image.src
                )));


        if (response.isEmpty()) return null;
        return response.get(0);
    }

    @Override
    public Page<DepartmentEventSummaryDto> getDepartmentEventSummaryDtos(List<Long> departmentIds, Long userId, SortType sortType, String query, Pageable pageable) {

        QEvent event = QEvent.event;
        QLikes like = QLikes.likes;
        QComment comment = QComment.comment;
        QDepartment department = QDepartment.department;

        BooleanExpression whereCondition = event.departmentId.in(departmentIds)
                .or(event.userId.eq(userId));
        System.out.println("departmentIds = " + departmentIds);
        if (!departmentIds.isEmpty()) {
            whereCondition = whereCondition.or(event.departmentId.isNull().and(event.userId.isNull()));
        }
        if (query != null && !query.isEmpty()) {
            whereCondition = whereCondition.and(
                    event.title.containsIgnoreCase(query)
                            .or(event.content.containsIgnoreCase(query))
            );
        }

        List<DepartmentEventSummaryDto> content = jpaQueryFactory
                .select(Projections.constructor(
                        DepartmentEventSummaryDto.class,
                        Projections.constructor(
                                DepartmentEventSummaryDto.DepartmentInfo.class,
                                department.departmentId.as("departmentId"),
                                department.name.as("departmentName")
                        ),
                        Projections.constructor(
                                DepartmentEventSummaryDto.EventInfo.class,
                                event.eventId.as("eventId"),
                                event.title,
                                event.startDateTime,
                                event.endDateTime,
                                like.countDistinct().as("likeCount"),
                                comment.countDistinct().as("commentCount"),
                                event.createdAt
                        )

                ))
                .from(event)
                .leftJoin(department).on(department.departmentId.eq(event.departmentId))
                .leftJoin(like).on(like.eventId.eq(event.eventId))
                .leftJoin(comment).on(comment.eventId.eq(event.eventId))
                .where(whereCondition)
                .groupBy(event.eventId)
                .orderBy(getSortOrder(sortType, event, like, comment))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory.select(event.countDistinct())
                .from(event)
                .where(whereCondition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }


    private OrderSpecifier<?>[] getSortOrder(SortType sortType, QEvent event, QLikes like, QComment comment) {
        LocalDateTime now = LocalDateTime.now();

        BooleanExpression isActive = event.startDateTime.loe(now)
                .and(event.endDateTime.goe(now));
        BooleanExpression isFuture = event.startDateTime.goe(now);

        NumberExpression<Integer> priority = new CaseBuilder()
                .when(isActive).then(1)
                .when(isFuture).then(2)
                .otherwise(3);

        OrderSpecifier<?>[] baseOrder = new OrderSpecifier<?>[] {
                priority.asc(),
                event.startDateTime.asc()
        };

        if (sortType == SortType.LIKES) {
            return new OrderSpecifier<?>[] {
                    like.countDistinct().desc(),
                    priority.asc(),
                    event.startDateTime.asc()
            };
        } else if (sortType == SortType.COMMENTS) {
            return new OrderSpecifier<?>[] {
                    comment.countDistinct().desc(),
                    priority.asc(),
                    event.startDateTime.asc()
            };
        } else {
            return baseOrder;
        }
    }



}
