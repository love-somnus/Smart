package com.somnus.smart.message.account;

import com.somnus.smart.message.Message;

public class ProtocolDrawResponse extends Message {

	private static final long serialVersionUID = 1L;
	
	private String transferStatus;

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
}
