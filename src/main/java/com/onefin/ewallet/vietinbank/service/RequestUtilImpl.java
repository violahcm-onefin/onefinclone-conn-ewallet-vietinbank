package com.onefin.ewallet.vietinbank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.utility.resttemplate.RestTemplateHelper;
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

@Service
public class RequestUtilImpl implements IRequestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtilImpl.class);

	private static final String ibmClientId = "x-ibm-client-id";

	private static final String xIbmClientSecret = "x-ibm-client-secret";

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	protected RestTemplateHelper restTemplateHelper;

	@Override
	public VtbLinkBankBaseResponse sendTokenIssue(TokenIssue data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			httpCode = responseEntity.getStatusCode();
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}

	}

	@Override
	public VtbLinkBankBaseResponse sendRegisterOnlinePay(RegisterOnlinePay data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendVerifyPin(VerifyPin data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendTokenRevoke(TokenRevokeReIssue data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendTokenReIssue(TokenRevokeReIssue data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendPaymentByToken(PaymentByToken data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendPaymentByOTP(PaymentByOTP data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendWithdraw(Withdraw data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendTransactionInquiry(TransactionInquiry data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public Map<String, Object> sendProviderInquiry(ProviderInquiry data) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			VtbLinkBankBaseResponse tmp = responseEntity.getBody();
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
	public VtbLinkBankBaseResponse sendTokenIssuePayment(TokenIssuePayment data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

	@Override
	public VtbLinkBankBaseResponse sendRefund(Refund data, HttpStatus httpCode) {
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
			ResponseEntity<VtbLinkBankBaseResponse> responseEntity = restTemplateHelper.post(url,
					MediaType.APPLICATION_JSON_VALUE, headersMap, pathVariables, urlParameters,
					configLoader.getProxyConfig(), data, new ParameterizedTypeReference<VtbLinkBankBaseResponse>() {
					});
			LOGGER.info("== Success receive response from Vietin {}", responseEntity.getBody());
			return responseEntity.getBody();
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!", e);
			if (e.getCause() instanceof HttpHostConnectException) {
				httpCode = HttpStatus.REQUEST_TIMEOUT;
			}
			return null;
		}
	}

}
