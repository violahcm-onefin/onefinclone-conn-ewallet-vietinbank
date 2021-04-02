package com.onefin.ewallet.model;

import lombok.Data;

@Data
public class VietinConnectorResponse {

	private String connectorCode;
	
	private VietinBaseMessage vtbResponse;
	
	private String version;
}
