package com.onefin.ewallet.common;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import com.onefin.ewallet.service.ConfigLoader;

@Service
public class VietinUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinUtils.class);

	@Value("${vietin.onefinPrivateKey}")
	private String onefinPrivateKey;

	@Value("${vietin.vtbPublicKey}")
	private String vtbPublicKey;

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	private MessageUtil msgUtil;

	@Autowired
	private EncryptUtil encryptUtil;

	public Map<String, String> buildVietinTokenIssuer(TokenIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_CARDNUMBER, model.getCardNumber());
		data.put(OneFinConstants.VTB_CARDISSUEDATE, model.getCardIssueDate());
		data.put(OneFinConstants.VTB_CARDHOLDERNAME, model.getCardHolderName());
		data.put(OneFinConstants.VTB_PROVIDERCUSTID, model.getProviderCustId());
		data.put(OneFinConstants.VTB_CUSTPHONENO, model.getCustPhoneNo());
		data.put(OneFinConstants.VTB_CUSTIDNO, model.getCustIDNo());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_CARDNUMBER) + data.get(OneFinConstants.VTB_CARDISSUEDATE)
				+ data.get(OneFinConstants.VTB_CARDHOLDERNAME) + data.get(OneFinConstants.VTB_PROVIDERCUSTID)
				+ data.get(OneFinConstants.VTB_CUSTPHONENO) + data.get(OneFinConstants.VTB_CUSTIDNO)
				+ data.get(OneFinConstants.VTB_CLIENTIP) + data.get(OneFinConstants.VTB_TRANSTIME)
				+ data.get(OneFinConstants.VTB_REQUESTID) + data.get(OneFinConstants.VTB_PROVIDERID)
				+ data.get(OneFinConstants.VTB_MERCHANTID) + data.get(OneFinConstants.VTB_CHANNEL)
				+ data.get(OneFinConstants.VTB_VERSION) + data.get(OneFinConstants.VTB_LANGUAGE)
				+ data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinVerifyPin(VerifyPin model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_OTP, model.getOtp());
		data.put(OneFinConstants.VTB_VERIFY_TRANSID, model.getVerifyTransactionId());
		data.put(OneFinConstants.VTB_VERIFY_BY, model.getVerifyBy());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_OTP) + data.get(OneFinConstants.VTB_VERIFY_TRANSID)
				+ data.get(OneFinConstants.VTB_VERIFY_BY) + data.get(OneFinConstants.VTB_TRANSTIME)
				+ data.get(OneFinConstants.VTB_CLIENTIP) + data.get(OneFinConstants.VTB_REQUESTID)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinRegisterOnlinePay(RegisterOnlinePay model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_CARDNUMBER, model.getCardNumber());
		data.put(OneFinConstants.VTB_CARDISSUEDATE, model.getCardIssueDate());
		data.put(OneFinConstants.VTB_CARDHOLDERNAME, model.getCardHolderName());
		data.put(OneFinConstants.VTB_PROVIDERCUSTID, model.getProviderCustId());
		data.put(OneFinConstants.VTB_CUSTIDNO, model.getCustIDNo());
		data.put(OneFinConstants.VTB_CUSTIDISSUEDATE, model.getCustIDIssueDate());
		data.put(OneFinConstants.VTB_CUSTIDISSUEBY, model.getCustIDIssueBy());
		data.put(OneFinConstants.VTB_CUSTPHONENO, model.getCustPhoneNo());
		data.put(OneFinConstants.VTB_CUSTGENDER, model.getCustGender());
		data.put(OneFinConstants.VTB_CUSTBIRTHDAY, model.getCustBirthday());
		data.put(OneFinConstants.VTB_CUSTEMAIL, model.getCustEmail());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_CARDNUMBER) + data.get(OneFinConstants.VTB_CARDISSUEDATE)
				+ data.get(OneFinConstants.VTB_CARDHOLDERNAME) + data.get(OneFinConstants.VTB_PROVIDERCUSTID)
				+ data.get(OneFinConstants.VTB_CUSTIDNO) + data.get(OneFinConstants.VTB_CUSTIDISSUEDATE)
				+ data.get(OneFinConstants.VTB_CUSTIDISSUEBY) + data.get(OneFinConstants.VTB_CUSTPHONENO)
				+ data.get(OneFinConstants.VTB_CUSTGENDER) + data.get(OneFinConstants.VTB_CUSTBIRTHDAY)
				+ data.get(OneFinConstants.VTB_CLIENTIP) + data.get(OneFinConstants.VTB_TRANSTIME)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinTokenRevoke(TokenRevokeReIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_TOKEN, model.getToken());
		data.put(OneFinConstants.VTB_TOKEN_ISSUE_DATE, model.getTokenIssueDate());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_TOKEN) + data.get(OneFinConstants.VTB_TOKEN_ISSUE_DATE)
				+ data.get(OneFinConstants.VTB_TRANSTIME) + data.get(OneFinConstants.VTB_CLIENTIP)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinTokenReIssue(TokenRevokeReIssue model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_TOKEN, model.getToken());
		data.put(OneFinConstants.VTB_TOKEN_ISSUE_DATE, model.getTokenIssueDate());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_TOKEN) + data.get(OneFinConstants.VTB_TOKEN_ISSUE_DATE)
				+ data.get(OneFinConstants.VTB_TRANSTIME) + data.get(OneFinConstants.VTB_CLIENTIP)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinPaymentByToken(PaymentByToken model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_TOKEN, model.getToken());
		data.put(OneFinConstants.VTB_TOKEN_ISSUE_DATE, model.getTokenIssueDate());
		data.put(OneFinConstants.VTB_AMOUNT, model.getAmount());
		data.put(OneFinConstants.VTB_CURRENCY_CODE, model.getCurrencyCode());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_PAYMETHOD, model.getPayMethod());
		data.put(OneFinConstants.VTB_GOODSTYPE, model.getGoodsType());
		data.put(OneFinConstants.VTB_BILLNO, model.getBillNo());
		data.put(OneFinConstants.VTB_REMARK, model.getRemark());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_TOKEN) + data.get(OneFinConstants.VTB_TOKEN_ISSUE_DATE)
				+ data.get(OneFinConstants.VTB_AMOUNT) + data.get(OneFinConstants.VTB_CURRENCY_CODE)
				+ data.get(OneFinConstants.VTB_TRANSTIME) + data.get(OneFinConstants.VTB_CLIENTIP)
				+ data.get(OneFinConstants.VTB_PAYMETHOD) + data.get(OneFinConstants.VTB_GOODSTYPE)
				+ data.get(OneFinConstants.VTB_BILLNO) + data.get(OneFinConstants.VTB_REMARK)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinPaymentByOTP(PaymentByOTP model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_TOKEN, model.getToken());
		data.put(OneFinConstants.VTB_TOKEN_ISSUE_DATE, model.getTokenIssueDate());
		data.put(OneFinConstants.VTB_AMOUNT, model.getAmount());
		data.put(OneFinConstants.VTB_CURRENCY_CODE, model.getCurrencyCode());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_PAYMETHOD, model.getPayMethod());
		data.put(OneFinConstants.VTB_GOODSTYPE, model.getGoodsType());
		data.put(OneFinConstants.VTB_BILLNO, model.getBillNo());
		data.put(OneFinConstants.VTB_REMARK, model.getRemark());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_TOKEN) + data.get(OneFinConstants.VTB_TOKEN_ISSUE_DATE)
				+ data.get(OneFinConstants.VTB_AMOUNT) + data.get(OneFinConstants.VTB_CURRENCY_CODE)
				+ data.get(OneFinConstants.VTB_TRANSTIME) + data.get(OneFinConstants.VTB_CLIENTIP)
				+ data.get(OneFinConstants.VTB_PAYMETHOD) + data.get(OneFinConstants.VTB_GOODSTYPE)
				+ data.get(OneFinConstants.VTB_BILLNO) + data.get(OneFinConstants.VTB_REMARK)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinWithdraw(Withdraw model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_TOKEN, model.getToken());
		data.put(OneFinConstants.VTB_TOKEN_ISSUE_DATE, model.getTokenIssueDate());
		data.put(OneFinConstants.VTB_AMOUNT, model.getAmount());
		data.put(OneFinConstants.VTB_CURRENCY_CODE, model.getCurrencyCode());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_BENNAME, model.getBenName());
		data.put(OneFinConstants.VTB_BENACCNO, model.getBenAcctNo());
		data.put(OneFinConstants.VTB_BENIDNO, model.getBenIDNo());
		data.put(OneFinConstants.VTB_BENADDINFO, model.getBenAddInfo());
		data.put(OneFinConstants.VTB_REMARK, model.getRemark());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_TOKEN) + data.get(OneFinConstants.VTB_TOKEN_ISSUE_DATE)
				+ data.get(OneFinConstants.VTB_AMOUNT) + data.get(OneFinConstants.VTB_CURRENCY_CODE)
				+ data.get(OneFinConstants.VTB_TRANSTIME) + data.get(OneFinConstants.VTB_CLIENTIP)
				+ data.get(OneFinConstants.VTB_BENNAME) + data.get(OneFinConstants.VTB_BENACCNO)
				+ data.get(OneFinConstants.VTB_BENIDNO) + data.get(OneFinConstants.VTB_BENADDINFO)
				+ data.get(OneFinConstants.VTB_REMARK) + data.get(OneFinConstants.VTB_PROVIDERID)
				+ data.get(OneFinConstants.VTB_MERCHANTID) + data.get(OneFinConstants.VTB_CHANNEL)
				+ data.get(OneFinConstants.VTB_VERSION) + data.get(OneFinConstants.VTB_LANGUAGE)
				+ data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinTransactionInquiry(TransactionInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_QUERYTRANSACTIONID, model.getQueryTransactionId());
		data.put(OneFinConstants.VTB_QUERYTYPE, model.getQueryType());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_QUERYTRANSACTIONID) + data.get(OneFinConstants.VTB_QUERYTYPE)
				+ data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinProviderInquiry(ProviderInquiry model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_PROVIDERID) + data.get(OneFinConstants.VTB_MERCHANTID)
				+ data.get(OneFinConstants.VTB_CHANNEL) + data.get(OneFinConstants.VTB_VERSION)
				+ data.get(OneFinConstants.VTB_LANGUAGE) + data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	public Map<String, String> buildVietinTokenIssuerPayment(TokenIssuePayment model)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Map<String, String> data = new HashMap<>();
		String providerId = configLoader.getVietinProviderId();
		String merchantId = configLoader.getVietinMerchantId();
		String version = configLoader.getVietinVersion();

		data.put(OneFinConstants.VTB_CARDNUMBER, model.getCardNumber());
		data.put(OneFinConstants.VTB_CARDISSUEDATE, model.getCardIssueDate());
		data.put(OneFinConstants.VTB_CARDHOLDERNAME, model.getCardHolderName());
		data.put(OneFinConstants.VTB_AMOUNT, model.getAmount());
		data.put(OneFinConstants.VTB_CURRENCY_CODE, model.getCurrencyCode());
		data.put(OneFinConstants.VTB_PROVIDERCUSTID, model.getProviderCustId());
		data.put(OneFinConstants.VTB_CUSTPHONENO, model.getCustPhoneNo());
		data.put(OneFinConstants.VTB_CUSTIDNO, model.getCustIDNo());
		data.put(OneFinConstants.VTB_CLIENTIP, model.getClientIP());
		data.put(OneFinConstants.VTB_TRANSTIME, model.getTransTime());
		data.put(OneFinConstants.VTB_REQUESTID, model.getRequestId());
		data.put(OneFinConstants.VTB_PROVIDERID, providerId);
		data.put(OneFinConstants.VTB_MERCHANTID, merchantId);
		data.put(OneFinConstants.VTB_CHANNEL, model.getChannel());
		data.put(OneFinConstants.VTB_VERSION, version);
		data.put(OneFinConstants.VTB_LANGUAGE, model.getLanguage());
		data.put(OneFinConstants.VTB_MAC, model.getMac());

		String dataSign = data.get(OneFinConstants.VTB_CARDNUMBER) + data.get(OneFinConstants.VTB_CARDISSUEDATE)
				+ data.get(OneFinConstants.VTB_CARDHOLDERNAME) + data.get(OneFinConstants.VTB_AMOUNT)
				+ data.get(OneFinConstants.VTB_CURRENCY_CODE) + data.get(OneFinConstants.VTB_PROVIDERCUSTID)
				+ data.get(OneFinConstants.VTB_CUSTPHONENO) + data.get(OneFinConstants.VTB_CUSTIDNO)
				+ data.get(OneFinConstants.VTB_CLIENTIP) + data.get(OneFinConstants.VTB_TRANSTIME)
				+ data.get(OneFinConstants.VTB_REQUESTID) + data.get(OneFinConstants.VTB_PROVIDERID)
				+ data.get(OneFinConstants.VTB_MERCHANTID) + data.get(OneFinConstants.VTB_CHANNEL)
				+ data.get(OneFinConstants.VTB_VERSION) + data.get(OneFinConstants.VTB_LANGUAGE)
				+ data.get(OneFinConstants.VTB_MAC);
		LOGGER.info("== Before Sign Data :: " + dataSign);
		String signData = viettinSign(dataSign);
		data.put(OneFinConstants.VTB_SIGNATURE, signData);
		LOGGER.info("== After Sign Data :: " + signData);
		return data;
	}

	private String viettinSign(String input) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		PrivateKey privateKeyItek = encryptUtil.readPrivateKey(onefinPrivateKey);
		String signedData = encryptUtil.sign(input, privateKeyItek);
		return signedData;
	}

	/**
	 * Validate response from VTB
	 * 
	 * @param responseObj
	 * @param language
	 * @return
	 */
	public ResponseEntity<?> validateResponse(VietinBaseMessage baseMessage) {
		// Check response
		String errorMsg = "";
		if (baseMessage == null) {
			LOGGER.error("== Failure response from VIETIN!");
			return new ResponseEntity<>(msgUtil.buildVietinErrorResponse(configLoader.getVietinVersion(), baseMessage),
					HttpStatus.OK);
		}

		try {
			if (!baseMessage.isValidMessage()) {
				LOGGER.error("== Invalid response from VIETIN!");
				return new ResponseEntity<>(
						msgUtil.buildVietinInvalidResponse(configLoader.getVietinVersion(), baseMessage),
						HttpStatus.OK);
			}

			if (!configLoader.getVietinProviderId().equals(baseMessage.getProviderId())) {
				LOGGER.error("== ProviderId not support:" + baseMessage.getProviderId());
				return new ResponseEntity<>(
						msgUtil.buildVietinInvalidProviderId(configLoader.getVietinVersion(), baseMessage),
						HttpStatus.OK);
			}

			if (!configLoader.getVietinMerchantId().equals(baseMessage.getMerchantId())) {
				LOGGER.error("== MerchantId not support:" + baseMessage.getMerchantId());
				return new ResponseEntity<>(
						msgUtil.buildVietinInvalidMerchantId(configLoader.getVietinVersion(), baseMessage),
						HttpStatus.OK);
			}

			// validate signature
			String requestId = baseMessage.getRequestId();
			if (requestId == null) {
				requestId = "";
			}

			if (!verifySignature(requestId + baseMessage.getProviderId() + baseMessage.getMerchantId()
					+ baseMessage.getStatus().getCode(), baseMessage.getSignature())) {
				LOGGER.error("== Verify signature fail");
				return new ResponseEntity<>(msgUtil.buildVietinInvalidSig(configLoader.getVietinVersion(), baseMessage),
						HttpStatus.OK);
			}

			LOGGER.info("== Validation success!");
			return new ResponseEntity<>(
					msgUtil.buildVietinValidateSuccess(configLoader.getVietinVersion(), baseMessage), HttpStatus.OK);

		} catch (Exception e) {
			errorMsg = "== Validate response from VIETIN error! " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error(errorMsg);
			return new ResponseEntity<>(
					msgUtil.buildVietinValidationFunctionFail(configLoader.getVietinVersion(), baseMessage),
					HttpStatus.OK);

		}
	}

	public boolean verifySignature(String data, String signature) throws CertificateException, IOException {
		PublicKey publicKeyVietin = encryptUtil.readPublicKey2(vtbPublicKey);
		return encryptUtil.verifySignature(data, signature, publicKeyVietin);
	}

	public boolean isEmptyStr(String value) {
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isEmptyStr(String... strArr) {
		for (String st : strArr) {
			if (st == null || st.trim().isEmpty()) {
				return true;
			}
		}
		return false;
	}

}
