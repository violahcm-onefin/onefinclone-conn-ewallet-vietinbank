package com.onefin.ewallet.vietinbank.model;

import lombok.Data;

@Data
public class TokenReIssueResponse {

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
