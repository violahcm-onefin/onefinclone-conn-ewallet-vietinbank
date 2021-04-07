package com.onefin.ewallet.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onefin.ewallet.common.HTTPRequestUtil;
import com.onefin.ewallet.common.MessageUtil;
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
import com.onefin.ewallet.service.IVietinService;

@Controller
@Configuration
@RequestMapping("/vietin/ewallet")
public class VietinController {

	@Autowired
	public IVietinService iVietinService;

	@Autowired
	private MessageUtil msgUtil;

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	private HTTPRequestUtil hTTPRequestUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssue")
	public @ResponseBody ResponseEntity<?> getTokenIssue(@Valid @RequestBody(required = true) TokenIssue requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TokenIssue Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinTokenIssuer(requestBody);
			LOGGER.info("== RequestID {} - TokenIssue Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenIssue(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process TokenIssue function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyPin")
	public @ResponseBody ResponseEntity<?> verifyPin(@Valid @RequestBody(required = true) VerifyPin requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send VerifyPin Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinVerifyPin(requestBody);
			LOGGER.info("== RequestID {} - VerifyPin Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getVerifyPin(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process VerifyPin function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/registerOnlinePay")
	public @ResponseBody ResponseEntity<?> registerOnlinePay(
			@Valid @RequestBody(required = true) RegisterOnlinePay requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send RegisterOnlinePay Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinRegisterOnlinePay(requestBody);
			LOGGER.info("== RequestID {} - RegisterOnlinePay Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getRegisterOnlinePay(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process RegisterOnlinePay function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenRevoke")
	public @ResponseBody ResponseEntity<?> tokenRevoke(
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TokenRevoke Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinTokenRevoke(requestBody);
			LOGGER.info("== RequestID {} - TokenRevoke Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenRevoke(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process TokenRevoke function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenReIssue")
	public @ResponseBody ResponseEntity<?> tokenReIssue(
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TokenReissue Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinTokenReIssue(requestBody);
			LOGGER.info("== RequestID {} - TokenReissue Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenRevoke(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process TokenReissue function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByToken")
	public @ResponseBody ResponseEntity<?> paymentByToken(
			@Valid @RequestBody(required = true) PaymentByToken requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send PaymentByToken Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinPaymentByToken(requestBody);
			LOGGER.info("== RequestID {} - PaymentByToken Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getPaymentByToken(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process PaymentByToken function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByOTP")
	public @ResponseBody ResponseEntity<?> paymentByOTP(@Valid @RequestBody(required = true) PaymentByOTP requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send PaymentByOTP Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinPaymentByOTP(requestBody);
			LOGGER.info("== RequestID {} - PaymentByOTP Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getPaymentByOTP(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process PaymentByOTP function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/withdraw")
	public @ResponseBody ResponseEntity<?> withdraw(@Valid @RequestBody(required = true) Withdraw requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send Withdraw Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinWithdraw(requestBody);
			LOGGER.info("== RequestID {} - Withdraw Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getWidthdraw(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process Withdraw function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/transactionInquiry")
	public @ResponseBody ResponseEntity<?> transactionInquiry(
			@Valid @RequestBody(required = true) TransactionInquiry requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TransactionInquiry Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinTransactionInquiry(requestBody);
			LOGGER.info("== RequestID {} - TransactionInquiry Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTransactionInquiry(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process TransactionInquiry function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/providerInquiry")
	public @ResponseBody ResponseEntity<?> providerInquiry(
			@Valid @RequestBody(required = true) ProviderInquiry requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send ProviderInquiry Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinProviderInquiry(requestBody);
			LOGGER.info("== RequestID {} - ProviderInquiry Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getProviderInquiry(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process ProviderInquiry function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssuer-payment")
	public @ResponseBody ResponseEntity<?> getTokenIssuerPayment(
			@Valid @RequestBody(required = true) TokenIssuePayment requestBody, HttpServletRequest request)
			throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TokenIssuePayment Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = iVietinService.buildVietinTokenIssuerPayment(requestBody);
			LOGGER.info("== RequestID {} - TokenIssuePayment Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenIssuePayment(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Fail to process TokenIssuePayment function: " + e;
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinInternalServerError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

}
