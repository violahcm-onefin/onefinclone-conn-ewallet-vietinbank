package com.onefin.ewallet.common;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(HashMapConverter.class);

	@Override
	public String convertToDatabaseColumn(Map<String, Object> customerInfo) {

		String customerInfoJson = null;
		try {
			customerInfoJson = objectMapper.writeValueAsString(customerInfo);
		} catch (final JsonProcessingException e) {
			LOGGER.error("JSON writing error", e);
		}

		return customerInfoJson;
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String customerInfoJSON) {

		Map<String, Object> customerInfo = null;
		try {
			customerInfo = objectMapper.readValue(customerInfoJSON, Map.class);
		} catch (final IOException e) {
			LOGGER.error("JSON reading error", e);
		}

		return customerInfo;
	}

}