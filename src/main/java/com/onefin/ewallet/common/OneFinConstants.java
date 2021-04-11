package com.onefin.ewallet.common;

public class OneFinConstants extends com.onefin.common.OneFinConstants {

	// VIETIN parameters
	public static final String VTB_REQUESTID = "requestId";
	public static final String VTB_PROVIDERID = "providerId";
	public static final String VTB_MERCHANTID = "merchantId";
	public static final String VTB_SIGNATURE = "signature";
	public static final String VTB_STATUS = "status";
	public static final String VTB_CODE = "code";

	// VIETIN connector error code
	public static final String VTB_CONNECTOR_SUCCESS = "00";
	public static final String VTB_INVALID_SIG = "01";
	public static final String VTB_ERROR_RESPONSE = "02";
	public static final String VTB_INVALID_PROVIDER_ID = "03";
	public static final String VTB_INVALID_MERCHANT_ID = "04";
	public static final String VTB_INVALID_RESPONSE = "05";
	public static final String VTB_VALIDATION_FUNCTION_FAIL = "06";
	public static final String INTERNAL_SERVER_ERROR = "07";
	public static final String CONNECTOR_INVALID_VALIDATION_REQUESTBODY = "08";

}
