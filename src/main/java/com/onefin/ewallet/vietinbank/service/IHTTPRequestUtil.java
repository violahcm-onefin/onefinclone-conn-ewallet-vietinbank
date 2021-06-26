package com.onefin.ewallet.vietinbank.service;

import java.util.Map;

import com.onefin.ewallet.vietinbank.model.PaymentByOTP;
import com.onefin.ewallet.vietinbank.model.PaymentByOTPResponse;
import com.onefin.ewallet.vietinbank.model.PaymentByToken;
import com.onefin.ewallet.vietinbank.model.PaymentByTokenResponse;
import com.onefin.ewallet.vietinbank.model.ProviderInquiry;
import com.onefin.ewallet.vietinbank.model.RegisterOnlinePay;
import com.onefin.ewallet.vietinbank.model.RegisterOnlinePayResponse;
import com.onefin.ewallet.vietinbank.model.TokenIssue;
import com.onefin.ewallet.vietinbank.model.TokenIssuePayment;
import com.onefin.ewallet.vietinbank.model.TokenIssuePaymentResponse;
import com.onefin.ewallet.vietinbank.model.TokenIssueResponse;
import com.onefin.ewallet.vietinbank.model.TokenReIssueResponse;
import com.onefin.ewallet.vietinbank.model.TokenRevokeReIssue;
import com.onefin.ewallet.vietinbank.model.TokenRevokeResponse;
import com.onefin.ewallet.vietinbank.model.TransactionInquiry;
import com.onefin.ewallet.vietinbank.model.TransactionInquiryResponse;
import com.onefin.ewallet.vietinbank.model.VerifyPin;
import com.onefin.ewallet.vietinbank.model.VerifyPinResponse;
import com.onefin.ewallet.vietinbank.model.Withdraw;
import com.onefin.ewallet.vietinbank.model.WithdrawResponse;

public interface IHTTPRequestUtil {

	RegisterOnlinePayResponse sendRegisterOnlinePay(RegisterOnlinePay data) throws Exception;

	TokenIssueResponse sendTokenIssue(TokenIssue data) throws Exception;

	VerifyPinResponse sendVerifyPin(VerifyPin data) throws Exception;

	TokenRevokeResponse sendTokenRevoke(TokenRevokeReIssue data) throws Exception;

	TokenReIssueResponse sendTokenReIssue(TokenRevokeReIssue data) throws Exception;

	PaymentByTokenResponse sendPaymentByToken(PaymentByToken data) throws Exception;

	PaymentByOTPResponse sendPaymentByOTP(PaymentByOTP data) throws Exception;

	WithdrawResponse sendWithdraw(Withdraw data) throws Exception;

	TransactionInquiryResponse sendTransactionInquiry(TransactionInquiry data) throws Exception;

	Map<String, Object> sendProviderInquiry(ProviderInquiry data) throws Exception;

	TokenIssuePaymentResponse sendTokenIssuePayment(TokenIssuePayment data) throws Exception;

}
