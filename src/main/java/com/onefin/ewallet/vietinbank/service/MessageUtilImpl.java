package com.onefin.ewallet.vietinbank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.utility.json.JSONHelper;
import com.onefin.ewallet.vietinbank.linkbank.model.VietinConnResponse;

@Service
public class MessageUtilImpl implements IMessageUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtilImpl.class);

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	@Qualifier("jsonHelper")
	private JSONHelper JsonHelper;

	@Override
	public VietinConnResponse buildVietinConnectorResponse(String code, Object data, String... args) {
		VietinConnResponse response = new VietinConnResponse();
		response.setConnectorCode(code);
		response.setVtbResponse(data);
		response.setVersion(configLoader.getVietinVersion());
		response.setType(args != null ? args[0] : null);
		return response;
	}

}
