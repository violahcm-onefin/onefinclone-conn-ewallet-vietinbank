package com.onefin.ewallet.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onefin.ewallet.common.VietinConstants;
import com.onefin.ewallet.common.VietinConstants.LinkType;
import com.onefin.ewallet.common.base.controller.AbstractBaseController;
import com.onefin.ewallet.common.utility.json.JSONHelper;
import com.onefin.ewallet.model.EwalletTransaction;
import com.onefin.ewallet.model.PaymentByOTP;
import com.onefin.ewallet.model.PaymentByOTPResponse;
import com.onefin.ewallet.model.PaymentByToken;
import com.onefin.ewallet.model.PaymentByTokenResponse;
import com.onefin.ewallet.model.ProviderInquiry;
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
import com.onefin.ewallet.service.ConfigLoader;
import com.onefin.ewallet.service.EwalletTransactionRepository;
import com.onefin.ewallet.service.IHTTPRequestUtil;
import com.onefin.ewallet.service.IMessageUtil;
import com.onefin.ewallet.service.IVietinService;

@Controller
@Configuration
@RequestMapping("/vietin/ewallet")
public class VietinController extends AbstractBaseController {

	@Autowired
	public IVietinService iVietinService;

	@Autowired
	private IMessageUtil imsgUtil;

	@Autowired
	private IHTTPRequestUtil IHTTPRequestUtil;

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	@Qualifier("jsonHelper")
	private JSONHelper JsonHelper;

	@Autowired
	private EwalletTransactionRepository transRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssue/type/{type}")
	public @ResponseBody ResponseEntity<?> getTokenIssue(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenIssue requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start TokenIssue", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		TokenIssueResponse response = null;
		try {
			TokenIssue requestMap = iVietinService.buildVietinTokenIssuer(requestBody, type.toString());

			response = (TokenIssueResponse) IHTTPRequestUtil.sendTokenIssue(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_PENDING);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process TokenIssue", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_TOKEN_ISSUER);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End TokenIssue", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyPin/type/{type}")
	public @ResponseBody ResponseEntity<?> verifyPin(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) VerifyPin requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start VerifyPin", requestBody.getRequestId());
		EwalletTransaction vietinTrans = transRepository
				.findByRequestIdAndTranStatus(requestBody.getVerifyTransactionId(), VietinConstants.VTB_TRANS_PENDING);
		if (vietinTrans == null || !vietinTrans.getTranStatus().equals(VietinConstants.VTB_TRANS_PENDING)) {
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		}
		VerifyPinResponse response = null;
		try {
			VerifyPin requestMap = iVietinService.buildVietinVerifyPin(requestBody, type.toString());

			response = (VerifyPinResponse) IHTTPRequestUtil.sendVerifyPin(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_SUCCESS);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== Fail to process VerifyPin", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setBankTransactionId(
						StringUtils.isEmpty(vietinTrans.getBankTransactionId()) ? response.getBankTransactionId()
								: vietinTrans.getBankTransactionId());
				vietinTrans.setToken(
						StringUtils.isEmpty(vietinTrans.getToken()) ? response.getToken() : vietinTrans.getToken());
				vietinTrans.setTokenIssueDate(
						StringUtils.isEmpty(vietinTrans.getTokenIssueDate()) ? response.getTokenIssueDate()
								: vietinTrans.getTokenIssueDate());
				iVietinService.update(vietinTrans);
				LOGGER.info("== RequestID {} - End VerifyPin", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/registerOnlinePay/type/{type}")
	public @ResponseBody ResponseEntity<?> registerOnlinePay(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) RegisterOnlinePay requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start RegisterOnlinePay", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		RegisterOnlinePayResponse response = null;
		try {
			if (!requestBody.isAcceptRegistered()) {
				LOGGER.error("== User not accept register online payment");
				return new ResponseEntity<>(imsgUtil.buildVietinConnectorResponse(
						VietinConstants.USER_NOT_ACCEPT_REGISTER_ONLINEPAY, null, type.toString()), HttpStatus.OK);
			}
			RegisterOnlinePay requestMap = iVietinService.buildVietinRegisterOnlinePay(requestBody, type.toString());

			response = (RegisterOnlinePayResponse) IHTTPRequestUtil.sendRegisterOnlinePay(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_PENDING);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process RegisterOnlinePay", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_REGISTER_ONLINE_PAYMENT);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End RegisterOnlinePay", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenRevoke/type/{type}")
	public @ResponseBody ResponseEntity<?> tokenRevoke(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TokenRevoke", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		TokenRevokeResponse response = null;
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenRevoke(requestBody, type.toString());

			response = (TokenRevokeResponse) IHTTPRequestUtil.sendTokenRevoke(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_SUCCESS);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process TokenRevoke", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_TOKEN_REVOKE);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				vietinTrans.setToken(requestBody.getToken());
				vietinTrans.setTokenIssueDate(requestBody.getTokenIssueDate());
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End TokenRevoke", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenReIssue/type/{type}")
	public @ResponseBody ResponseEntity<?> tokenReIssue(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TokenReIssue", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		TokenReIssueResponse response = null;
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenReIssue(requestBody, type.toString());

			response = (TokenReIssueResponse) IHTTPRequestUtil.sendTokenReIssue(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_SUCCESS);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process TokenReissue", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_TOKEN_REISSUE);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				vietinTrans.setToken(response.getToken());
				vietinTrans.setTokenIssueDate(response.getTokenIssueDate());
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End TokenReIssue", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByToken/type/{type}")
	public @ResponseBody ResponseEntity<?> payment(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) Map<String, Object> requestBody, HttpServletRequest request)
			throws Exception {
		ResponseEntity<?> response = null;
		if (configLoader.isAlwaysTopupOTP()) {
			PaymentByOTP data = (PaymentByOTP) JsonHelper.convertObject2Map(requestBody, PaymentByOTP.class);
			response = paymentByOTP(type, data, request);
		} else {
			PaymentByToken data = (PaymentByToken) JsonHelper.convertObject2Map(requestBody, PaymentByToken.class);
			response = paymentByToken(type, data, request);
		}
		return response;
	}

	public ResponseEntity<?> paymentByToken(LinkType type, PaymentByToken requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start PaymentByToken", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		PaymentByTokenResponse response = null;
		try {
			PaymentByToken requestMap = iVietinService.buildVietinPaymentByToken(requestBody, type.toString());

			response = (PaymentByTokenResponse) IHTTPRequestUtil.sendPaymentByToken(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_SUCCESS);
			} else if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_PAY_BY_OTP_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_PENDING);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process PaymentByToken", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_TOPUP_TOKEN);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setCurrency(requestBody.getCurrencyCode());
				vietinTrans.setAmount(new BigDecimal(requestBody.getAmount()));
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				vietinTrans.setBankTransactionId(response.getBankTransactionId());
				vietinTrans.setToken(requestBody.getToken());
				vietinTrans.setTokenIssueDate(requestBody.getTokenIssueDate());
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End PaymentByToken", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	public ResponseEntity<?> paymentByOTP(LinkType type, PaymentByOTP requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start PaymentByOTP", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		PaymentByOTPResponse response = null;
		Map<String, Object> tmpRes = new HashMap();
		try {
			PaymentByOTP requestMap = iVietinService.buildVietinPaymentByOTP(requestBody, type.toString());

			response = (PaymentByOTPResponse) IHTTPRequestUtil.sendPaymentByOTP(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			// Change response for OTP screen
			if (configLoader.isAlwaysTopupOTP()
					&& response.getStatus().getCode().equals(VietinConstants.VTB_SUCCESS_CODE)
					&& responseEntity.getConnectorCode().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)) {
				response.getStatus().setCode(VietinConstants.VTB_PAY_BY_OTP_CODE);
				responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.VTB_CONNECTOR_SUCCESS, response,
						type.toString());
			}
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_PAY_BY_OTP_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_PENDING);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process PaymentByOTP", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_TOPUP_TOKEN_OTP);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setCurrency(requestBody.getCurrencyCode());
				vietinTrans.setAmount(new BigDecimal(requestBody.getAmount()));
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				vietinTrans.setToken(requestBody.getToken());
				vietinTrans.setTokenIssueDate(requestBody.getTokenIssueDate());
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End PaymentByOTP", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/withdraw/type/{type}")
	public @ResponseBody ResponseEntity<?> withdraw(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) Withdraw requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start Withdraw", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		WithdrawResponse response = null;
		try {
			Withdraw requestMap = iVietinService.buildVietinWithdraw(requestBody, type.toString());

			response = (WithdrawResponse) IHTTPRequestUtil.sendWithdraw(requestMap);

			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_SUCCESS);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process Withdraw", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_WITHDRAW);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setCurrency(requestBody.getCurrencyCode());
				vietinTrans.setAmount(new BigDecimal(requestBody.getAmount()));
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				vietinTrans.setBankTransactionId(response.getBankTransactionId());
				vietinTrans.setToken(requestBody.getToken());
				vietinTrans.setTokenIssueDate(requestBody.getTokenIssueDate());
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End Withdraw", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/transactionInquiry/type/{type}")
	public @ResponseBody ResponseEntity<?> transactionInquiry(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TransactionInquiry requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TransactionInquiry", requestBody.getRequestId());
		TransactionInquiryResponse response = null;
		try {
			TransactionInquiry requestMap = iVietinService.buildVietinTransactionInquiry(requestBody, type.toString());

			response = (TransactionInquiryResponse) IHTTPRequestUtil.sendTransactionInquiry(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== Fail to process TransactionInquiry", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				LOGGER.info("== RequestID {} - End TransactionInquiry", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/providerInquiry")
	public @ResponseBody ResponseEntity<?> providerInquiry(
			@Valid @RequestBody(required = true) ProviderInquiry requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start ProviderInquiry", requestBody.getRequestId());
		Map<String, Object> response = null;
		try {
			ProviderInquiry requestMap = iVietinService.buildVietinProviderInquiry(requestBody);

			response = IHTTPRequestUtil.sendProviderInquiry(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, null);
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== Fail to process ProviderInquiry", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, null),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				LOGGER.info("== RequestID {} - End ProviderInquiry", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssuer-payment/type/{type}")
	public @ResponseBody ResponseEntity<?> getTokenIssuerPayment(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenIssuePayment requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TokenIssuePayment", requestBody.getRequestId());
		EwalletTransaction vietinTrans = new EwalletTransaction();
		TokenIssuePaymentResponse response = null;
		try {
			TokenIssuePayment requestMap = iVietinService.buildVietinTokenIssuerPayment(requestBody, type.toString());

			response = (TokenIssuePaymentResponse) IHTTPRequestUtil.sendTokenIssuePayment(requestMap);
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(response, type.toString());
			vietinTrans.setConnResult(responseEntity.getConnectorCode());
			vietinTrans
					.setVietinResult(response != null ? Objects.toString(response.getStatus().getCode(), null) : null);
			if (vietinTrans.getConnResult().equals(VietinConstants.VTB_CONNECTOR_SUCCESS)
					&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_PENDING);
			} else {
				vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			}
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			vietinTrans.setTranStatus(VietinConstants.VTB_TRANS_ERROR);
			LOGGER.error("== Fail to process TokenIssuePayment", e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(VietinConstants.INTERNAL_SERVER_ERROR, null, type.toString()),
					HttpStatus.OK);
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_TOKEN_ISSUER_TOPUP);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setCurrency(requestBody.getCurrencyCode());
				vietinTrans.setAmount(new BigDecimal(requestBody.getAmount()));
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				iVietinService.save(vietinTrans);
				LOGGER.info("== RequestID {} - End TokenIssuePayment", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}

	}

	@Override
	protected @ResponseBody ResponseEntity<?> getAbout() {
		// TODO Auto-generated method stub
		return new ResponseEntity<>("Connector Ewallet VietinBank", HttpStatus.OK);
	}

}
