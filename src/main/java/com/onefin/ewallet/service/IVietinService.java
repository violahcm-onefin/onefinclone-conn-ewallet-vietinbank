package com.onefin.ewallet.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

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
import com.onefin.ewallet.model.VietinBaseMessage;
import com.onefin.ewallet.model.Withdraw;

public interface IVietinService {

	/**
	 * Validate response from VTB
	 * 
	 * @param responseObj
	 * @param language
	 * @return
	 */
	ResponseEntity<?> validateResponse(VietinBaseMessage baseMessage);

	Map<String, String> buildVietinTokenIssuer(TokenIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinVerifyPin(VerifyPin model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinRegisterOnlinePay(RegisterOnlinePay model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinTokenRevoke(TokenRevokeReIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinTokenReIssue(TokenRevokeReIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinPaymentByToken(PaymentByToken model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinPaymentByOTP(PaymentByOTP model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinWithdraw(Withdraw model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinTransactionInquiry(TransactionInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinProviderInquiry(ProviderInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	Map<String, String> buildVietinTokenIssuerPayment(TokenIssuePayment model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

}
