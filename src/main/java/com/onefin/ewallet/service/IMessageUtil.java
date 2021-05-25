package com.onefin.ewallet.service;

import com.onefin.ewallet.common.base.service.IConnMessageService;
import com.onefin.ewallet.model.VietinConnResponse;

public interface IMessageUtil extends IConnMessageService {

	VietinConnResponse buildVietinConnectorResponse(String connectorCode, Object data, String... args);

}
