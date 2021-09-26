package br.com.eddy.productcompositeservice.core.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

	private int productId;
	private int recommendationId;
	private String author;
	private int rate;
	private String content;
	private String serviceAddress;
}
