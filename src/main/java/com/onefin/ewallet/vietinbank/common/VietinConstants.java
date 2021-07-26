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
	public static final String CONN_VTB_INVALID_SIG = "01"; // VietinBank signature invalid

	public enum LinkType {
		CARD, ACCOUNT;
	}

}
