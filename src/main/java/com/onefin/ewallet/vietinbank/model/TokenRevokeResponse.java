package com.onefin.ewallet.vietinbank.model;

import lombok.Data;

@Data
public class TokenRevokeResponse {

	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

}
