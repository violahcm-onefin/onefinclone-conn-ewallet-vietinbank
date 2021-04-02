package com.onefin.ewalletvtb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@Configuration
@Service
public class ConfigLoader {

	@Value("${vietin.merchantId}")
	private String vietinMerchantId;

	@Value("${vietin.providerId}")
	private String vietinProviderId;

	@Value("${vietin.version}")
	private String vietinVersion;

	@Value("${vietin.url.tokenIssue}")
	private String tokenIssue;
	
	@Value("${vietin.url.verifyPin}")
	private String verifyPin;
	
	@Value("${vietin.url.registerOnlinePay}")
	private String registerOnlinePay;
	
	@Value("${vietin.url.tokenRevoke}")
	private String tokenRevoke;
	
	@Value("${vietin.url.tokenReissue}")
	private String tokenReissue;
	
	@Value("${vietin.url.paymentByToken}")
	private String paymentByToken;
	
	@Value("${vietin.url.paymentByOTP}")
	private String paymentByOTP;
	
	@Value("${vietin.url.widthdraw}")
	private String widthdraw;
	
	@Value("${vietin.url.transactionInquiry}")
	private String transactionInquiry;
	
	@Value("${vietin.url.providerInquiry}")
	private String providerInquiry;
	
	@Value("${vietin.url.tokenIssuePayment}")
	private String tokenIssuePayment;

	@Value("${vietin.ibmClientId}")
	private String ibmClientId;

	@Value("${vietin.xIbmClientSecret}")
	private String xIbmClientSecret;

}
