package spring.reactive.web.java.decorator;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import spring.reactive.web.java.filter.TransactionFilter;

@Slf4j
public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

	private final Flux<DataBuffer> body;

	public RequestLoggingDecorator(ServerWebExchange exchange) {
		super(exchange.getRequest());

		log.info("|>> REQUEST: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getPath());
		log.info("|>> CLIENT_IP: {}", exchange.getRequest().getRemoteAddress());
		log.info("|>> REQUEST_HEADER: {}", exchange.getRequest().getHeaders());
		log.info("|>> REQUEST_PARAMETER: {}", super.getQueryParams());

		this.body = super.getBody().doOnNext(buffer -> {
			ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();

			try {
				Channels.newChannel(bodyStream).write(buffer.readableByteBuffers().next());
				String bodyString = new String(bodyStream.toByteArray());

				log.info("|>> REQUEST_BODY: {}", bodyString);
			} catch (Exception e) {
				log.error("Error reading request body", e);
			}
		});
	}

	@Override
	public Flux<DataBuffer> getBody() {
		return this.body;
	}
}
