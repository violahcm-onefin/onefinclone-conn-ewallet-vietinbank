package com.onefin.ewallet.vietinbank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.utility.resttemplate.RestTemplateHelper;
import com.onefin.ewallet.vietinbank.model.PaymentByOTP;
import com.onefin.ewallet.vietinbank.model.PaymentByOTPResponse;
import com.onefin.ewallet.vietinbank.model.PaymentByToken;
import com.onefin.ewallet.vietinbank.model.PaymentByTokenResponse;
import com.onefin.ewallet.vietinbank.model.ProviderInquiry;
import com.onefin.ewallet.vietinbank.model.ProviderInquiryResponse;
import com.onefin.ewallet.vietinbank.model.Refund;
import com.onefin.ewallet.vietinbank.model.RefundResponse;
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

@Service
public class HTTPRequestUtilImpl implements IHTTPRequestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPRequestUtilImpl.class);

	private static final String ibmClientId = "x-ibm-client-id";

	private static final String xIbmClientSecret = "x-ibm-client-secret";

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	protected RestTemplateHelper restTemplateHelper;

	@Override
	public TokenIssueResponse sendTokenIssue(TokenIssue data) throws Exception {
		String url = configLoader.getTokenIssue();
		LOGGER.info("== Send TokenIssue request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<TokenIssueResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<TokenIssueResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public RegisterOnlinePayResponse sendRegisterOnlinePay(RegisterOnlinePay data) throws Exception {
		String url = configLoader.getRegisterOnlinePay();
		LOGGER.info("== Send RegisterOnlinePay request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<RegisterOnlinePayResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<RegisterOnlinePayResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public VerifyPinResponse sendVerifyPin(VerifyPin data) throws Exception {
		String url = configLoader.getVerifyPin();
		LOGGER.info("== Send VerifyPin request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<VerifyPinResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VerifyPinResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public TokenRevokeResponse sendTokenRevoke(TokenRevokeReIssue data) throws Exception {
		String url = configLoader.getTokenRevoke();
		LOGGER.info("== Send TokenRevoke request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<TokenRevokeResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<TokenRevokeResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public TokenReIssueResponse sendTokenReIssue(TokenRevokeReIssue data) throws Exception {
		String url = configLoader.getTokenReissue();
		LOGGER.info("== Send TokenReIssue request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<TokenReIssueResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<TokenReIssueResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public PaymentByTokenResponse sendPaymentByToken(PaymentByToken data) throws Exception {
		String url = configLoader.getPaymentByToken();
		LOGGER.info("== Send PaymentByToken request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<PaymentByTokenResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<PaymentByTokenResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public PaymentByOTPResponse sendPaymentByOTP(PaymentByOTP data) throws Exception {
		String url = configLoader.getPaymentByOTP();
		LOGGER.info("== Send PaymentByOTP request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<PaymentByOTPResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<PaymentByOTPResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public WithdrawResponse sendWithdraw(Withdraw data) throws Exception {
		String url = configLoader.getWidthdraw();
		LOGGER.info("== Send Withdraw request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<WithdrawResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<WithdrawResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public TransactionInquiryResponse sendTransactionInquiry(TransactionInquiry data) throws Exception {
		String url = configLoader.getTransactionInquiry();
		LOGGER.info("== Send TransactionInquiry request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<TransactionInquiryResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<TransactionInquiryResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public Map<String, Object> sendProviderInquiry(ProviderInquiry data) throws Exception {
		String url = configLoader.getProviderInquiry();
		LOGGER.info("== Send ProviderInquiry request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<ProviderInquiryResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<ProviderInquiryResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			ProviderInquiryResponse tmp = responseEntity.getBody();
			try {
				Map<String, Object> response = new HashMap<String, Object>();
				response.put("signature", tmp.getSignature());
				response.put("providerId", tmp.getProviderId());
				response.put("merchantId", tmp.getMerchantId());
				response.put("requestId", tmp.getRequestId());
				response.put("status", tmp.getStatus());
				response.put("balances", tmp.getBalances().get(0));
				return response;
			} catch (Exception e) {
				LOGGER.error("== Can't parse result from Vietin!!!", e);
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

	@Override
	public TokenIssuePaymentResponse sendTokenIssuePayment(TokenIssuePayment data) throws Exception {
		String url = configLoader.getTokenIssuePayment();
		LOGGER.info("== Send TokenIssuePayment request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<TokenIssuePaymentResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<TokenIssuePaymentResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}
	
	@Override
	public RefundResponse sendRefund(Refund data) throws Exception {
		String url = configLoader.getRefund();
		LOGGER.info("== Send Refund request to Vietin {} - url: {}", data, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(ibmClientId, configLoader.getIbmClientId());
		headers.add(xIbmClientSecret, configLoader.getXIbmClientSecret());
		HashMap<String, String> headersMap = new HashMap<String, String>();
		for (String header : headers.keySet()) {
			headersMap.put(header, headers.getFirst(header));
		}
		HashMap<String, String> urlParameters = new HashMap<>();
		List<String> pathVariables = new ArrayList<String>();
		try {
			ResponseEntity<RefundResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<RefundResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			return null;
		}
	}

}
