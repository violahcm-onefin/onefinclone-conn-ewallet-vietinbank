package com.onefin.ewallet.vietinbank.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.onefin.ewallet.common.base.application.BaseApplication;

@EntityScan("com.onefin.ewallet.common.domain.bank.vietin")
public class MainApplication extends BaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}
