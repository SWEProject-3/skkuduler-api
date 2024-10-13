package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
