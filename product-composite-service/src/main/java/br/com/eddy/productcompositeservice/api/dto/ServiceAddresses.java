package br.com.eddy.productcompositeservice.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ServiceAddresses {

	private String cmp;
	private String pro;
	private String rev;
	private String rec;
}
