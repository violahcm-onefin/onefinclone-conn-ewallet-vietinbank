package com.onefin.ewallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TokenReIssueResponse {

	@JsonIgnore
	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;
	
	private String token;
	
	private String tokenIssueDate;
	
	private String tokenExpireDate;
	
	private String cardMask;

}
