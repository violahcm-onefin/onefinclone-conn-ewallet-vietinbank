package com.onefin.ewallet.service;

import com.onefin.ewallet.model.VietinConnResponse;

public interface IMessageUtil {

	VietinConnResponse buildVietinConnectorResponse(String connectorCode, Object vtbRes);


}
