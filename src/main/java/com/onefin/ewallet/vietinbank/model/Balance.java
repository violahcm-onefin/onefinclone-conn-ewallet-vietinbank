package com.onefin.ewallet.vietinbank.model;

import lombok.Data;

@Data
public class Balance {

	private String acctId;

	private String amount;

	private String currencyCode;

}
