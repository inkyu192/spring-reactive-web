package spring.reactive.web.java.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
public class Notification extends Base implements Persistable<Long> {

    @Id
    private Long notificationId;
    private String type;
    private String title;
    private String message;
    private String url;

    @Override
    public Long getId() {
        return notificationId;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
