package spring.reactive.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public Mono<String> getExample() {
        return Mono.just("Hello, Spring REST Docs!");

    }
}
