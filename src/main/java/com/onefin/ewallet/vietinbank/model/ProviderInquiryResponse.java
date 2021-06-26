package com.onefin.ewallet.vietinbank.model;

import java.util.List;

import lombok.Data;

@Data
public class ProviderInquiryResponse {

	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

	private List<Balance> balances;

}
