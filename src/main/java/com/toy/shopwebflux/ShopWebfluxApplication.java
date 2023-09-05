package com.toy.shopwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcAuditing
@EnableR2dbcRepositories
@SpringBootApplication
public class ShopWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopWebfluxApplication.class, args);
	}

}
