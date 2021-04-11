package com.onefin.ewallet.controller;

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

import com.onefin.ewallet.common.OneFinConstants;
import com.onefin.ewallet.model.PaymentByOTP;
import com.onefin.ewallet.model.PaymentByOTPResponse;
import com.onefin.ewallet.model.PaymentByToken;
import com.onefin.ewallet.model.PaymentByTokenResponse;
import com.onefin.ewallet.model.ProviderInquiry;
import com.onefin.ewallet.model.ProviderInquiryResponse;
import com.onefin.ewallet.model.RegisterOnlinePay;
import com.onefin.ewallet.model.RegisterOnlinePayResponse;
import com.onefin.ewallet.model.Status;
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

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssue")
	public @ResponseBody ResponseEntity<?> getTokenIssue(@Valid @RequestBody(required = true) TokenIssue requestBody,
			HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send TokenIssue Request to VIETIN", requestBody.getRequestId());
		try {
//			TokenIssue requestMap = iVietinService.buildVietinTokenIssuer(requestBody);
//			LOGGER.info("== RequestID {} - TokenIssue Request : " + requestMap, requestBody.getRequestId());
//
//			TokenIssueResponse response = (TokenIssueResponse) IHTTPRequestUtil.sendTokenIssue(requestMap);

			TokenIssueResponse simulator = new TokenIssueResponse();
			
			simulator.setProviderId("ONEFIN");
			simulator.setMerchantId("ONEFINTEST");
			simulator.setRequestId(requestBody.getRequestId());
			simulator.setSignature("eyJ0b2tlblJlc3VsdCI6eyJyZXN1bHQiOiJTVUNDRVNTIiwicmVzcG9uc2UiOnsiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInRva2VuIjoiOTcwNDAwMzU0MTQ4MDAxOCIsImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJTTUwifSwiZGV2aWNlSWQiOiIwMTIzNDU2Nzg5In0sInBheW1lbnRSZXN1bHQiOnsiYXBpT3BlcmF0aW9uIjoiUFVSQ0hBU0UiLCJtZXJjaGFudElkIjoiT05FRklOQ0UiLCJvcmRlciI6eyJhbW91bnQiOiIxMDAwMCIsImNyZWF0aW9uVGltZSI6IjIwMjEtMDQtMDlUMTQ6Mzk6MzIuOTcxWiIsImN1cnJlbmN5IjoiVk5EIiwiaWQiOiJPUkRFUl8xMjMyOSJ9LCJyZXNwb25zZSI6eyJhY3F1aXJlckNvZGUiOiIwIiwiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInJlc3VsdCI6IlNVQ0NFU1MiLCJzb3VyY2VPZkZ1bmRzIjp7InByb3ZpZGVkIjp7ImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJPVEhFUlMifX0sInR5cGUiOiJDQVJEIn0sInRyYW5zYWN0aW9uIjp7ImFjcXVpcmVyIjp7ImRhdGUiOiIyMDIxLzA0LzA5IDE0OjMyOjA0IiwiaWQiOiI1MTIwMDA0NDciLCJ0cmFuc2FjdGlvbklkIjoiNTEyMDAwNDQ3In0sImFtb3VudCI6IjEwMDAwIiwiY3VycmVuY3kiOiJWTkQiLCJpZCI6IjY1ODI0MDAzIiwidHlwZSI6IlBBWU1FTlQifX19");
			Status status = new Status();
			status.setCode("00");
			status.setMessage("Success");
			simulator.setStatus(status);
			
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(simulator);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenIssue function: {}", requestBody.getRequestId(), e.toString());
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyPin")
	public @ResponseBody ResponseEntity<?> verifyPin(@Valid @RequestBody(required = true) VerifyPin requestBody,
			HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send VerifyPin Request to VIETIN", requestBody.getRequestId());
		try {
//			VerifyPin requestMap = iVietinService.buildVietinVerifyPin(requestBody);
//			LOGGER.info("== RequestID {} - VerifyPin Request : " + requestMap, requestBody.getRequestId());
//
//			VerifyPinResponse response = (VerifyPinResponse) IHTTPRequestUtil.sendVerifyPin(requestMap);
			
			VerifyPinResponse simulator = new VerifyPinResponse();
			
			simulator.setProviderId("ONEFIN");
			simulator.setMerchantId("ONEFINTEST");
			simulator.setRequestId(requestBody.getRequestId());
			simulator.setSignature("eyJ0b2tlblJlc3VsdCI6eyJyZXN1bHQiOiJTVUNDRVNTIiwicmVzcG9uc2UiOnsiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInRva2VuIjoiOTcwNDAwMzU0MTQ4MDAxOCIsImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJTTUwifSwiZGV2aWNlSWQiOiIwMTIzNDU2Nzg5In0sInBheW1lbnRSZXN1bHQiOnsiYXBpT3BlcmF0aW9uIjoiUFVSQ0hBU0UiLCJtZXJjaGFudElkIjoiT05FRklOQ0UiLCJvcmRlciI6eyJhbW91bnQiOiIxMDAwMCIsImNyZWF0aW9uVGltZSI6IjIwMjEtMDQtMDlUMTQ6Mzk6MzIuOTcxWiIsImN1cnJlbmN5IjoiVk5EIiwiaWQiOiJPUkRFUl8xMjMyOSJ9LCJyZXNwb25zZSI6eyJhY3F1aXJlckNvZGUiOiIwIiwiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInJlc3VsdCI6IlNVQ0NFU1MiLCJzb3VyY2VPZkZ1bmRzIjp7InByb3ZpZGVkIjp7ImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJPVEhFUlMifX0sInR5cGUiOiJDQVJEIn0sInRyYW5zYWN0aW9uIjp7ImFjcXVpcmVyIjp7ImRhdGUiOiIyMDIxLzA0LzA5IDE0OjMyOjA0IiwiaWQiOiI1MTIwMDA0NDciLCJ0cmFuc2FjdGlvbklkIjoiNTEyMDAwNDQ3In0sImFtb3VudCI6IjEwMDAwIiwiY3VycmVuY3kiOiJWTkQiLCJpZCI6IjY1ODI0MDAzIiwidHlwZSI6IlBBWU1FTlQifX19");
			Status status = new Status();
			status.setCode("00");
			status.setMessage("Success");
			simulator.setStatus(status);
			
			simulator.setBankTransactionId("TRANS_12329");
			simulator.setToken("9704003541480018");
			simulator.setTokenIssueDate("0904");
			simulator.setTokenExpireDate("0906");
			simulator.setCardMask("970400xxxxxx0018");
			simulator.setDescription("");
			
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(simulator);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process VerifyPin function: {}", requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/registerOnlinePay")
	public @ResponseBody ResponseEntity<?> registerOnlinePay(
			@Valid @RequestBody(required = true) RegisterOnlinePay requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send RegisterOnlinePay Request to VIETIN", requestBody.getRequestId());
		try {
			RegisterOnlinePay requestMap = iVietinService.buildVietinRegisterOnlinePay(requestBody);
			LOGGER.info("== RequestID {} - RegisterOnlinePay Request : " + requestMap, requestBody.getRequestId());

			RegisterOnlinePayResponse response = (RegisterOnlinePayResponse) IHTTPRequestUtil
					.sendRegisterOnlinePay(requestMap);
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(response);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process RegisterOnlinePay function: {}", requestBody.getRequestId(),
					e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenRevoke")
	public @ResponseBody ResponseEntity<?> tokenRevoke(
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TokenRevoke Request to VIETIN", requestBody.getRequestId());
		try {
//			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenRevoke(requestBody);
//			LOGGER.info("== RequestID {} - TokenRevoke Request : " + requestMap, requestBody.getRequestId());
//
//			TokenRevokeResponse response = (TokenRevokeResponse) IHTTPRequestUtil.sendTokenRevoke(requestMap);
			
			TokenRevokeResponse simulator = new TokenRevokeResponse();
			
			simulator.setProviderId("ONEFIN");
			simulator.setMerchantId("ONEFINTEST");
			simulator.setRequestId(requestBody.getRequestId());
			simulator.setSignature("eyJ0b2tlblJlc3VsdCI6eyJyZXN1bHQiOiJTVUNDRVNTIiwicmVzcG9uc2UiOnsiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInRva2VuIjoiOTcwNDAwMzU0MTQ4MDAxOCIsImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJTTUwifSwiZGV2aWNlSWQiOiIwMTIzNDU2Nzg5In0sInBheW1lbnRSZXN1bHQiOnsiYXBpT3BlcmF0aW9uIjoiUFVSQ0hBU0UiLCJtZXJjaGFudElkIjoiT05FRklOQ0UiLCJvcmRlciI6eyJhbW91bnQiOiIxMDAwMCIsImNyZWF0aW9uVGltZSI6IjIwMjEtMDQtMDlUMTQ6Mzk6MzIuOTcxWiIsImN1cnJlbmN5IjoiVk5EIiwiaWQiOiJPUkRFUl8xMjMyOSJ9LCJyZXNwb25zZSI6eyJhY3F1aXJlckNvZGUiOiIwIiwiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInJlc3VsdCI6IlNVQ0NFU1MiLCJzb3VyY2VPZkZ1bmRzIjp7InByb3ZpZGVkIjp7ImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJPVEhFUlMifX0sInR5cGUiOiJDQVJEIn0sInRyYW5zYWN0aW9uIjp7ImFjcXVpcmVyIjp7ImRhdGUiOiIyMDIxLzA0LzA5IDE0OjMyOjA0IiwiaWQiOiI1MTIwMDA0NDciLCJ0cmFuc2FjdGlvbklkIjoiNTEyMDAwNDQ3In0sImFtb3VudCI6IjEwMDAwIiwiY3VycmVuY3kiOiJWTkQiLCJpZCI6IjY1ODI0MDAzIiwidHlwZSI6IlBBWU1FTlQifX19");
			Status status = new Status();
			status.setCode("00");
			status.setMessage("Success");
		
			simulator.setStatus(status);
			
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(simulator);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenRevoke function: {}", requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenReIssue")
	public @ResponseBody ResponseEntity<?> tokenReIssue(
			@Valid @RequestBody(required = true) TokenRevokeReIssue requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TokenReissue Request to VIETIN", requestBody.getRequestId());
		try {
			TokenRevokeReIssue requestMap = iVietinService.buildVietinTokenReIssue(requestBody);
			LOGGER.info("== RequestID {} - TokenReissue Request : " + requestMap, requestBody.getRequestId());

			TokenReIssueResponse response = (TokenReIssueResponse) IHTTPRequestUtil.sendTokenReIssue(requestMap);
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(response);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenReissue function: {}", requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByToken")
	public @ResponseBody ResponseEntity<?> paymentByToken(
			@Valid @RequestBody(required = true) PaymentByToken requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send PaymentByToken Request to VIETIN", requestBody.getRequestId());
		try {
//			PaymentByToken requestMap = iVietinService.buildVietinPaymentByToken(requestBody);
//			LOGGER.info("== RequestID {} - PaymentByToken Request : " + requestMap, requestBody.getRequestId());
//
//			PaymentByTokenResponse response = (PaymentByTokenResponse) IHTTPRequestUtil.sendPaymentByToken(requestMap);
			
			PaymentByTokenResponse simulator = new PaymentByTokenResponse();
			
			simulator.setProviderId("ONEFIN");
			simulator.setMerchantId("ONEFINTEST");
			simulator.setRequestId(requestBody.getRequestId());
			simulator.setSignature("eyJ0b2tlblJlc3VsdCI6eyJyZXN1bHQiOiJTVUNDRVNTIiwicmVzcG9uc2UiOnsiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInRva2VuIjoiOTcwNDAwMzU0MTQ4MDAxOCIsImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJTTUwifSwiZGV2aWNlSWQiOiIwMTIzNDU2Nzg5In0sInBheW1lbnRSZXN1bHQiOnsiYXBpT3BlcmF0aW9uIjoiUFVSQ0hBU0UiLCJtZXJjaGFudElkIjoiT05FRklOQ0UiLCJvcmRlciI6eyJhbW91bnQiOiIxMDAwMCIsImNyZWF0aW9uVGltZSI6IjIwMjEtMDQtMDlUMTQ6Mzk6MzIuOTcxWiIsImN1cnJlbmN5IjoiVk5EIiwiaWQiOiJPUkRFUl8xMjMyOSJ9LCJyZXNwb25zZSI6eyJhY3F1aXJlckNvZGUiOiIwIiwiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInJlc3VsdCI6IlNVQ0NFU1MiLCJzb3VyY2VPZkZ1bmRzIjp7InByb3ZpZGVkIjp7ImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJPVEhFUlMifX0sInR5cGUiOiJDQVJEIn0sInRyYW5zYWN0aW9uIjp7ImFjcXVpcmVyIjp7ImRhdGUiOiIyMDIxLzA0LzA5IDE0OjMyOjA0IiwiaWQiOiI1MTIwMDA0NDciLCJ0cmFuc2FjdGlvbklkIjoiNTEyMDAwNDQ3In0sImFtb3VudCI6IjEwMDAwIiwiY3VycmVuY3kiOiJWTkQiLCJpZCI6IjY1ODI0MDAzIiwidHlwZSI6IlBBWU1FTlQifX19");
			Status status = new Status();
			status.setCode("00");
			status.setMessage("Success");
			
			simulator.setStatus(status);
			
			simulator.setBankTransactionId("TRANS_123");
			simulator.setDescription("Test");
			
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(simulator);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process PaymentByToken function: {}", requestBody.getRequestId(),
					e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/paymentByOTP")
	public @ResponseBody ResponseEntity<?> paymentByOTP(@Valid @RequestBody(required = true) PaymentByOTP requestBody,
			HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send PaymentByOTP Request to VIETIN", requestBody.getRequestId());
		try {
			PaymentByOTP requestMap = iVietinService.buildVietinPaymentByOTP(requestBody);
			LOGGER.info("== RequestID {} - PaymentByOTP Request : " + requestMap, requestBody.getRequestId());

			PaymentByOTPResponse response = (PaymentByOTPResponse) IHTTPRequestUtil.sendPaymentByOTP(requestMap);
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(response);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process PaymentByOTP function: {}", requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/withdraw")
	public @ResponseBody ResponseEntity<?> withdraw(@Valid @RequestBody(required = true) Withdraw requestBody,
			HttpServletRequest request) throws Exception {

		LOGGER.info("== RequestID {} - Send Withdraw Request to VIETIN", requestBody.getRequestId());
		try {
//			Withdraw requestMap = iVietinService.buildVietinWithdraw(requestBody);
//			LOGGER.info("== RequestID {} - Withdraw Request : " + requestMap, requestBody.getRequestId());
//
//			WithdrawResponse response = (WithdrawResponse) IHTTPRequestUtil.sendWithdraw(requestMap);
			
			WithdrawResponse simulator = new WithdrawResponse();
			
			simulator.setProviderId("ONEFIN");
			simulator.setMerchantId("ONEFINTEST");
			simulator.setRequestId(requestBody.getRequestId());
			simulator.setSignature("eyJ0b2tlblJlc3VsdCI6eyJyZXN1bHQiOiJTVUNDRVNTIiwicmVzcG9uc2UiOnsiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInRva2VuIjoiOTcwNDAwMzU0MTQ4MDAxOCIsImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJTTUwifSwiZGV2aWNlSWQiOiIwMTIzNDU2Nzg5In0sInBheW1lbnRSZXN1bHQiOnsiYXBpT3BlcmF0aW9uIjoiUFVSQ0hBU0UiLCJtZXJjaGFudElkIjoiT05FRklOQ0UiLCJvcmRlciI6eyJhbW91bnQiOiIxMDAwMCIsImNyZWF0aW9uVGltZSI6IjIwMjEtMDQtMDlUMTQ6Mzk6MzIuOTcxWiIsImN1cnJlbmN5IjoiVk5EIiwiaWQiOiJPUkRFUl8xMjMyOSJ9LCJyZXNwb25zZSI6eyJhY3F1aXJlckNvZGUiOiIwIiwiZ2F0ZXdheUNvZGUiOiJTVUNDRVNTIiwibWVzc2FnZSI6IlRyYW5zYWN0aW9uIGlzIHN1Y2Nlc3NmdWwuIn0sInJlc3VsdCI6IlNVQ0NFU1MiLCJzb3VyY2VPZkZ1bmRzIjp7InByb3ZpZGVkIjp7ImNhcmQiOnsiYnJhbmQiOiJTTUwiLCJuYW1lT25DYXJkIjoiTkdVWUVOIFZBTiBBIiwiaXNzdWVEYXRlIjoiMDMwNyIsIm51bWJlciI6Ijk3MDQwMHh4eHh4eDAwMTgiLCJzY2hlbWUiOiJPVEhFUlMifX0sInR5cGUiOiJDQVJEIn0sInRyYW5zYWN0aW9uIjp7ImFjcXVpcmVyIjp7ImRhdGUiOiIyMDIxLzA0LzA5IDE0OjMyOjA0IiwiaWQiOiI1MTIwMDA0NDciLCJ0cmFuc2FjdGlvbklkIjoiNTEyMDAwNDQ3In0sImFtb3VudCI6IjEwMDAwIiwiY3VycmVuY3kiOiJWTkQiLCJpZCI6IjY1ODI0MDAzIiwidHlwZSI6IlBBWU1FTlQifX19");
			Status status = new Status();
			status.setCode("00");
			status.setMessage("Success");
			
			simulator.setStatus(status);
			
			simulator.setBankTransactionId("TRANS_123");
			simulator.setDescription("Test");
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(simulator);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process Withdraw function: {}", requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/transactionInquiry")
	public @ResponseBody ResponseEntity<?> transactionInquiry(
			@Valid @RequestBody(required = true) TransactionInquiry requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TransactionInquiry Request to VIETIN", requestBody.getRequestId());
		try {
			TransactionInquiry requestMap = iVietinService.buildVietinTransactionInquiry(requestBody);
			LOGGER.info("== RequestID {} - TransactionInquiry Request : " + requestMap, requestBody.getRequestId());

			TransactionInquiryResponse response = (TransactionInquiryResponse) IHTTPRequestUtil
					.sendTransactionInquiry(requestMap);
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(response);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TransactionInquiry function: {}",
					requestBody.getRequestId(), e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/providerInquiry")
	public @ResponseBody ResponseEntity<?> providerInquiry(
			@Valid @RequestBody(required = true) ProviderInquiry requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send ProviderInquiry Request to VIETIN", requestBody.getRequestId());
		try {
			ProviderInquiry requestMap = iVietinService.buildVietinProviderInquiry(requestBody);
			LOGGER.info("== RequestID {} - ProviderInquiry Request : " + requestMap, requestBody.getRequestId());

			ProviderInquiryResponse response = (ProviderInquiryResponse) IHTTPRequestUtil
					.sendProviderInquiry(requestMap);
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(response);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process ProviderInquiry function: {}", requestBody.getRequestId(),
					e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/tokenIssuer-payment")
	public @ResponseBody ResponseEntity<?> getTokenIssuerPayment(
			@Valid @RequestBody(required = true) TokenIssuePayment requestBody, HttpServletRequest request)
			throws Exception {

		LOGGER.info("== RequestID {} - Send TokenIssuePayment Request to VIETIN", requestBody.getRequestId());
		try {
			TokenIssuePayment requestMap = iVietinService.buildVietinTokenIssuerPayment(requestBody);
			LOGGER.info("== RequestID {} - TokenIssuePayment Request : " + requestMap, requestBody.getRequestId());

			TokenIssuePaymentResponse response = (TokenIssuePaymentResponse) IHTTPRequestUtil
					.sendTokenIssuePayment(requestMap);
			// Validate response from VTB
			ResponseEntity<?> responseEntity = iVietinService.validateResponse(response);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("== RequestID {} - Fail to process TokenIssuePayment function: {}", requestBody.getRequestId(),
					e);
			return new ResponseEntity<>(
					imsgUtil.buildVietinConnectorResponse(OneFinConstants.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
		}

	}

}
