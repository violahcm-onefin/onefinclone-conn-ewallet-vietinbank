package com.onefin.ewallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class PaymentByOTPResponse {

	@JsonProperty(access = Access.WRITE_ONLY)
	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

}
