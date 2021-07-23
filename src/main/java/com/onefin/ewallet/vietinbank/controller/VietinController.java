package com.onefin.ewallet.vietinbank.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onefin.ewallet.common.base.controller.AbstractBaseController;
import com.onefin.ewallet.common.base.errorhandler.RuntimeBadRequestException;
import com.onefin.ewallet.common.base.errorhandler.RuntimeExpectationFailedException;
import com.onefin.ewallet.common.base.errorhandler.RuntimeNullPointerException;
import com.onefin.ewallet.common.domain.bank.vietin.VietinEwalletTransaction;
import com.onefin.ewallet.common.utility.json.JSONHelper;
import com.onefin.ewallet.vietinbank.common.VietinConstants;
import com.onefin.ewallet.vietinbank.common.VietinConstants.LinkType;
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
import com.onefin.ewallet.vietinbank.model.VietinConnResponse;
import com.onefin.ewallet.vietinbank.model.VtbLinkBankBaseResponse;
import com.onefin.ewallet.vietinbank.model.Withdraw;
import com.onefin.ewallet.vietinbank.service.ConfigLoader;
import com.onefin.ewallet.vietinbank.service.ETransRepo;
import com.onefin.ewallet.vietinbank.service.IMessageUtil;
import com.onefin.ewallet.vietinbank.service.IRequestUtil;
import com.onefin.ewallet.vietinbank.service.IVietinService;

@RestController
@RequestMapping("/vietin/ewallet")
public class VietinController extends AbstractBaseController {

	@Autowired
	public IVietinService iVietinService;

	@Autowired
	private IMessageUtil imsgUtil;

	@Autowired
	private IRequestUtil IHTTPRequestUtil;

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	@Qualifier("jsonHelper")
	private JSONHelper JsonHelper;

	@Autowired
	private ETransRepo transRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinController.class);

	@PostMapping("/tokenIssue/type/{type}")
	public ResponseEntity<?> getTokenIssue(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenIssue requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start TokenIssue", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		try {
			TokenIssue requestMap = iVietinService.buildVietinTokenIssuer(requestBody, type.toString());
			responseBaseEntity = IHTTPRequestUtil.sendTokenIssue(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process TokenIssue", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_PENDING);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End TokenIssue", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/verifyPin/type/{type}")
	public ResponseEntity<?> verifyPin(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) VerifyPin requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start VerifyPin", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = transRepository
				.findByRequestIdAndTranStatus(requestBody.getVerifyTransactionId(), VietinConstants.TRANS_PENDING);
		if (vietinTrans == null || !vietinTrans.getTranStatus().equals(VietinConstants.TRANS_PENDING)) {
			throw new RuntimeNullPointerException();
		}
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			VerifyPin requestMap = iVietinService.buildVietinVerifyPin(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendVerifyPin(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process VerifyPin", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_SUCCESS);
				}
				vietinTrans.setBankTransactionId(
						StringUtils.isEmpty(vietinTrans.getBankTransactionId()) ? responseBase.getBankTransactionId()
								: vietinTrans.getBankTransactionId());
				vietinTrans.setToken(
						StringUtils.isEmpty(vietinTrans.getToken()) ? responseBase.getToken() : vietinTrans.getToken());
				iVietinService.update(vietinTrans);
				iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				LOGGER.info("== RequestID {} - End VerifyPin", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/registerOnlinePay/type/{type}")
	public ResponseEntity<?> registerOnlinePay(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) RegisterOnlinePay requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start RegisterOnlinePay", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		breakLabel : try {
			if (!requestBody.isAcceptRegistered()) {
				LOGGER.error("== User not accept register online payment");
				throw new RuntimeBadRequestException();
			}
			RegisterOnlinePay requestMap = iVietinService.buildVietinRegisterOnlinePay(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendRegisterOnlinePay(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process RegisterOnlinePay", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_PENDING);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End RegisterOnlinePay", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/tokenRevoke/type/{type}")
	public ResponseEntity<?> tokenRevoke(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TokenRevoke", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenRevoke(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendTokenRevoke(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process TokenRevoke", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_SUCCESS);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End TokenRevoke", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/tokenReIssue/type/{type}")
	public ResponseEntity<?> tokenReIssue(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TokenReIssue", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenReIssue(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendTokenReIssue(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process TokenReissue", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_SUCCESS);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				vietinTrans.setToken(responseBase.getToken());
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End TokenReIssue", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/paymentByToken/type/{type}")
	public ResponseEntity<?> payment(@PathVariable(required = true) LinkType type,
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
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			PaymentByToken requestMap = iVietinService.buildVietinPaymentByToken(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendPaymentByToken(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process PaymentByToken", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_SUCCESS);
				} else if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_PAY_BY_OTP_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_PENDING);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				vietinTrans.setBankTransactionId(responseBase.getBankTransactionId());
				vietinTrans.setToken(requestBody.getToken());
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End PaymentByToken", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	public ResponseEntity<?> paymentByOTP(LinkType type, PaymentByOTP requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start PaymentByOTP", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			PaymentByOTP requestMap = iVietinService.buildVietinPaymentByOTP(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendPaymentByOTP(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
			if (configLoader.isAlwaysTopupOTP()
					&& responseBase.getStatus().getCode().equals(VietinConstants.VTB_SUCCESS_CODE)
					&& responseEntity.getConnectorCode().equals(VietinConstants.CONNECTOR_SUCCESS)) {
				responseBase.getStatus().setCode(VietinConstants.VTB_PAY_BY_OTP_CODE);
				responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONNECTOR_SUCCESS,
						responseBase, type.toString());
			}
		} catch (Exception e) {
			LOGGER.error("== Fail to process PaymentByOTP", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				// Change response for OTP screen
				vietinTrans.setVietinResult(
						responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), null) : null);
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_PAY_BY_OTP_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_PENDING);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End PaymentByOTP", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/withdraw/type/{type}")
	public ResponseEntity<?> withdraw(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) Withdraw requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start Withdraw", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			Withdraw requestMap = iVietinService.buildVietinWithdraw(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendWithdraw(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process Withdraw", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_SUCCESS);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				vietinTrans.setBankTransactionId(responseBase.getBankTransactionId());
				vietinTrans.setToken(requestBody.getToken());
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End Withdraw", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@PostMapping("/transactionInquiry/type/{type}")
	public ResponseEntity<?> transactionInquiry(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TransactionInquiry requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TransactionInquiry", requestBody.getRequestId());
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		
		try {
			TransactionInquiry requestMap = iVietinService.buildVietinTransactionInquiry(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendTransactionInquiry(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			VietinConnResponse responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
			return new ResponseEntity<>(responseEntity, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== Fail to process TransactionInquiry", e);
			throw new RuntimeNullPointerException();
		} finally {
			try {
				// Set data to transaction
				LOGGER.info("== RequestID {} - End TransactionInquiry", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
				throw new RuntimeExpectationFailedException();
			}
		}

	}

	@PostMapping("/providerInquiry")
	public ResponseEntity<?> providerInquiry(
			@Valid @RequestBody(required = true) ProviderInquiry requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start ProviderInquiry", requestBody.getRequestId());
		Map<String, Object> response = null;
		try {
			ProviderInquiry requestMap = iVietinService.buildVietinProviderInquiry(requestBody);
			response = IHTTPRequestUtil.sendProviderInquiry(requestMap);
			return new ResponseEntity<>(imsgUtil.buildVietinConnectorResponse(VietinConstants.CONNECTOR_SUCCESS, response, null), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("== Fail to process ProviderInquiry", e);
			throw new RuntimeNullPointerException();
		} finally {
			try {
				// Set data to transaction
				LOGGER.info("== RequestID {} - End ProviderInquiry", requestBody.getRequestId());
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
				throw new RuntimeExpectationFailedException();
			}
		}

	}

	@PostMapping("/tokenIssuer-payment/type/{type}")
	public ResponseEntity<?> getTokenIssuerPayment(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) TokenIssuePayment requestBody, HttpServletRequest request)
			throws Exception {
		LOGGER.info("== RequestID {} - Start TokenIssuePayment", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			TokenIssuePayment requestMap = iVietinService.buildVietinTokenIssuerPayment(requestBody, type.toString());

			responseBaseEntity = IHTTPRequestUtil.sendTokenIssuePayment(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process TokenIssuePayment", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_PENDING);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
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
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End TokenIssuePayment", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}
	
	@PostMapping("/refund/type/{type}")
	public ResponseEntity<?> refund(@PathVariable(required = true) LinkType type,
			@Valid @RequestBody(required = true) Refund requestBody, HttpServletRequest request) throws Exception {
		LOGGER.info("== RequestID {} - Start Refund", requestBody.getRequestId());
		VietinEwalletTransaction vietinTrans = new VietinEwalletTransaction();
		ResponseEntity<VtbLinkBankBaseResponse> responseBaseEntity = null;
		VtbLinkBankBaseResponse responseBase = null;
		VietinConnResponse responseEntity = null;
		
		try {
			Refund requestMap = iVietinService.buildVietinRefund(requestBody, type.toString());
			responseBaseEntity = IHTTPRequestUtil.sendRefund(requestMap);
			responseBase = responseBaseEntity.getBody();
			// Validate response from VTB
			responseEntity = iVietinService.validateResponse(responseBase, responseBaseEntity.getStatusCode(), type.toString());
		} catch (Exception e) {
			LOGGER.error("== Fail to process Refund", e);
			responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
					type.toString());
		} finally {
			try {
				// Set data to transaction
				vietinTrans.setConnResult(responseEntity != null ? responseEntity.getConnectorCode() : "");
				vietinTrans
						.setVietinResult(responseBase != null ? Objects.toString(responseBase.getStatus().getCode(), "") : "");
				if (vietinTrans.getConnResult().equals(VietinConstants.CONNECTOR_SUCCESS)
						&& vietinTrans.getVietinResult().equals(VietinConstants.VTB_SUCCESS_CODE)) {
					vietinTrans.setTranStatus(VietinConstants.TRANS_SUCCESS);
				} else {
					vietinTrans.setTranStatus(VietinConstants.TRANS_ERROR);
				}
				vietinTrans.setRequestId(requestBody.getRequestId());
				vietinTrans.setApiOperation(VietinConstants.VTB_REFUND);
				vietinTrans.setLinkType(type.toString());
				vietinTrans.setTransDate(requestBody.getTransTime());
				if (type.toString().equals(VietinConstants.LinkType.CARD.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdCard());
				}
				if (type.toString().equals(VietinConstants.LinkType.ACCOUNT.toString())) {
					vietinTrans.setMerchantId(configLoader.getVietinMerchantIdAccount());
				}
				vietinTrans.setBankTransactionId(responseBase != null ? responseBase.getBankTransactionId() : null);
				vietinTrans.setAmount(new BigDecimal(requestBody.getAmount()));
				vietinTrans.setCurrency(requestBody.getCurrencyCode());
				vietinTrans.setRefundId(requestBody.getRefundTransactionId());
				if (!vietinTrans.getVietinResult().equals(VietinConstants.VTB_DUPLICATE_REQUESTID_CODE)) {
					iVietinService.create(vietinTrans);
					iVietinService.backUpRequestResponse(requestBody.getRequestId(), requestBody, responseBase);
				}
				LOGGER.info("== RequestID {} - End Refund", requestBody.getRequestId());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.error("== Fail store transaction", e);
			}
		}
		throw new RuntimeExpectationFailedException();
	}

	@Override
	protected ResponseEntity<?> getAbout() {
		// TODO Auto-generated method stub
		return new ResponseEntity<>("Connector Ewallet VietinBank", HttpStatus.OK);
	}

}
