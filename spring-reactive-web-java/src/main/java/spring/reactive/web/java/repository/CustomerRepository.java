package spring.reactive.web.java.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import spring.reactive.web.java.domain.Customer;

public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
}
