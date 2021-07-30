package com.onefin.ewallet.vietinbank.linkbank.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TokenIssuePayment {

	@Size(max = 20)
	@NotEmpty(message = "Not empty")
	private String cardNumber;

	@Size(max = 6)
	// @NotEmpty(message = "Not empty")
	private String cardIssueDate;

	@Size(max = 50)
	@NotEmpty(message = "Not empty")
	private String cardHolderName;

	@Size(max = 31)
	@NotEmpty(message = "Not empty")
	private String amount;

	@Size(max = 3)
	@NotEmpty(message = "Not empty")
	private String currencyCode;

	@Size(max = 30)
	@NotEmpty(message = "Not empty")
	private String providerCustId;

	@Size(max = 30)
	@NotEmpty(message = "Not empty")
	private String custPhoneNo;

	@Size(max = 30)
	@NotEmpty(message = "Not empty")
	private String custIDNo;

	@Size(max = 16)
	private String clientIP;

	@Size(max = 14)
	@NotEmpty(message = "Not empty")
	private String transTime;

	@Size(max = 12)
	@NotEmpty(message = "Not empty")
	private String requestId;

	@Size(max = 15)
	@NotEmpty(message = "Not empty")
	private String channel;

	@Size(max = 3)
	private String language;

	@Size(max = 30)
	private String mac;

	private String providerId;

	private String merchantId;

	private String version;

	private String signature;

	public TokenIssuePayment() {
		this.cardIssueDate = new String();
		this.custPhoneNo = new String();
		this.custIDNo = new String();
		this.clientIP = new String();
		this.language = new String();
		this.mac = new String();
	}

}
