package com.onefin.ewalletvtb.common;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onefin.ewalletvtb.controller.VietinController;
import com.onefin.ewalletvtb.model.VietinBaseMessage;

@Service
public class HTTPRequestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinController.class);

	/**
	 * Send POST request
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public Object sendVietinPost(String url, Object obj, String clientId, String clientSecret) throws Exception {
		LOGGER.info("== URL :: " + url);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("x-ibm-client-id", clientId);
		headers.add("x-ibm-client-secret", clientSecret);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(obj, headers);
		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			LOGGER.error("== Error response from Vietin!!!" + url);
			return null;
		}
		try {
			return object2VietinBaseMesseage(resp.getBody());
		} catch (Exception e) {
			LOGGER.error("== Can't parse result from Vietin!!!" + url);
			return null;
		}
	}

	private VietinBaseMessage object2VietinBaseMesseage(String object)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		LOGGER.info("== Response :: " + object);
		VietinBaseMessage result = mapper.readValue(object, VietinBaseMessage.class);
		return result;
	}

}
