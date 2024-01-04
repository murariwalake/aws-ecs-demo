package com.murariwalake.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class ProductsController {

	@GetMapping("/health")
	public String health() {
		return "products : online";
	}
}