package br.com.eddy.reviewservice.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.eddy.reviewservice.api.dto.Review;
import br.com.eddy.reviewservice.domain.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewServiceController {
	
	@GetMapping
	public List<Review> getReviewsByProduct(@RequestParam(value = "productId", required = true) int productId){
		
		 if (productId < 1) {
		      throw new InvalidInputException("Invalid productId: " + productId);
		    }

		    if (productId == 213) {
		      log.debug("No reviews found for productId: {}", productId);
		      return new ArrayList<>();
		    }
		
		Review r1 = Review.builder()
				.productId(productId)
				.reviewId(1)
				.author("author 1")
				.subject("subject 1")
				.content("content 1")
				.serviceAddress("localhost:7002")
				.build();
		
		Review r2 = Review.builder()
				.productId(productId)
				.reviewId(1)
				.author("author 2")
				.subject("subject 2")
				.content("content 2")
				.serviceAddress("localhost:7002")
				.build();
		
		return Arrays.asList(r1, r2);
	}
}
