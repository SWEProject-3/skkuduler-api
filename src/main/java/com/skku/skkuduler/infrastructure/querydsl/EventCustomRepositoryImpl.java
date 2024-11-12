package com.skku.skkuduler.infrastructure.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.skkuduler.domain.calender.QCalendar;
import com.skku.skkuduler.domain.calender.QCalendarEvent;
import com.skku.skkuduler.domain.calender.QEvent;
import com.skku.skkuduler.domain.calender.QImage;
import com.skku.skkuduler.domain.department.QDepartment;
import com.skku.skkuduler.domain.user.QUser;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
        QCalendar calendar = QCalendar.calendar;
        QCalendarEvent calendarEvent = QCalendarEvent.calendarEvent;

        boolean isAddedToMyCalendar = jpaQueryFactory
                .select(calendarEvent.event.eventId)
                .from(calendarEvent)
                .join(calendarEvent.calendar, calendar)
                .where(calendar.user.userId.eq(userId).and(calendarEvent.event.eventId.eq(eventId)))
                .fetchFirst() != null;

        List<CalendarEventDetailDto> response = jpaQueryFactory
                .selectFrom(event)
                .distinct()
                .leftJoin(user).on(event.userId.eq(user.userId))
                .leftJoin(department).on(event.departmentId.eq(department.departmentId))
                .leftJoin(like).on(like.eventId.eq(event.eventId))
                .leftJoin(event.images,image)
                .where(event.eventId.eq(eventId))
                .orderBy(image.order.asc())
                .transform(groupBy(event.eventId).list(Projections.fields(CalendarEventDetailDto.class,
                        event.eventId.as("eventId"),
                        event.isUserEvent.when(true).then(false).otherwise(true).as("isDepartmentEvent"),
                        event.userId.eq(userId).as("isMyEvent"),
                        Expressions.asBoolean(isAddedToMyCalendar).as("isAddedToMyCalendar"), // `isAddedToMyCalendar` 추가
                        Expressions.asNumber(user.userId).as("ownerUserId"),
                        Expressions.asString(user.name).as("ownerName"),
                        Expressions.asNumber(department.departmentId).as("departmentId"),
                        Expressions.asString(department.name).as("departmentName"),
                        Expressions.asNumber(like.count().coalesce(0)).as("likeCount"),
                        Projections.constructor(CalendarEventDetailDto.EventInfo.class,
                                event.eventId,
                                event.title,
                                event.content,
                                event.colorCode,
                                event.startDateTime,
                                event.endDateTime
                                ).as("eventInfo"),
                        list(Projections.constructor(CalendarEventDetailDto.ImageInfo.class,
                                image.src, image.order).as("images")).as("images")
                        )));
        if(response.isEmpty()) return null;
        if(response.get(0).getImages().get(0).getImageUrl() == null){
            response.get(0).setImages( new ArrayList<>());
        }
    return response.get(0);
    }

}
