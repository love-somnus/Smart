package com.somnus.smart.base.domain;

public class CoreAccountItems extends Entity {

	private static final long serialVersionUID = 1L;
	
	private String reqMsgCode; 
	
	private String reqMsg;
	
	public String getReqMsgCode() {
		return reqMsgCode;
	}
	public void setReqMsgCode(String reqMsgCode) {
		this.reqMsgCode = reqMsgCode;
	}
	public String getReqMsg() {
		return reqMsg;
	}
	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	} 
}
