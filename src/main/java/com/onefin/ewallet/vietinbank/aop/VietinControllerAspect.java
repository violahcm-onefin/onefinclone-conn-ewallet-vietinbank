package com.onefin.ewallet.vietinbank.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.onefin.ewallet.common.base.errorhandler.RuntimeInternalServerException;
import com.onefin.ewallet.vietinbank.common.VietinConstants;
import com.onefin.ewallet.vietinbank.common.VietinConstants.LinkType;
import com.onefin.ewallet.vietinbank.linkbank.model.VietinConnResponse;
import com.onefin.ewallet.vietinbank.service.ConfigLoader;
import com.onefin.ewallet.vietinbank.service.IMessageUtil;

@Aspect
@Component
public class VietinControllerAspect {
	
	@Autowired
	private IMessageUtil imsgUtil;
	
	@Autowired
	private ConfigLoader configLoader;

	@Around(value = "execution(* com.onefin.ewallet.vietinbank.controller.VietinLinkBankController.*(..))")
	public Object checkAvailableService(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		VietinConnResponse responseEntity = null;
		try {
			if (args[0].equals(LinkType.ACCOUNT) && configLoader.isVietinActiveAccount() == false) {
				responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_INTERNAL_SERVER_ERROR, null,
						args[0].toString());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			}
			if (args[0].equals(LinkType.CARD) && configLoader.isVietinActiveCard() == false) {
				responseEntity = imsgUtil.buildVietinConnectorResponse(VietinConstants.CONN_SERVICE_NOT_AVAILABLE, null,
						args[0].toString());
				return new ResponseEntity<>(responseEntity, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new RuntimeInternalServerException();
		}
		return joinPoint.proceed();
	}

}
