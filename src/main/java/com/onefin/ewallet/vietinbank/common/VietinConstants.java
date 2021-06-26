package com.onefin.ewallet.vietinbank.common;

import com.onefin.ewallet.common.base.constants.OneFinConstants;

public class VietinConstants extends OneFinConstants {

	// Vietin parameters
	public static final String VTB_REQUESTID = "requestId";
	public static final String VTB_PROVIDERID = "providerId";
	public static final String VTB_MERCHANTID = "merchantId";
	public static final String VTB_SIGNATURE = "signature";
	public static final String VTB_STATUS = "status";
	public static final String VTB_CODE = "code";

	// Vietin error code
	public static final String VTB_SUCCESS_CODE = "00";
	public static final String VTB_DUPLICATE_REQUESTID_CODE = "07";
	public static final String VTB_PAY_BY_OTP_CODE = "20";

	// Connector error code
	public static final String VTB_CONNECTOR_SUCCESS = "00";
	public static final String VTB_INVALID_SIG = "01";
	public static final String VTB_ERROR_RESPONSE = "02";
	public static final String VTB_INVALID_PROVIDER_ID = "03";
	public static final String VTB_INVALID_MERCHANT_ID = "04";
	public static final String VTB_INVALID_RESPONSE = "05";
	public static final String VTB_VALIDATION_FUNCTION_FAIL = "06";
	public static final String INTERNAL_SERVER_ERROR = "07";
	public static final String USER_NOT_ACCEPT_REGISTER_ONLINEPAY = "09";

	public enum LinkType {
		CARD, ACCOUNT;
	}

}
