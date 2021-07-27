package com.onefin.ewallet.vietinbank.model;

import com.onefin.ewallet.common.base.model.BaseConnResponse;

import lombok.Data;

@Data
public class VietinConnResponse extends BaseConnResponse {
	
	private Object vtbResponse;
	
	private String version;
	
	private String type;
}
