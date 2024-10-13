package com.skku.skkuduler.domain.notification;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "notification")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long userId;

    private Long eventId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    enum NotificationStatus {
        READ,
        UNREAD
    }
}
