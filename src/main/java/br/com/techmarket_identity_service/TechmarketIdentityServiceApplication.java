package br.com.techmarket_identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TechmarketIdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechmarketIdentityServiceApplication.class, args);
	}

}
