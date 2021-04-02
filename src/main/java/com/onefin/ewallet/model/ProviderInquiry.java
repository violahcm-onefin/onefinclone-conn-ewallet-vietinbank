package com.onefin.ewallet.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProviderInquiry {

	@Size(max = 12)
	@NotEmpty(message = "Not empty")
	private String requestId;

	@Size(max = 15)
	@NotEmpty(message = "Not empty")
	private String channel;

	@Size(max = 3)
	private String language;

	@Size(max = 30)
	private String mac;

	public ProviderInquiry() {
		this.language = new String();
		this.mac = new String();
	}

}
