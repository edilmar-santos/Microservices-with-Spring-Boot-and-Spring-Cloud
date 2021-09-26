package br.com.eddy.productcompositeservice.api.dto;

import java.util.List;

import br.com.eddy.productcompositeservice.core.http.response.RecommendationResponse;
import br.com.eddy.productcompositeservice.core.http.response.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregate {
	
	private int productId;
	private String name;
	private int weight;
	private List<RecommendationResponse> recommendations;
	private List<ReviewResponse> reviews;
	private ServiceAddresses serviceAddresses;
}
