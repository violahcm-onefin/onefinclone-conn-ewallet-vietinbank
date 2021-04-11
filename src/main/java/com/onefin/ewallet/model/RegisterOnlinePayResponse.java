package com.onefin.ewallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class RegisterOnlinePayResponse {

	@JsonIgnore
	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

}
