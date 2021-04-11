package com.onefin.ewallet.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.http.ResponseEntity;

import com.onefin.ewallet.model.PaymentByOTP;
import com.onefin.ewallet.model.PaymentByToken;
import com.onefin.ewallet.model.ProviderInquiry;
import com.onefin.ewallet.model.RegisterOnlinePay;
import com.onefin.ewallet.model.TokenIssue;
import com.onefin.ewallet.model.TokenIssuePayment;
import com.onefin.ewallet.model.TokenRevokeReIssue;
import com.onefin.ewallet.model.TransactionInquiry;
import com.onefin.ewallet.model.VerifyPin;
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
	public ResponseEntity<?> validateResponse(Object data);

	TokenIssue buildVietinTokenIssuer(TokenIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	VerifyPin buildVietinVerifyPin(VerifyPin model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	RegisterOnlinePay buildVietinRegisterOnlinePay(RegisterOnlinePay model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TokenRevokeReIssue buildVietinTokenRevoke(TokenRevokeReIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TokenRevokeReIssue buildVietinTokenReIssue(TokenRevokeReIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	PaymentByToken buildVietinPaymentByToken(PaymentByToken model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	PaymentByOTP buildVietinPaymentByOTP(PaymentByOTP model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Withdraw buildVietinWithdraw(Withdraw model) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TransactionInquiry buildVietinTransactionInquiry(TransactionInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	ProviderInquiry buildVietinProviderInquiry(ProviderInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	TokenIssuePayment buildVietinTokenIssuerPayment(TokenIssuePayment model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

}
