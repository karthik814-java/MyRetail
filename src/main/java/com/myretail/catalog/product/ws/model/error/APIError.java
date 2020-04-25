package com.myretail.catalog.product.ws.model.error;

public class APIError {

	private String apimsg;

	private int httpcode;

	public String getApimsg() {
		return apimsg;
	}

	public void setApimsg(String apimsg) {
		this.apimsg = apimsg;
	}

	public int getHttpcode() {
		return httpcode;
	}

	public void setHttpcode(int httpcode) {
		this.httpcode = httpcode;
	}

	@Override
	public String toString() {
		return "APIError [apimsg=" + apimsg + ", httpcode=" + httpcode + "]";
	}



}
