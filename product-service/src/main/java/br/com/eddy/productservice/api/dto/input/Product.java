package br.com.eddy.productservice.api.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Product {
	
	private int productId;
	private String name;
	private int weight;
	private String serviceAddress;
}
