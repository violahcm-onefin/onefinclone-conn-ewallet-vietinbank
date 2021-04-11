package com.onefin.ewallet.service;

import com.onefin.ewallet.model.PaymentByOTP;
import com.onefin.ewallet.model.PaymentByOTPResponse;
import com.onefin.ewallet.model.PaymentByToken;
import com.onefin.ewallet.model.PaymentByTokenResponse;
import com.onefin.ewallet.model.ProviderInquiry;
import com.onefin.ewallet.model.ProviderInquiryResponse;
import com.onefin.ewallet.model.RegisterOnlinePay;
import com.onefin.ewallet.model.RegisterOnlinePayResponse;
import com.onefin.ewallet.model.TokenIssue;
import com.onefin.ewallet.model.TokenIssuePayment;
import com.onefin.ewallet.model.TokenIssuePaymentResponse;
import com.onefin.ewallet.model.TokenIssueResponse;
import com.onefin.ewallet.model.TokenReIssueResponse;
import com.onefin.ewallet.model.TokenRevokeReIssue;
import com.onefin.ewallet.model.TokenRevokeResponse;
import com.onefin.ewallet.model.TransactionInquiry;
import com.onefin.ewallet.model.TransactionInquiryResponse;
import com.onefin.ewallet.model.VerifyPin;
import com.onefin.ewallet.model.VerifyPinResponse;
import com.onefin.ewallet.model.Withdraw;
import com.onefin.ewallet.model.WithdrawResponse;

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

	ProviderInquiryResponse sendProviderInquiry(ProviderInquiry data) throws Exception;

	TokenIssuePaymentResponse sendTokenIssuePayment(TokenIssuePayment data) throws Exception;

}
