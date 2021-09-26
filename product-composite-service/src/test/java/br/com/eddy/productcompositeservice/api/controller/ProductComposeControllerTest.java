package br.com.eddy.productcompositeservice.api.controller;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.eddy.productcompositeservice.api.dto.ProductAggregate;
import br.com.eddy.productcompositeservice.api.dto.ServiceAddresses;
import br.com.eddy.productcompositeservice.core.http.ProductCompositeIntegration;
import br.com.eddy.productcompositeservice.core.http.response.RecommendationResponse;
import br.com.eddy.productcompositeservice.core.http.response.ReviewResponse;
import br.com.eddy.productcompositeservice.domain.exception.InvalidInputException;
import br.com.eddy.productcompositeservice.domain.exception.NotFoundException;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProductComposeControllerTest {

	private static final String PRODUCT_COMPOSE_PATH = "/product-compose/";
	private static final int PRODUCT_ID_OK = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 2;
	private static final int PRODUCT_ID_INVALID = 3;


	@Autowired
	private WebTestClient client;

	@MockBean
	private ProductCompositeIntegration compositeIntegration;

	@BeforeEach
	void setup() {
		when(compositeIntegration.getProdutWithRecommendationsAndReviews(PRODUCT_ID_OK))
				.thenReturn(buildProductComposite());
		
		when(compositeIntegration.getProdutWithRecommendationsAndReviews(PRODUCT_ID_NOT_FOUND))
			.thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));
		
		when(compositeIntegration.getProdutWithRecommendationsAndReviews(PRODUCT_ID_INVALID))
			.thenThrow(new InvalidInputException("INVALID: " + PRODUCT_ID_INVALID));
	}

	@Test
	void getProductById() {

		client.get().uri(PRODUCT_COMPOSE_PATH + PRODUCT_ID_OK)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK);
	}

	@Test
	void getProductNotFound() {

		client.get().uri(PRODUCT_COMPOSE_PATH + PRODUCT_ID_NOT_FOUND).accept(APPLICATION_JSON).exchange().expectStatus()
				.isNotFound().expectHeader().contentType(APPLICATION_JSON).expectBody().jsonPath("$.path")
				.isEqualTo(PRODUCT_COMPOSE_PATH + PRODUCT_ID_NOT_FOUND).jsonPath("$.message")
				.isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);
	}

	@Test
	void getProductInvalidInput() {

		client.get().uri(PRODUCT_COMPOSE_PATH + PRODUCT_ID_INVALID).accept(APPLICATION_JSON).exchange().expectStatus()
				.isEqualTo(UNPROCESSABLE_ENTITY).expectHeader().contentType(APPLICATION_JSON).expectBody()
				.jsonPath("$.path").isEqualTo(PRODUCT_COMPOSE_PATH + PRODUCT_ID_INVALID).jsonPath("$.message")
				.isEqualTo("INVALID: " + PRODUCT_ID_INVALID);
	}

	private ProductAggregate buildProductComposite() {
		return ProductAggregate.builder()
				.productId(1)
				.name("product_valid")
				.weight(1)
				.recommendations(singletonList(new RecommendationResponse(1, 1, "autor_valid", 10, "content_valid", "rcm_serviceAddress_valid")))
				.reviews(singletonList(new ReviewResponse(1, 1, "author_valid", "subject_valid", "content_valid", "rev_serviceAddress_valid")))
				.serviceAddresses(
						ServiceAddresses
							.builder()
							.cmp("localhost:7000")
							.pro("pdr_serviceAddress_valid")
							.rec("rcm_serviceAddress_valid")
							.rev("rev_serviceAddress_valid")
							.build()
				)
				.build();
	}
}
