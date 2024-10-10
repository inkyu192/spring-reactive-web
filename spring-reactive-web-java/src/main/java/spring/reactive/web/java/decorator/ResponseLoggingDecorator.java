package spring.reactive.web.java.decorator;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.filter.TransactionFilter;

@Slf4j
public class ResponseLoggingDecorator extends ServerHttpResponseDecorator {

	public ResponseLoggingDecorator(ServerWebExchange exchange) {
		super(exchange.getResponse());
	}

	@Override
	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		return super.writeWith(
			Flux.from(body).doOnNext(buffer -> {
				ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();
				try {
					Channels.newChannel(bodyStream).write(buffer.readableByteBuffers().next());
					String bodyString = new String(bodyStream.toByteArray());

					log.info("|>> RESPONSE_BODY: {}", bodyString);
				} catch (Exception e) {
					log.error("Error reading response body", e);
				}
			})
		);
	}
}
