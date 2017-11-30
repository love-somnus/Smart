package com.somnus.smart.message.account;

import java.util.Date;

import com.somnus.smart.message.Response;


/**
 * AccountResponse
 * 
 * @author IH745
 * @version 1.0 13-10-9
 */
public class AccountResponse extends Response {

	private static final long serialVersionUID = 1L;
	
	private String appTranNo;
	
	private String accTranNo;
	
	private Date accDate;

	public String getAppTranNo() {
		return appTranNo;
	}

	public void setAppTranNo(String appTranNo) {
		this.appTranNo = appTranNo;
	}

	public String getAccTranNo() {
		return accTranNo;
	}

	public void setAccTranNo(String accTranNo) {
		this.accTranNo = accTranNo;
	}

	public Date getAccDate() {
		return accDate;
	}

	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}

}
