package spring.reactive.web.java.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Table
public class NotificationReception extends Base implements Persistable<Long> {

    @Id
    private Long notificationReceptionId;
    private Boolean isRead;
    private LocalDateTime readDate;
    private Long notificationId;
    private Long memberId;

    @Override
    public Long getId() {
        return notificationReceptionId;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public static NotificationReception create(Long memberId, Long notificationId) {
        NotificationReception notificationReception = new NotificationReception();

        notificationReception.notificationId = notificationId;
        notificationReception.memberId = memberId;
        notificationReception.isRead = false;

        return notificationReception;
    }
}
