package com.onefin.ewalletvtb.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.onefin")
@ComponentScan(basePackages = "com.onefin")
public class ConnectorEwalletVietinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectorEwalletVietinApplication.class, args);
	}

}
