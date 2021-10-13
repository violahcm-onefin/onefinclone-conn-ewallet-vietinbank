package com.onefin.ewallet.vietinbank.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.onefin.ewallet.common.base.service.BackupService;
import com.onefin.ewallet.common.base.service.BaseService;
import com.onefin.ewallet.common.domain.bank.vietin.VietinLinkBankTransaction;
import com.onefin.ewallet.common.utility.json.JSONHelper;
import com.onefin.ewallet.vietinbank.common.VietinConstants;
import com.onefin.ewallet.vietinbank.linkbank.model.PaymentByOTP;
import com.onefin.ewallet.vietinbank.linkbank.model.PaymentByToken;
import com.onefin.ewallet.vietinbank.linkbank.model.ProviderInquiry;
import com.onefin.ewallet.vietinbank.linkbank.model.Refund;
import com.onefin.ewallet.vietinbank.linkbank.model.RegisterOnlinePay;
import com.onefin.ewallet.vietinbank.linkbank.model.TokenIssue;
import com.onefin.ewallet.vietinbank.linkbank.model.TokenIssuePayment;
import com.onefin.ewallet.vietinbank.linkbank.model.TokenRevokeReIssue;
import com.onefin.ewallet.vietinbank.linkbank.model.TransactionInquiry;
import com.onefin.ewallet.vietinbank.linkbank.model.VerifyPin;
import com.onefin.ewallet.vietinbank.linkbank.model.VietinConnResponse;
import com.onefin.ewallet.vietinbank.linkbank.model.VtbLinkBankBaseResponse;
import com.onefin.ewallet.vietinbank.linkbank.model.Withdraw;

@Service
public class VietinLinkBankServiceImpl extends BaseService<VietinLinkBankTransaction> implements IVietinLinkBankService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VietinLinkBankServiceImpl.class);

	@Autowired
	private ConfigLoader configLoader;

	@Autowired
	private IMessageUtil iMessageUtil;

	@Autowired
	private EncryptUtil encryptUtil;

	@Autowired
	@Qualifier("jsonHelper")
	private JSONHelper JsonHelper;

	@Autowired
	private BackupService backupService;

	@Autowired
	@Qualifier("linkBankTransRepo")
	public void setEwalletTransactionRepository(LinkBankTransRepo<?> ewalletTransactionRepository) {
		this.setTransBaseRepository(ewalletTransactionRepository);
	}

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
		data.setCurrencyCode(VietinConstants.CURRENCY_VND);
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
		data.setCurrencyCode(VietinConstants.CURRENCY_VND);
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
		data.setCurrencyCode(VietinConstants.CURRENCY_VND);
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
		data.setCurrencyCode(VietinConstants.CURRENCY_VND);
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

	@Override
	public Refund buildVietinRefund(Refund data, String linkType)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		data.setCurrencyCode(VietinConstants.CURRENCY_VND);
		data.setProviderId(configLoader.getVietinProviderId());
		if (linkType.equals(VietinConstants.LinkType.CARD.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdCard());
		}
		if (linkType.equals(VietinConstants.LinkType.ACCOUNT.toString())) {
			data.setMerchantId(configLoader.getVietinMerchantIdAccount());
		}
		data.setVersion(configLoader.getVietinVersion());

		String dataSign = String.format("%s%s%s%s%s%s%s%s%s%s%s", data.getAmount(), data.getCurrencyCode(),
				data.getRefundTransactionId(), data.getTransTime(), data.getClientIP(), data.getProviderId(),
				data.getMerchantId(), data.getChannel(), data.getVersion(), data.getLanguage(), data.getMac());

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
	public VietinConnResponse validateResponse(VtbLinkBankBaseResponse data, HttpStatus httpStatus, String type) {
		if (HttpStatus.REQUEST_TIMEOUT.equals(httpStatus)) {
			LOGGER.error("== Failure response from Vietin!!!");
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.CONN_PARTNER_TIMEOUT_RESPONSE, null, type);
		}
		// Check response
		if (data == null) {
			LOGGER.error("== Failure response from Vietin!!!");
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.CONN_PARTNER_ERROR_RESPONSE, null, type);
		}
		try {
			String code = null;
			if (data.getStatus() != null) {
				code = Objects.toString(data.getStatus().getCode(), "");
			}
			if (!isValidMessage(data.getRequestId(), data.getProviderId(), data.getMerchantId(), data.getSignature())) {
				LOGGER.error("== Invalid response from Vietin!!!");
				return iMessageUtil.buildVietinConnectorResponse(VietinConstants.CONN_PARTNER_INVALID_RESPONSE, null, type);
			}

			// validate signature
			if (!verifySignature(data.getRequestId() + data.getProviderId() + data.getMerchantId() + code, data.getSignature())) {
				LOGGER.error("== Verify signature fail!!!");
				return iMessageUtil.buildVietinConnectorResponse(VietinConstants.CONN_PARTNER_INVALID_SIGNATURE, null, type);
			}

			LOGGER.info("== Validation success!");
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.CONN_SUCCESS, data, type);

		} catch (Exception e) {
			LOGGER.error("== Validate response from Vietin error!!!", e);
			return iMessageUtil.buildVietinConnectorResponse(VietinConstants.CONN_FAIL_TO_EXCUTE_VALIDATION_FUNCTION, null, type);

		}
	}

	@Override
	public void backUpRequestResponse(String requestId, Object request, Object response) throws Exception {
		if (request != null) {
			backupService.backup(configLoader.getBackupVietinLinkBank(), requestId, request, VietinConstants.BACKUP_REQUEST);
		}
		if (response != null) {
			backupService.backup(configLoader.getBackupVietinLinkBank(), requestId, response, VietinConstants.BACKUP_RESPONSE);
		}
	}

	private boolean isValidMessage(String requestId, String providerId, String merchantId, String signature) {
		if (providerId == null || providerId.trim().isEmpty() || requestId == null || requestId.trim().isEmpty()
				|| signature == null || signature.trim().isEmpty() || merchantId == null
				|| merchantId.trim().isEmpty()) {

			return false;
		}
		if (!configLoader.getVietinProviderId().equals(providerId)) {
			LOGGER.error("== ProviderId not support: {}", providerId);
			return false;
		}
		if (!configLoader.getVietinMerchantIdCard().equals(merchantId)
				&& !configLoader.getVietinMerchantIdAccount().equals(merchantId)) {
			LOGGER.error("== MerchantId not support: {}", merchantId);
			return false;
		}
		return true;
	}

	private boolean verifySignature(String data, String signature) throws CertificateException, IOException {
		PublicKey publicKeyVietin = encryptUtil.readPublicKey2(configLoader.getVtbPublicKey());
		return encryptUtil.verifySignature(data, signature, publicKeyVietin);
	}

}