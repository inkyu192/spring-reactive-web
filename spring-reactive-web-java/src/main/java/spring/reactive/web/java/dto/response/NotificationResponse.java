package spring.reactive.web.java.dto.response;

import spring.reactive.web.java.domain.Notification;
import spring.reactive.web.java.domain.NotificationReception;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long notificationReceptionId,
        Long memberId,
        String type,
        String title,
        String message,
        String url,
        LocalDateTime createdDate
) {
    public NotificationResponse(Notification notification, NotificationReception notificationReception) {
        this(
                notificationReception.getNotificationReceptionId(),
                notificationReception.getMemberId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getUrl(),
                notificationReception.getCreatedDate()
        );
    }
}
