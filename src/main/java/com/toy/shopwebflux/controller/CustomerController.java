package com.toy.shopwebflux.controller;

import com.toy.shopwebflux.domain.Customer;
import com.toy.shopwebflux.repository.CustomerRepository;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final Sinks.Many<Customer> sink;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/customer/sse")
    public Flux<ServerSentEvent<Customer>> findAllSSE(@RequestParam String firstname) {
        return sink.asFlux()
                .filter(customer -> customer.getFirstName().equals(firstname))
                .map(customer -> ServerSentEvent.builder(customer).build())
                .doOnCancel(() -> sink.asFlux().blockLast());
    }

    @PostMapping("/customer")
    public Mono<Customer> save(@RequestParam String firstname, @RequestParam String lastname) {
        return customerRepository.save(new Customer(firstname, lastname))
                .doOnNext(sink::tryEmitNext);
    }
}
