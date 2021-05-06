package com.onefin.ewallet.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.VietinConstants;
import com.onefin.ewallet.model.VietinConnResponse;

@Service
public class MessageUtilImpl implements IMessageUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtilImpl.class);

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	public IVietinService iVietinService;

	@Override
	public VietinConnResponse buildVietinConnectorResponse(String connectorCode, Object vtbRes, String type) {
		Map<String, Object> res = new HashMap();
		if (vtbRes != null) {
			try {
				res = (Map<String, Object>) iVietinService.convertObject2Map(vtbRes, Map.class);
				res.remove(VietinConstants.VTB_SIGNATURE);
			} catch (Exception e) {
				LOGGER.error("Fail to parse vietin response to Map");
			}

		}
		VietinConnResponse response = new VietinConnResponse();
		response.setConnectorCode(connectorCode);
		response.setVtbResponse(res);
		response.setVersion(configLoader.getVietinVersion());
		response.setType(type);
		return response;
	}

}
