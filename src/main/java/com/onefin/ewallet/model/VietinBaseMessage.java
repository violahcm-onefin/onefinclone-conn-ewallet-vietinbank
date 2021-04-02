package com.onefin.ewallet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class VietinBaseMessage {

	private String signature;

	private String providerId;

	private String merchantId;

	private String requestId;

	private Status status;

	private List<Balance> balances;

	private String bankTransactionId;

	private String token;

	private String tokenIssueDate;

	private String tokenExpireDate;

	private String cardMask;

	private String description;

	@JsonIgnore
	public boolean isValidMessage() {
		if (providerId == null || providerId.trim().isEmpty() || requestId == null || requestId.trim().isEmpty()
				|| signature == null || signature.trim().isEmpty() || merchantId == null
				|| merchantId.trim().isEmpty()) {

			return false;
		}
		return true;
	}
}
