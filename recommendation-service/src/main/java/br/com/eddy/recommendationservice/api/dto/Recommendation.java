package br.com.eddy.recommendationservice.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Recommendation {
	
	private int productId;
	private int recommendationId;
	private String author;
	private int rate;
	private String content;
	private String serviceAddress;
}
