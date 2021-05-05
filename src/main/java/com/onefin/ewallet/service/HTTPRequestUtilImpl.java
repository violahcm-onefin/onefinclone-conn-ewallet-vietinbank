package com.onefin.ewallet.service;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.onefin.ewallet.model.TransactionInquiry;
import com.onefin.ewallet.model.TransactionInquiryResponse;
import com.onefin.ewallet.model.VerifyPin;
import com.onefin.ewallet.model.VerifyPinResponse;
import com.onefin.ewallet.model.Withdraw;
import com.onefin.ewallet.model.WithdrawResponse;

@Service
public class HTTPRequestUtilImpl implements IHTTPRequestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPRequestUtilImpl.class);

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	public IVietinService iVietinService;

	@Override
	public TokenIssueResponse sendTokenIssue(TokenIssue data) throws Exception {
		String url = configLoader.getTokenIssue();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), TokenIssueResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public RegisterOnlinePayResponse sendRegisterOnlinePay(RegisterOnlinePay data) throws Exception {
		String url = configLoader.getRegisterOnlinePay();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), RegisterOnlinePayResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public VerifyPinResponse sendVerifyPin(VerifyPin data) throws Exception {
		String url = configLoader.getVerifyPin();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), VerifyPinResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public Map<String, Object> sendTokenRevoke(TokenRevokeReIssue data) throws Exception {
		String url = configLoader.getTokenRevoke();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			LOGGER.info("== Response - " + resp.getBody());
			return (Map<String, Object>) iVietinService.convertString2Map(resp.getBody(), Map.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public TokenReIssueResponse sendTokenReIssue(TokenRevokeReIssue data) throws Exception {
		String url = configLoader.getTokenReissue();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), TokenReIssueResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public PaymentByTokenResponse sendPaymentByToken(PaymentByToken data) throws Exception {
		String url = configLoader.getPaymentByToken();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), PaymentByTokenResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public PaymentByOTPResponse sendPaymentByOTP(PaymentByOTP data) throws Exception {
		String url = configLoader.getPaymentByOTP();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), PaymentByOTPResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public WithdrawResponse sendWithdraw(Withdraw data) throws Exception {
		String url = configLoader.getWidthdraw();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			LOGGER.info("== Response - " + resp.getBody());
			return (WithdrawResponse) iVietinService.convertString2Map(resp.getBody(), WithdrawResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public TransactionInquiryResponse sendTransactionInquiry(TransactionInquiry data) throws Exception {
		String url = configLoader.getTransactionInquiry();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), TransactionInquiryResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public ProviderInquiryResponse sendProviderInquiry(ProviderInquiry data) throws Exception {
		String url = configLoader.getProviderInquiry();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), ProviderInquiryResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

	@Override
	public TokenIssuePaymentResponse sendTokenIssuePayment(TokenIssuePayment data) throws Exception {
		String url = configLoader.getTokenIssuePayment();
		LOGGER.info("== Send request to VIETIN {} ", url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", configLoader.getIbmClientId());
		headers.add("x-ibm-client-secret", configLoader.getXIbmClientSecret());
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(data, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from VIETIN!!!", e);
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("== Response - " + resp.getBody());
			return mapper.readValue(resp.getBody(), TokenIssuePaymentResponse.class);

		} catch (Exception e) {
			LOGGER.error("== Can't parse result from VIETIN!!!", e);
			return null;
		}
	}

}
