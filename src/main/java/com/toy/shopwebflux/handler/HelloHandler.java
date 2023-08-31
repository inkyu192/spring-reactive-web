package com.toy.shopwebflux.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class HelloHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));

        log.info("id: {}", id);

        return ServerResponse.ok().bodyValue("hello!");
    }
}
