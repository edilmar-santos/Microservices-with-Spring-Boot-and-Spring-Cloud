package br.com.eddy.productcompositeservice.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eddy.productcompositeservice.api.dto.ProductAggregate;
import br.com.eddy.productcompositeservice.core.http.ProductCompositeIntegration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product-compose")
public class ProductComposeController {
	
	private final ProductCompositeIntegration productCompositeIntegration;
	
	@GetMapping("/{productId}")
	public ProductAggregate getProduct(@PathVariable int productId) {
		return productCompositeIntegration.getProdutWithRecommendationsAndReviews(productId);
	}
}
