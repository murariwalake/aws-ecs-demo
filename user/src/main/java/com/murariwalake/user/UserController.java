package com.murariwalake.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/api/v1")
@RestController
@Slf4j
public class UserController {
	private static final String HEALTH = "/api/v1/health";

	//get product service url form environment variable
	@Value("${product.service.url}")
	private String productServiceUrl;

	@GetMapping("/health")
	public String health() {
		return "users : online,\n" + getProductServiceHealth();
	}

	//get product service health
	private String getProductServiceHealth() {
		//make http call to product service health API using restTemplate
		try {
			log.info("products service url : {}", productServiceUrl + HEALTH);
			return new RestTemplate().getForObject(productServiceUrl + HEALTH, String.class);
		} catch (Exception e) {
			log.error("Error while calling product service health API", e);
			return "products : offline";
		}
	}
}