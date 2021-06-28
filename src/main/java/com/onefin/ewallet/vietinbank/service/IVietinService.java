package com.onefin.ewallet.vietinbank.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.onefin.ewallet.common.base.service.IBaseService;
import com.onefin.ewallet.common.domain.bank.vietin.VietinEwalletTransaction;
import com.onefin.ewallet.vietinbank.model.PaymentByOTP;
import com.onefin.ewallet.vietinbank.model.PaymentByToken;
import com.onefin.ewallet.vietinbank.model.ProviderInquiry;
import com.onefin.ewallet.vietinbank.model.Refund;
import com.onefin.ewallet.vietinbank.model.RegisterOnlinePay;
import com.onefin.ewallet.vietinbank.model.TokenIssue;
import com.onefin.ewallet.vietinbank.model.TokenIssuePayment;
import com.onefin.ewallet.vietinbank.model.TokenRevokeReIssue;
import com.onefin.ewallet.vietinbank.model.TransactionInquiry;
import com.onefin.ewallet.vietinbank.model.VerifyPin;
import com.onefin.ewallet.vietinbank.model.VietinConnResponse;
import com.onefin.ewallet.vietinbank.model.Withdraw;

public interface IVietinService extends IBaseService<VietinEwalletTransaction> {

	/**
	 * Validate response from VTB
	 * 
	 * @param responseObj
	 * @param language
	 * @return
	 */
	public VietinConnResponse validateResponse(Object data, String type);

	TokenIssue buildVietinTokenIssuer(TokenIssue model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	VerifyPin buildVietinVerifyPin(VerifyPin model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	RegisterOnlinePay buildVietinRegisterOnlinePay(RegisterOnlinePay model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TokenRevokeReIssue buildVietinTokenRevoke(TokenRevokeReIssue model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TokenRevokeReIssue buildVietinTokenReIssue(TokenRevokeReIssue model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	PaymentByToken buildVietinPaymentByToken(PaymentByToken model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	PaymentByOTP buildVietinPaymentByOTP(PaymentByOTP model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Withdraw buildVietinWithdraw(Withdraw model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TransactionInquiry buildVietinTransactionInquiry(TransactionInquiry model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	ProviderInquiry buildVietinProviderInquiry(ProviderInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TokenIssuePayment buildVietinTokenIssuerPayment(TokenIssuePayment model, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Refund buildVietinRefund(Refund data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	void backUpRequestResponse(String requestId, Object request, Object response) throws Exception;

}
