package com.banking.funding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FundingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundingServiceApplication.class, args);
	}

}
