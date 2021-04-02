package com.onefin.ewallet.model;

import lombok.Data;

@Data
public class Balance {

	private String acctId;

	private String amount;

	private String currencyCode;

}
