package com.onefin.ewallet.common;

public class VietinConstants extends com.onefin.common.OneFinConstants {

	// Vietin parameters
	public static final String VTB_REQUESTID = "requestId";
	public static final String VTB_PROVIDERID = "providerId";
	public static final String VTB_MERCHANTID = "merchantId";
	public static final String VTB_SIGNATURE = "signature";
	public static final String VTB_STATUS = "status";
	public static final String VTB_CODE = "code";

	// Vietin connector error code
	public static final String VTB_CONNECTOR_SUCCESS = "00";
	public static final String VTB_INVALID_SIG = "01";
	public static final String VTB_ERROR_RESPONSE = "02";
	public static final String VTB_INVALID_PROVIDER_ID = "03";
	public static final String VTB_INVALID_MERCHANT_ID = "04";
	public static final String VTB_INVALID_RESPONSE = "05";
	public static final String VTB_VALIDATION_FUNCTION_FAIL = "06";
	public static final String INTERNAL_SERVER_ERROR = "07";
	public static final String CONNECTOR_INVALID_VALIDATION_REQUESTBODY = "08";
	
	// Vietin api operation
	public static final String VIETIN_TOKEN_ISSUER = "TOKEN_ISSUER";
	public static final String VIETIN_VERIFY_PIN = "VERIFY_PIN";
	public static final String VIETIN_REGISTER_ONLINE_PAY = "REGISTER_ONLINE_PAY";
	public static final String VIETIN_PAY_BY_TOKEN = "PAY_BY_TOKEN";
	public static final String VIETIN_WITHDRAW = "WITHDRAW";
	public static final String VIETIN_PAY_BY_OTP = "PAY_BY_OTP";
	public static final String VIETIN_TRANSACTION_INQUIRY = "TRANSACTION_INQUIRY";
	public static final String VIETIN_PROVIDER_INQUIRY = "PROVIDER_INQUIRY";
	public static final String VIETIN_REFUND = "REFUND";
	public static final String VIETIN_TOKEN_REISSUE = "TOKEN_REISSUE";
	public static final String VIETIN_TOKEN_REVOKE = "TOKEN_REVOKE";
	public static final String VIETIN_ACCOUNT_INQUIRY = "ACCOUNT_INQUIRY";
	public static final String VIETIN_TOKEN_ISSUER_PAYMENT = "TOKEN_ISSUER_PAYMENT";
	
	
	

}
