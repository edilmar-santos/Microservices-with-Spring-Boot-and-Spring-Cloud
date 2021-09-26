package br.com.eddy.reviewservice.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Review {
	
	private int productId;
	private int reviewId;
	private String author;
	private String subject;
	private String content;
	private String serviceAddress;
}
