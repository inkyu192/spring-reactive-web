package com.toy.shopwebflux.repository;

import com.toy.shopwebflux.domain.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
}
