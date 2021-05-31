package com.onefin.ewallet.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.VietinConstants;
import com.onefin.ewallet.common.base.service.BaseService;
import com.onefin.ewallet.common.domain.vietin.VietinEwalletTransaction;
import com.onefin.ewallet.common.utility.json.JSONHelper;
import com.onefin.ewallet.model.PaymentByOTP;
import com.onefin.ewallet.model.PaymentByToken;
import com.onefin.ewallet.model.ProviderInquiry;
import com.onefin.ewallet.model.RegisterOnlinePay;
import com.onefin.ewallet.model.TokenIssue;
import com.onefin.ewallet.model.TokenIssuePayment;
import com.onefin.ewallet.model.TokenRevokeReIssue;
import com.onefin.ewallet.model.TransactionInquiry;
import com.onefin.ewallet.model.VerifyPin;
import com.onefin.ewallet.model.VietinConnResponse;
import com.onefin.ewallet.model.Withdraw;

@Service
public class VietinServiceImpl extends BaseService implements IVietinService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinServiceImpl.class);

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	private IMessageUtil iMessageUtil;

	@Autowired
	private EncryptUtil encryptUtil;

	@Autowired
	private EwalletTransactionRepository transRepository;

	@Autowired
	@Qualifier("jsonHelper")
	private JSONHelper JsonHelper;

	@Override
	public TokenIssue buildVietinTokenIssuer(TokenIssue data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", data.getCardNumber(), data.getCardIssueDate(),
				data.getCardHolderName(), data.getProviderCustId(), data.getCustPhoneNo(), data.getCustIDNo(),
				data.getClientIP(), data.getTransTime(), data.getRequestId(), data.getProviderId(),
				data.getMerchantId(), data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public VerifyPin buildVietinVerifyPin(VerifyPin data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s", data.getOtp(), data.getVerifyTransactionId(),
				data.getVerifyBy(), data.getTransTime(), data.getClientIP(), data.getRequestId(), data.getProviderId(),
				data.getMerchantId(), data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public RegisterOnlinePay buildVietinRegisterOnlinePay(RegisterOnlinePay data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", data.getCardNumber(),
				data.getCardIssueDate(), data.getCardHolderName(), data.getProviderCustId(), data.getCustIDNo(),
				data.getCustIDIssueDate(), data.getCustIDIssueBy(), data.getCustPhoneNo(), data.getCustGender(),
				data.getCustBirthday(), data.getClientIP(), data.getTransTime(), data.getProviderId(),
				data.getMerchantId(), data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public TokenRevokeReIssue buildVietinTokenRevoke(TokenRevokeReIssue data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s", data.getToken(), data.getTokenIssueDate(),
				data.getTransTime(), data.getClientIP(), data.getProviderId(), data.getMerchantId(), data.getChannel(),
				data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public TokenRevokeReIssue buildVietinTokenReIssue(TokenRevokeReIssue data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s", data.getToken(), data.getTokenIssueDate(),
				data.getTransTime(), data.getClientIP(), data.getProviderId(), data.getMerchantId(), data.getChannel(),
				data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public PaymentByToken buildVietinPaymentByToken(PaymentByToken data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", data.getToken(), data.getTokenIssueDate(),
				data.getAmount(), data.getCurrencyCode(), data.getTransTime(), data.getClientIP(), data.getPayMethod(),
				data.getGoodsType(), data.getBillNo(), data.getRemark(), data.getProviderId(), data.getMerchantId(),
				data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public PaymentByOTP buildVietinPaymentByOTP(PaymentByOTP data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", data.getToken(), data.getTokenIssueDate(),
				data.getAmount(), data.getCurrencyCode(), data.getTransTime(), data.getClientIP(), data.getPayMethod(),
				data.getGoodsType(), data.getBillNo(), data.getRemark(), data.getProviderId(), data.getMerchantId(),
				data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public Withdraw buildVietinWithdraw(Withdraw data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", data.getToken(), data.getTokenIssueDate(),
				data.getAmount(), data.getCurrencyCode(), data.getTransTime(), data.getClientIP(), data.getBenName(),
				data.getBenAcctNo(), data.getBenIDNo(), data.getBenAddInfo(), data.getRemark(), data.getProviderId(),
				data.getMerchantId(), data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public TransactionInquiry buildVietinTransactionInquiry(TransactionInquiry data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s", data.getQueryTransactionId(), data.getQueryType(),
				data.getProviderId(), data.getMerchantId(), data.getChannel(), data.getVersion(), data.getLanguage(),
				data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public ProviderInquiry buildVietinProviderInquiry(ProviderInquiry data)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		data.setMerchantId(configLoader.getVietinMerchantIdCard());
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s", data.getProviderId(), data.getMerchantId(), data.getChannel(),
				data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	@Override
	public TokenIssuePayment buildVietinTokenIssuerPayment(TokenIssuePayment data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", data.getCardNumber(),
				data.getCardIssueDate(), data.getCardHolderName(), data.getAmount(), data.getCurrencyCode(),
				data.getProviderCustId(), data.getCustPhoneNo(), data.getCustIDNo(), data.getClientIP(),
				data.getTransTime(), data.getRequestId(), data.getProviderId(), data.getMerchantId(), data.getChannel(),
				data.getVersion(), data.getLanguage(), data.getMac());

		LOGGER.info("== Before Sign Data - " + dataSign);
		String signData = viettinSign(dataSign);
		data.setSignature(signData);
		LOGGER.info("== After Sign Data - " + signData);
		return data;
	}

	private String viettinSign(String input) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		PrivateKey privateKeyOneFin = encryptUtil.readPrivateKey(configLoader.getOnefinPrivateKey());
		String signedData = encryptUtil.sign(input, privateKeyOneFin);
		return signedData;
	}

	/**
	 * Validate response from VTB
	 * 
	 * @param responseObj
	 * @param language
	 * @return
	 */
	@Override
	public VietinConnResponse validateResponse(Object data, String type) {
		// Check response
		if (data == null) {
			LOGGER.error("== Failure response from Vietin!!!");
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_ERROR_RESPONSE, data, type);
		}

		try {
			Map<String, Object> mapData = (Map<String, Object>) JsonHelper.convertObject2Map(data, HashMap.class);
			String requestId = Objects.toString(mapData.get(VietinConstants.VTB_REQUESTID), "");
			String providerId = Objects.toString(mapData.get(VietinConstants.VTB_PROVIDERID), "");
			String merchantId = Objects.toString(mapData.get(VietinConstants.VTB_MERCHANTID), "");
			String signature = Objects.toString(mapData.get(VietinConstants.VTB_SIGNATURE), "");
			Map<String, Object> status = (Map<String, Object>) mapData.get(VietinConstants.VTB_STATUS);
			String code = null;
			if (status != null) {
				code = Objects.toString(status.get(VietinConstants.VTB_CODE), "");
			}
			if (!isValidMessage(requestId, providerId, merchantId, signature)) {
				LOGGER.error("== Invalid response from Vietin!!!");
				return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_INVALID_RESPONSE, data, type);
			}

			if (!configLoader.getVietinProviderId().equals(providerId)) {
				LOGGER.error("== ProviderId not support: {}", providerId);
				return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_INVALID_PROVIDER_ID, data, type);
			}

			if (!configLoader.getVietinMerchantIdCard()
					.equals(Objects.toString(mapData.get(VietinConstants.VTB_MERCHANTID), ""))
					&& !configLoader.getVietinMerchantIdAccount()
							.equals(Objects.toString(mapData.get(VietinConstants.VTB_MERCHANTID), ""))) {
				LOGGER.error("== MerchantId not support: {}", merchantId);
				return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_INVALID_MERCHANT_ID, data, type);
			}

			// validate signature
			if (!verifySignature(requestId + providerId + merchantId + code, signature)) {
				LOGGER.error("== Verify signature fail!!!");
				return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_INVALID_SIG, data, type);
			}

			LOGGER.info("== Validation success!");
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_CONNECTOR_SUCCESS, data, type);

		} catch (Exception e) {
			LOGGER.error("== Validate response from Vietin error!!!", e);
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.VTB_VALIDATION_FUNCTION_FAIL, data, type);

		}
	}

	@Override
	public VietinEwalletTransaction save(VietinEwalletTransaction transData) throws Exception {
		transData.setCreatedDate(new Date(System.currentTimeMillis()));
		transData.setUpdatedDate(new Date(System.currentTimeMillis()));
		return (VietinEwalletTransaction) transRepository.save(transData);
	}

	@Override
	public VietinEwalletTransaction update(VietinEwalletTransaction transData) throws Exception {
		transData.setUpdatedDate(new Date(System.currentTimeMillis()));
		return (VietinEwalletTransaction) transRepository.save(transData);
	}

	private boolean isValidMessage(String requestId, String providerId, String merchantId, String signature) {
		if (providerId == null || providerId.trim().isEmpty() || requestId == null || requestId.trim().isEmpty()
				|| signature == null || signature.trim().isEmpty() || merchantId == null
				|| merchantId.trim().isEmpty()) {

			return false;
		}
		return true;
	}

	private boolean verifySignature(String data, String signature) throws CertificateException, IOException {
		PublicKey publicKeyVietin = encryptUtil.readPublicKey2(configLoader.getVtbPublicKey());
		return encryptUtil.verifySignature(data, signature, publicKeyVietin);
	}

}
