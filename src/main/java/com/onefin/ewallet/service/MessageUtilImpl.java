package com.onefin.ewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.model.VietinConnResponse;

@Service
public class MessageUtilImpl implements IMessageUtil {

	@Autowired
	private ConfigLoader configLoader;

	@Override
	public VietinConnResponse buildVietinConnectorResponse(String connectorCode, Object vtbRes) {
		VietinConnResponse response = new VietinConnResponse();
		response.setConnectorCode(connectorCode);
		response.setVtbResponse(vtbRes);
		response.setVersion(configLoader.getVietinVersion());
		return response;
	}

}
