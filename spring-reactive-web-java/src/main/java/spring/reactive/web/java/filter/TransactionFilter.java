package spring.reactive.web.java.filter;

import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
@Order(Integer.MIN_VALUE)
public class TransactionFilter implements WebFilter {

	public static final String TRANSACTION_ID = "transactionId";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange.mutate().build())
			.contextWrite(Context.of(TRANSACTION_ID, UUID.randomUUID().toString()));
	}
}
