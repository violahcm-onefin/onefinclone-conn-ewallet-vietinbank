package com.onefin.ewallet.model;

import lombok.Data;

@Data
public class TransactionInquiryResponse {

	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

	private String bankTransactionId;

	private String token;

	private String tokenIssueDate;

	private String tokenExpireDate;

	private String cardMask;

	private String description;
}
