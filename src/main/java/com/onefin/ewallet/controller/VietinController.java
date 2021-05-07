package com.onefin.ewallet.controller;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onefin.ewallet.common.VietinConstants;
import com.onefin.ewallet.common.VietinConstants.LinkType;
import com.onefin.ewallet.model.EwalletTransaction;
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
import com.onefin.ewallet.model.VietinConnResponse;
import com.onefin.ewallet.model.Withdraw;
import com.onefin.ewallet.model.WithdrawResponse;
import com.onefin.ewallet.service.IHTTPRequestUtil;
import com.onefin.ewallet.service.IMessageUtil;
import com.onefin.ewallet.service.IVietinService;

@Controller
@Configuration
@RequestMapping("/vietin/ewallet")
public class VietinController {

	@Autowired
	public IVietinService iVietinService;

	@Autowired
	private IMessageUtil imsgUtil;

	@Autowired
	private IHTTPRequestUtil IHTTPRequestUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssue/type/{type}")
	public @ResponseBody ResponseEntity<?> getTokenIssue(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenIssue requestBody, HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send TokenIssue Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			TokenIssue requestMap = iVietinService.buildVietinTokenIssuer(requestBody, type.toString());
			LOGGER.info("== RequestID {} - TokenIssue Request : " + requestMap, requestBody.getRequestId());

			TokenIssueResponse response = (TokenIssueResponse) IHTTPRequestUtil.sendTokenIssue(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenIssue function", requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_TOKEN_ISSUER);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyPin/type/{type}")
	public @ResponseBody ResponseEntity<?> verifyPin(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) VerifyPin requestBody, HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send VerifyPin Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			VerifyPin requestMap = iVietinService.buildVietinVerifyPin(requestBody, type.toString());
			LOGGER.info("== RequestID {} - VerifyPin Request : " + requestMap, requestBody.getRequestId());

			VerifyPinResponse response = (VerifyPinResponse) IHTTPRequestUtil.sendVerifyPin(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process VerifyPin function", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_VERIFY_PIN);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/registerOnlinePay/type/{type}")
	public @ResponseBody ResponseEntity<?> registerOnlinePay(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) RegisterOnlinePay requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Send RegisterOnlinePay Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			if (!requestBody.isAcceptRegistered()) {
				LOGGER.error("== User not accept register online payment");
				return new ResponseEntity<>(imsgUtil.buildVietinConnectorResponse(
						VietinConstants.USER_NOT_ACCEPT_REGISTER_ONLINEPAY, null, type.toString()), HttpStatus.OK);
			}
			RegisterOnlinePay requestMap = iVietinService.buildVietinRegisterOnlinePay(requestBody, type.toString());
			LOGGER.info("== RequestID {} - RegisterOnlinePay Request : " + requestMap, requestBody.getRequestId());

			RegisterOnlinePayResponse response = (RegisterOnlinePayResponse) IHTTPRequestUtil
					.sendRegisterOnlinePay(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process RegisterOnlinePay function: {}",
					requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_REGISTER_ONLINE_PAY);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenRevoke/type/{type}")
	public @ResponseBody ResponseEntity<?> tokenRevoke(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TokenRevoke Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenRevoke(requestBody, type.toString());
			LOGGER.info("== RequestID {} - TokenRevoke Request : " + requestMap, requestBody.getRequestId());

			TokenRevokeResponse response = (TokenRevokeResponse) IHTTPRequestUtil.sendTokenRevoke(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenRevoke function: {}", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_TOKEN_REVOKE);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenReIssue/type/{type}")
	public @ResponseBody ResponseEntity<?> tokenReIssue(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TokenReissue Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenReIssue(requestBody, type.toString());
			LOGGER.info("== RequestID {} - TokenReissue Request : " + requestMap, requestBody.getRequestId());

			TokenReIssueResponse response = (TokenReIssueResponse) IHTTPRequestUtil.sendTokenReIssue(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenReissue function: {}", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_TOKEN_REISSUE);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByToken/type/{type}")
	public @ResponseBody ResponseEntity<?> paymentByToken(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) PaymentByToken requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send PaymentByToken Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			PaymentByToken requestMap = iVietinService.buildVietinPaymentByToken(requestBody, type.toString());
			LOGGER.info("== RequestID {} - PaymentByToken Request : " + requestMap, requestBody.getRequestId());

			PaymentByTokenResponse response = (PaymentByTokenResponse) IHTTPRequestUtil.sendPaymentByToken(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process PaymentByToken function: {}", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_PAY_BY_TOKEN);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByOTP/type/{type}")
	public @ResponseBody ResponseEntity<?> paymentByOTP(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) PaymentByOTP requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send PaymentByOTP Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			PaymentByOTP requestMap = iVietinService.buildVietinPaymentByOTP(requestBody, type.toString());
			LOGGER.info("== RequestID {} - PaymentByOTP Request : " + requestMap, requestBody.getRequestId());

			PaymentByOTPResponse response = (PaymentByOTPResponse) IHTTPRequestUtil.sendPaymentByOTP(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process PaymentByOTP function: {}", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_PAY_BY_OTP);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/withdraw/type/{type}")
	public @ResponseBody ResponseEntity<?> withdraw(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) Withdraw requestBody, HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send Withdraw Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			Withdraw requestMap = iVietinService.buildVietinWithdraw(requestBody, type.toString());
			LOGGER.info("== RequestID {} - Withdraw Request : " + requestMap, requestBody.getRequestId());

			WithdrawResponse response = (WithdrawResponse) IHTTPRequestUtil.sendWithdraw(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process Withdraw function: {}", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_WITHDRAW);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/transactionInquiry/type/{type}")
	public @ResponseBody ResponseEntity<?> transactionInquiry(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TransactionInquiry requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TransactionInquiry Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			TransactionInquiry requestMap = iVietinService.buildVietinTransactionInquiry(requestBody, type.toString());
			LOGGER.info("== RequestID {} - TransactionInquiry Request : " + requestMap, requestBody.getRequestId());

			TransactionInquiryResponse response = (TransactionInquiryResponse) IHTTPRequestUtil
					.sendTransactionInquiry(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TransactionInquiry function", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_TRANSACTION_INQUIRY);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/providerInquiry")
	public @ResponseBody ResponseEntity<?> providerInquiry(
			@Valid @RequestBody(required = true) ProviderInquiry requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send ProviderInquiry Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			ProviderInquiry requestMap = iVietinService.buildVietinProviderInquiry(requestBody);
			LOGGER.info("== RequestID {} - ProviderInquiry Request : " + requestMap, requestBody.getRequestId());

			ProviderInquiryResponse response = (ProviderInquiryResponse) IHTTPRequestUtil
					.sendProviderInquiry(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, null);
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process ProviderInquiry function", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, null),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setApiOperation(VietinConstants.VIETIN_PROVIDER_INQUIRY);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssuer-payment/type/{type}")
	public @ResponseBody ResponseEntity<?> getTokenIssuerPayment(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenIssuePayment requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TokenIssuePayment Request to VIETIN", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		try {
			TokenIssuePayment requestMap = iVietinService.buildVietinTokenIssuerPayment(requestBody, type.toString());
			LOGGER.info("== RequestID {} - TokenIssuePayment Request : " + requestMap, requestBody.getRequestId());

			TokenIssuePaymentResponse response = (TokenIssuePaymentResponse) IHTTPRequestUtil
					.sendTokenIssuePayment(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnectorResult(responseEntity.getConnectorCode());
			vietinTrans.setVietinResponse((Map<String, Object>) iVietinService.convertObject2Map(response, Map.class));
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenIssuePayment function", requestBody.getRequestId());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setApiOperation(VietinConstants.VIETIN_TOKEN_ISSUER_PAYMENT);
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setEwalletRequest(
						(Map<String, Object>) iVietinService.convertObject2Map(requestBody, Map.class));
				iVietinService.save(vietinTrans);
			} catch (Exception e) {
				LOGGER.error("Fail store Payment transaction retrieve transaction", e);
			}
		}

	}

}
