package com.onefin.ewallet.vietinbank.service;

import com.onefin.ewallet.vietinbank.linkbank.model.VietinConnResponse;

public interface IMessageUtil {

	VietinConnResponse buildVietinConnectorResponse(String connectorCode, Object data, String... args);

}
