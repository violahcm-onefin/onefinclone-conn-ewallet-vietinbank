package com.onefin.ewalletvtb.controller;

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

import com.onefin.ewalletvtb.common.HTTPRequestUtil;
import com.onefin.ewalletvtb.common.MessageUtil;
import com.onefin.ewalletvtb.common.VietinUtils;
import com.onefin.ewalletvtb.model.PaymentByOTP;
import com.onefin.ewalletvtb.model.PaymentByToken;
import com.onefin.ewalletvtb.model.RegisterOnlinePay;
import com.onefin.ewalletvtb.model.TokenIssue;
import com.onefin.ewalletvtb.model.TokenRevokeReIssue;
import com.onefin.ewalletvtb.model.VerifyPin;
import com.onefin.ewalletvtb.model.VietinBaseMessage;
import com.onefin.ewalletvtb.service.ConfigLoader;

@Controller
@Configuration
@RequestMapping("/vtbConnector/ewallet")
public class VietinController {

	@Autowired
	public VietinUtils vietinUtils;

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
			Map<String, String> requestMap = vietinUtils.buildVietinTokenIssuer(requestBody);
			LOGGER.info("== RequestID {} - TokenIssue Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenIssue(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process TokenIssue function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyPin")
	public @ResponseBody ResponseEntity<?> verifyPin(@Valid @RequestBody(required = true) VerifyPin requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send VerifyPin Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = vietinUtils.buildVietinVerifyPin(requestBody);
			LOGGER.info("== RequestID {} - VerifyPin Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getVerifyPin(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process VerifyPin function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
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
			Map<String, String> requestMap = vietinUtils.buildVietinRegisterOnlinePay(requestBody);
			LOGGER.info("== RequestID {} - RegisterOnlinePay Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getRegisterOnlinePay(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process RegisterOnlinePay function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenRevoke")
	public @ResponseBody ResponseEntity<?> tokenRevoke(@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TokenRevoke Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = vietinUtils.buildVietinTokenRevoke(requestBody);
			LOGGER.info("== RequestID {} - TokenRevoke Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenRevoke(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process TokenRevoke function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/tokenReIssue")
	public @ResponseBody ResponseEntity<?> tokenReIssue(@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send TokenReissue Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = vietinUtils.buildVietinTokenReIssue(requestBody);
			LOGGER.info("== RequestID {} - TokenReissue Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getTokenRevoke(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process TokenReissue function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/paymentByToken")
	public @ResponseBody ResponseEntity<?> paymentByToken(@Valid @RequestBody(required = true) PaymentByToken requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send PaymentByToken Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = vietinUtils.buildVietinPaymentByToken(requestBody);
			LOGGER.info("== RequestID {} - PaymentByToken Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getPaymentByToken(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process PaymentByToken function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/paymentByOTP")
	public @ResponseBody ResponseEntity<?> paymentByOTP(@Valid @RequestBody(required = true) PaymentByOTP requestBody,
			HttpServletRequest request) throws Exception {
		String errorMsg = "";
		LOGGER.info("== RequestID {} - Send PaymentByOTP Request to VIETIN", requestBody.getRequestId());
		try {
			Map<String, String> requestMap = vietinUtils.buildVietinPaymentByOTP(requestBody);
			LOGGER.info("== RequestID {} - PaymentByOTP Request : " + requestMap, requestBody.getRequestId());

			VietinBaseMessage baseMessage = (VietinBaseMessage) hTTPRequestUtil.sendVietinPost(
					configLoader.getPaymentByOTP(), requestMap, configLoader.getIbmClientId(),
					configLoader.getXIbmClientSecret());

			// Validate response from VTB
			ResponseEntity<?> responseEntity = vietinUtils.validateResponse(baseMessage);
			return responseEntity;
		} catch (Exception e) {
			errorMsg = "Fail to process PaymentByOTP function: " + e.getMessage();
			if (errorMsg.length() > 255) {
				errorMsg = errorMsg.substring(0, 255);
			}
			LOGGER.error("== RequestID {} - " + errorMsg, requestBody.getRequestId());
			return new ResponseEntity<>(msgUtil.buildVietinSystemError(configLoader.getVietinVersion(), null),
					HttpStatus.OK);
		}

	}

}
