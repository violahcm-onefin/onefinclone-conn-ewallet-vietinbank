package com.onefin.ewallet.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.onefin.ewallet.model.EwalletTransaction;
import com.onefin.ewallet.model.PaymentByOTP;
import com.onefin.ewallet.model.PaymentByToken;
import com.onefin.ewallet.model.ProviderInquiry;
import com.onefin.ewallet.model.RegisterOnlinePay;
import com.onefin.ewallet.model.TokenIssue;
import com.onefin.ewallet.model.TokenIssuePayment;
import com.onefin.ewallet.model.TokenRevokeReIssue;
import com.onefin.ewallet.model.TransactionInquiry;
import com.onefin.ewallet.model.VerifyPin;
import com.onefin.ewallet.model.VietinConnResponse;
import com.onefin.ewallet.model.Withdraw;
import com.onefin.service.IBaseService;

public interface IVietinService extends IBaseService {

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

	EwalletTransaction save(EwalletTransaction transData) throws Exception;

}
