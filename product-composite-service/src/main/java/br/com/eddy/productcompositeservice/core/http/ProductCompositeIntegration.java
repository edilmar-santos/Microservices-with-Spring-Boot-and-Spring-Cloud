package br.com.eddy.productcompositeservice.core.http;

import static org.springframework.http.HttpMethod.GET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eddy.productcompositeservice.api.dto.ProductAggregate;
import br.com.eddy.productcompositeservice.api.dto.ServiceAddresses;
import br.com.eddy.productcompositeservice.api.exceptionhandler.HttpErrorInfo;
import br.com.eddy.productcompositeservice.core.http.response.ProductResponse;
import br.com.eddy.productcompositeservice.core.http.response.RecommendationResponse;
import br.com.eddy.productcompositeservice.core.http.response.ReviewResponse;
import br.com.eddy.productcompositeservice.domain.exception.InvalidInputException;
import br.com.eddy.productcompositeservice.domain.exception.NotFoundException;

@Component
public class ProductCompositeIntegration {

	private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;

	private final String productServiceUrl;
	private final String recommendationServiceUrl;
	private final String reviewServiceUrl;

	@Autowired
	public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper mapper,
			@Value("${app.product-service.host}") String productServiceHost,
			@Value("${app.product-service.port}") int productServicePort,
			@Value("${app.recommendation-service.host}") String recommendationServiceHost,
			@Value("${app.recommendation-service.port}") int recommendationServicePort,
			@Value("${app.review-service.host}") String reviewServiceHost,
			@Value("${app.review-service.port}") int reviewServicePort) {

		this.restTemplate = restTemplate;
		this.mapper = mapper;

		productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/products/";
		recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendations?productId=";
		reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews?productId=";
	}

	public ProductAggregate getProdutWithRecommendationsAndReviews(int productId){
		ProductResponse product = this.getProduct(productId);
		List<RecommendationResponse> recommendations = this.getRecommendations(productId);
		List<ReviewResponse> reviews = this.getReviews(productId);
		
		return ProductAggregate.builder()
				.productId(productId)
				.name(product.getName())
				.weight(product.getWeight())
				.recommendations(recommendations)
				.reviews(reviews)
				.serviceAddresses(
						ServiceAddresses.builder()
							.cmp("localhost:7000")
							.pro(product.getServiceAddress())
							.rec(recommendations.stream().findFirst().map(rec -> rec.getServiceAddress()).orElse(""))
							.rev(reviews.stream().findFirst().map(rev -> rev.getServiceAddress()).orElse(""))
							.build()
				)
				.build();
	}
	
	public ProductResponse getProduct(int productId) {

	    try {
	      String url = productServiceUrl + productId;
	      LOG.debug("Will call getProduct API on URL: {}", url);

	      ProductResponse product = restTemplate.getForObject(url, ProductResponse.class);
	      LOG.debug("Found a product with id: {}", product.getProductId());

	      return product;

	    } catch (HttpClientErrorException ex) {

	      switch (ex.getStatusCode()) {
	        case NOT_FOUND:
	          throw new NotFoundException(getErrorMessage(ex));

	        case UNPROCESSABLE_ENTITY:
	          throw new InvalidInputException(getErrorMessage(ex));

	        default:
	          LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
	          LOG.warn("Error body: {}", ex.getResponseBodyAsString());
	          throw ex;
	      }
	    }
	  }

	  private String getErrorMessage(HttpClientErrorException ex) {
	    try {
	      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
	    } catch (IOException ioex) {
	      return ex.getMessage();
	    }
	  }

	  public List<RecommendationResponse> getRecommendations(int productId) {

	    try {
	      String url = recommendationServiceUrl + productId;

	      LOG.debug("Will call getRecommendations API on URL: {}", url);
	      List<RecommendationResponse> recommendations = restTemplate
	        .exchange(url, GET, null, new ParameterizedTypeReference<List<RecommendationResponse>>() {})
	        .getBody();

	      LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
	      return recommendations;

	    } catch (Exception ex) {
	      LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
	      return new ArrayList<>();
	    }
	  }

	  public List<ReviewResponse> getReviews(int productId) {

	    try {
	      String url = reviewServiceUrl + productId;

	      LOG.debug("Will call getReviews API on URL: {}", url);
	      List<ReviewResponse> reviews = restTemplate
	        .exchange(url, GET, null, new ParameterizedTypeReference<List<ReviewResponse>>() {})
	        .getBody();

	      LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
	      return reviews;

	    } catch (Exception ex) {
	      LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
	      return new ArrayList<>();
	    }
	  }
}
