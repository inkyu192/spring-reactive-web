package spring.reactive.web.java.decorator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class ExchangeLoggingDecorator extends ServerWebExchangeDecorator {

	private final RequestLoggingDecorator requestLoggingDecorator;
	private final ResponseLoggingDecorator responseLoggingDecorator;

	public ExchangeLoggingDecorator(ServerWebExchange delegate) {
		super(delegate);
		requestLoggingDecorator = new RequestLoggingDecorator(delegate);
		responseLoggingDecorator = new ResponseLoggingDecorator(delegate);
	}

	@Override
	public ServerHttpRequest getRequest() {
		return this.requestLoggingDecorator;
	}

	@Override
	public ServerHttpResponse getResponse() {
		return this.responseLoggingDecorator;
	}
}
