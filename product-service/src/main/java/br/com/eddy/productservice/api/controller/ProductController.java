package br.com.eddy.productservice.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eddy.productservice.api.dto.input.Product;
import br.com.eddy.productservice.domain.exception.InvalidInputException;
import br.com.eddy.productservice.domain.exception.NotFoundException;

@RestController
@RequestMapping("/products")
public class ProductController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	@GetMapping("/{productId}")
	public Product getById(@PathVariable int productId) {
		LOG.debug("/product return the found product for productId={}", productId);

		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		if (productId == 13) {
			throw new NotFoundException("No product found for productId: " + productId);
		}

		return Product.builder()
				.productId(productId)
				.name("celular")
				.weight(180)
				.serviceAddress("localhost:7001")
				.build();
	}
}
