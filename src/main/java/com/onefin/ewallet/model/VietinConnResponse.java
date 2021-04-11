package com.onefin.ewallet.model;

import lombok.Data;

@Data
public class VietinConnResponse {

	private String connectorCode;
	
	private Object vtbResponse;
	
	private String version;
}
