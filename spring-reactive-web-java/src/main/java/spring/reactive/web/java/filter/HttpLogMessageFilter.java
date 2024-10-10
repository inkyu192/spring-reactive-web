package spring.reactive.web.java.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import spring.reactive.web.java.decorator.ExchangeLoggingDecorator;

@Component
@Order(Integer.MIN_VALUE + 1)
public class HttpLogMessageFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(new ExchangeLoggingDecorator(exchange));
	}
}
