package com.onefin.ewallet.vietinbank.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.utility.resttemplate.RestProxy;

import lombok.Data;

@Data
@Configuration
@Service
public class ConfigLoader implements InitializingBean {

	@Value("${vietin.merchantIdAccount}")
	private String vietinMerchantIdAccount;

	@Value("${vietin.merchantIdCard}")
	private String vietinMerchantIdCard;

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
	
	@Value("${vietin.url.refund}")
	private String refund;

	@Value("${vietin.ibmClientId}")
	private String ibmClientId;

	@Value("${vietin.xIbmClientSecret}")
	private String xIbmClientSecret;

	@Value("${vietin.onefinPrivateKey}")
	private String onefinPrivateKey;

	@Value("${vietin.vtbPublicKey}")
	private String vtbPublicKey;

	@Value("${proxy.active}")
	private boolean proxActive;

	@Value("${proxy.host}")
	private String proxHost;

	@Value("${proxy.port}")
	private int proxPort;

	@Value("${proxy.activeAuth}")
	private boolean proxActiveAuth;

	@Value("${proxy.userName}")
	private String proxUserName;

	@Value("${proxy.password}")
	private String proxPassword;
	
	@Value("${vietin.alwaysTopupOTP}")
	private boolean alwaysTopupOTP;
	
	@Value("${backup.api.uri}")
	private String backupUri;
	
	@Value("${vietin.activeAccount}")
	private boolean vietinActiveAccount;

	private RestProxy proxyConfig = new RestProxy();

	@Override
	public void afterPropertiesSet() throws Exception {
		setProxy();
	}

	private void setProxy() {
		proxyConfig.setActive(proxActive);
		proxyConfig.setHost(proxHost);
		proxyConfig.setPort(proxPort);
		proxyConfig.setAuth(proxActiveAuth);
		proxyConfig.setUserName(proxUserName);
		proxyConfig.setPassword(proxPassword);
	}

}
