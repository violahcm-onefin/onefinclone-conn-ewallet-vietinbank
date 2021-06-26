package com.onefin.ewallet.vietinbank.model;

import lombok.Data;

@Data
public class RegisterOnlinePayResponse {

	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

}
