package com.skku.skkuduler.infrastructure.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.skkuduler.domain.calender.QCalendar;
import com.skku.skkuduler.domain.calender.QCalendarEvent;
import com.skku.skkuduler.domain.calender.QEvent;
import com.skku.skkuduler.domain.calender.QImage;
import com.skku.skkuduler.domain.department.QDepartment;
import com.skku.skkuduler.domain.like.QLikes;
import com.skku.skkuduler.domain.user.QUser;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

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

        List<CalendarEventDetailDto> response = jpaQueryFactory
                .selectFrom(event)
                .distinct()
                .leftJoin(user).on(event.userId.eq(user.userId))
                .leftJoin(department).on(event.departmentId.eq(department.departmentId))
                .leftJoin(event.images, image)
                .where(event.eventId.eq(eventId))
                .orderBy(image.order.asc())
                .transform(groupBy(event.eventId).list(Projections.constructor(CalendarEventDetailDto.class,
                        event.eventId,
                        event.createdAt,
                        event.isUserEvent.when(true).then(false).otherwise(true),
                        event.userId.eq(userId),
                        JPAExpressions.selectOne()
                                .from(calendarEvent)
                                .join(calendarEvent.calendar, calendar)
                                .where(calendar.user.userId.eq(userId)
                                        .and(calendarEvent.event.eventId.eq(eventId)))
                                .exists(),
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
                        list(Projections.constructor(CalendarEventDetailDto.ImageInfo.class,
                                image.src,
                                image.order
                        ))
                )));


        if (response.isEmpty()) return null;
        if (response.get(0).getImages().get(0).getImageUrl() == null) {
            response.get(0).setImages(new ArrayList<>());
        }
        return response.get(0);
    }

}
