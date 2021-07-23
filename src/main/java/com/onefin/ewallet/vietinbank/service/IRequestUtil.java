package com.onefin.ewallet.vietinbank.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

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

	ResponseEntity<VtbLinkBankBaseResponse> sendRegisterOnlinePay(RegisterOnlinePay data);

	ResponseEntity<VtbLinkBankBaseResponse> sendTokenIssue(TokenIssue data);

	ResponseEntity<VtbLinkBankBaseResponse> sendVerifyPin(VerifyPin data);

	ResponseEntity<VtbLinkBankBaseResponse> sendTokenRevoke(TokenRevokeReIssue data);

	ResponseEntity<VtbLinkBankBaseResponse> sendTokenReIssue(TokenRevokeReIssue data);

	ResponseEntity<VtbLinkBankBaseResponse> sendPaymentByToken(PaymentByToken data);

	ResponseEntity<VtbLinkBankBaseResponse> sendPaymentByOTP(PaymentByOTP data);

	ResponseEntity<VtbLinkBankBaseResponse> sendWithdraw(Withdraw data);

	ResponseEntity<VtbLinkBankBaseResponse> sendTransactionInquiry(TransactionInquiry data);

	Map<String, Object> sendProviderInquiry(ProviderInquiry data);

	ResponseEntity<VtbLinkBankBaseResponse> sendTokenIssuePayment(TokenIssuePayment data);

	ResponseEntity<VtbLinkBankBaseResponse> sendRefund(Refund data);

}
