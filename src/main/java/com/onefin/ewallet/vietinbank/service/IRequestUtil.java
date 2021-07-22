package com.onefin.ewallet.vietinbank.service;

import java.util.Map;

import org.springframework.http.HttpStatus;

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
import com.onefin.ewallet.vietinbank.model.VtbLinkBankBaseResponse;
import com.onefin.ewallet.vietinbank.model.Withdraw;

public interface IRequestUtil {

	VtbLinkBankBaseResponse sendRegisterOnlinePay(RegisterOnlinePay data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendTokenIssue(TokenIssue data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendVerifyPin(VerifyPin data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendTokenRevoke(TokenRevokeReIssue data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendTokenReIssue(TokenRevokeReIssue data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendPaymentByToken(PaymentByToken data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendPaymentByOTP(PaymentByOTP data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendWithdraw(Withdraw data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendTransactionInquiry(TransactionInquiry data, HttpStatus httpCode);

	Map<String, Object> sendProviderInquiry(ProviderInquiry data);

	VtbLinkBankBaseResponse sendTokenIssuePayment(TokenIssuePayment data, HttpStatus httpCode);

	VtbLinkBankBaseResponse sendRefund(Refund data, HttpStatus httpCode);

}
