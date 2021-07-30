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

	@Value("${vietin.linkbank.merchantIdAccount}")
	private String vietinMerchantIdAccount;

	@Value("${vietin.linkbank.merchantIdCard}")
	private String vietinMerchantIdCard;

	@Value("${vietin.linkbank.providerId}")
	private String vietinProviderId;

	@Value("${vietin.linkbank.version}")
	private String vietinVersion;

	@Value("${vietin.linkbank.url.tokenIssue}")
	private String tokenIssue;

	@Value("${vietin.linkbank.url.verifyPin}")
	private String verifyPin;

	@Value("${vietin.linkbank.url.registerOnlinePay}")
	private String registerOnlinePay;

	@Value("${vietin.linkbank.url.tokenRevoke}")
	private String tokenRevoke;

	@Value("${vietin.linkbank.url.tokenReissue}")
	private String tokenReissue;

	@Value("${vietin.linkbank.url.paymentByToken}")
	private String paymentByToken;

	@Value("${vietin.linkbank.url.paymentByOTP}")
	private String paymentByOTP;

	@Value("${vietin.linkbank.url.widthdraw}")
	private String widthdraw;

	@Value("${vietin.linkbank.url.transactionInquiry}")
	private String transactionInquiry;

	@Value("${vietin.linkbank.url.providerInquiry}")
	private String providerInquiry;

	@Value("${vietin.linkbank.url.tokenIssuePayment}")
	private String tokenIssuePayment;
	
	@Value("${vietin.linkbank.url.refund}")
	private String refund;

	@Value("${vietin.linkbank.ibmClientId}")
	private String ibmClientId;

	@Value("${vietin.linkbank.xIbmClientSecret}")
	private String xIbmClientSecret;

	@Value("${vietin.linkbank.onefinPrivateKey}")
	private String onefinPrivateKey;

	@Value("${vietin.linkbank.vtbPublicKey}")
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
	
	@Value("${vietin.linkbank.alwaysTopupOTP}")
	private boolean alwaysTopupOTP;
	
	@Value("${backup.api.uriVietinLinkBank}")
	private String backupVietinLinkBank;
	
	@Value("${vietin.linkbank.activeAccount}")
	private boolean vietinActiveAccount;
	
	@Value("${vietin.linkbank.activeCard}")
	private boolean vietinActiveCard;

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
