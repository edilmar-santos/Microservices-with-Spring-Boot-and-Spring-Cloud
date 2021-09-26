package br.com.eddy.recommendationservice.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.eddy.recommendationservice.api.dto.Recommendation;
import br.com.eddy.recommendationservice.domain.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/recommendations")
@Slf4j
public class RecommendationController {
	
	@GetMapping()
	public List<Recommendation> getRecommendationByProductId(@RequestParam(value = "productId", required = true) int productId){		
		
		
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		if (productId == 113) {
			log.debug("No recommendations found for productId: {}", productId);
			return new ArrayList<>();
		}
		
		Recommendation r1 = Recommendation.builder()
			.productId(productId)
			.author("t1")
			.rate(10)
			.recommendationId(1)
			.content("muito bom 1")
			.serviceAddress("localhost:7003")
			.build();
		
		Recommendation r2 = Recommendation.builder()
				.productId(productId)
				.author("t2")
				.rate(9)
				.recommendationId(1)
				.content("muito bom 2")
				.serviceAddress("localhost:7003")
				.build();
		
		return Arrays.asList(r1, r2);
	}
	
}
