package com.onefin.ewallet.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.onefin.ewallet.common.base.model.AbstractBaseEwalletTrans;
import com.onefin.ewallet.common.base.model.StringListConverter;

import lombok.Data;

@Data
@Entity
@Table(name = "VietinEwalletTransaction")
public class EwalletTransaction extends AbstractBaseEwalletTrans {

	@Column
	private String tranStatus; // PENDING, SUCCESS, ERROR

	@Column
	private String vietinResult;

	@Column
	private String connResult;

	@Column
	private String linkType;

	@Column
	private String currency;

	@Column
	private BigDecimal amount;

	@Column
	private String refundId;

	@Column
	private String transDate;

	@Column
	private String merchantId;

	@Column
	private String bankTransactionId;

	@Column
	private String token;

	@Column
	private String tokenIssueDate;

	@Column
	@Convert(converter = StringListConverter.class)
	private List<String> orgReqResId1;

	@Column
	@Convert(converter = StringListConverter.class)
	private List<String> otpReqResId;

}
