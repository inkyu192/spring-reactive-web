package spring.reactive.web.java.domain;

import lombok.Getter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Table
public class NotificationReception extends Base implements Persistable<Long> {

    private Long notificationReceptionId;
    private Boolean isRead;
    private LocalDateTime readDate;
    private Long notificationId;

    @Override
    public Long getId() {
        return notificationReceptionId;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
