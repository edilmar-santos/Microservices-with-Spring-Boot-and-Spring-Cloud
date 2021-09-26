package br.com.eddy.productcompositeservice.core.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	
	private int productId;
	private String name;
	private int weight;
	private String serviceAddress;
}
