package com.onefin.ewalletvtb.common;

import org.springframework.stereotype.Service;

import com.onefin.ewalletvtb.model.VietinBaseMessage;
import com.onefin.ewalletvtb.model.VietinConnectorResponse;

@Service
public class MessageUtil {

	public VietinConnectorResponse buildVietinValidateSuccess(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_CONNECTOR_SUCCESS, version, vtBase);
	}

	public VietinConnectorResponse buildVietinInvalidSig(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_INVALID_SIG, version, vtBase);
	}

	public VietinConnectorResponse buildVietinErrorResponse(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_ERROR_RESPONSE, version, vtBase);
	}

	public VietinConnectorResponse buildVietinInvalidProviderId(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_INVALID_PROVIDER_ID, version, vtBase);
	}

	public VietinConnectorResponse buildVietinInvalidMerchantId(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_INVALID_MERCHANT_ID, version, vtBase);
	}

	public VietinConnectorResponse buildVietinInvalidResponse(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_INVALID_RESPONSE, version, vtBase);
	}

	public VietinConnectorResponse buildVietinValidationFunctionFail(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_VALIDATION_FUNCTION_FAIL, version, vtBase);
	}

	public VietinConnectorResponse buildVietinSystemError(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.VTB_SYSTEM_ERROR, version, vtBase);
	}

	public VietinConnectorResponse buildConnectorValidationRequesBodyFail(String version, VietinBaseMessage vtBase) {
		return buildVietinConnectorResponse(OneFinConstants.CONNECTOR_INVALID_VALIDATION_REQUESTBODY, version, vtBase);
	}

	private VietinConnectorResponse buildVietinConnectorResponse(String connectorCode, String version,
			VietinBaseMessage vtBase) {
		VietinConnectorResponse response = new VietinConnectorResponse();
		response.setConnectorCode(connectorCode);
		response.setVtbResponse(vtBase);
		response.setVersion(version);
		return response;
	}
}
