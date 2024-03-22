package spring.reactive.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.dto.response.ApiResponse;
import spring.reactive.web.java.dto.response.NotificationResponse;
import spring.reactive.web.java.service.NotificationService;

@RestController
@RequestMapping("notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Mono<ApiResponse<Page<NotificationResponse>>> findNotifications(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) Long memberId
    ) {
        return notificationService.findNotifications(pageable, memberId)
                .map(ApiResponse::new);
    }
}
