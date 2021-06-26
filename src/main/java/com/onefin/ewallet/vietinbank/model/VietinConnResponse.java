package com.onefin.ewallet.vietinbank.model;

import lombok.Data;

@Data
public class VietinConnResponse {

	private String connectorCode;
	
	private Object vtbResponse;
	
	private String version;
	
	private String type;
}
