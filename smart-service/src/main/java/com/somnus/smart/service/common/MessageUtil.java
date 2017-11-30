package com.somnus.smart.service.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.message.Message;
import com.somnus.smart.message.Response;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

public class MessageUtil {
	
	/**
	 * 业务异常返回
	 * 
	 * @param ex
	 * @return
	 * @throws Exception
	 */
	public static Message errRetrunInAction(BizException ex) {
		Message message = new Message();
		String errMsg = ex.getMessage();
		if (errMsg != null && errMsg.split("[|]").length > 1) {
			String[] errs = errMsg.split("[|]");
			// 错误信息设置
			message.setRepCode(errs[0]);
			message.setRepMsg(errs[1]);
		} else {
			// 错误信息设置
			message.setRepCode(MsgCodeList.ERROR_999999);
			message.setRepMsg(ex.getMessage());
		}
		createMsg(message);
		return message;
	}
	
	/**
	 * createCommMsg创建公共成功返回报文
	 * 
	 * @param reqHead
	 * @return repHead
	 */
	public static Message createCommMsg() {
		Message message = new Message();
		createMsg(message);
		message.setRepCode(MsgCodeList.SUCCESS_000000);
		message.setRepMsg("处理成功!");
		return message;
	}
	
	/**
	 * 系统异常返回
	 * @return
	 */
	public static Message createErrorMsg() {
		Message message = new Message();
		createMsg(message);
		message.setRepCode(MsgCodeList.ERROR_999999);
		message.setRepMsg("处理失败!");
		return message;
	}
	
	public static void errRetrunInAction(Message message,BizException ex) {
		String errMsg = ex.getMessage();
		if (errMsg != null && errMsg.split("[|]").length > 1) {
			String[] errs = errMsg.split("[|]");
			// 错误信息设置
			message.setRepCode(errs[0]);
			message.setRepMsg(errs[1]);
		} else {
			// 错误信息设置
			message.setRepCode(MsgCodeList.ERROR_999999);
			message.setRepMsg(ex.getMessage());
		}
		createMsg(message);
	}
	
	/**
	 * createCommMsg创建公共成功返回报文
	 * 
	 * @param reqHead
	 * @return repHead
	 */
	public static void createCommMsg(Message message) {
		createMsg(message);
		message.setRepCode(MsgCodeList.SUCCESS_000000);
		message.setRepMsg("处理成功!");
	}
	
	/**
	 * 系统异常返回
	 * @return
	 */
	public static void createErrorMsg(Message message) {
		createMsg(message);
		message.setRepCode(MsgCodeList.ERROR_999999);
		message.setRepMsg("处理失败!");
	}
	
	/**
	 * 设置报文头
	 * @param message
	 */
	public static void createMsg(Message message) {
		message.setSysCode(BasConstants.DEFAULT_SYSCODE);
		message.setBrcCode(BasConstants.DEFAULT_BRCCODE);
		message.setChannelNo(BasConstants.DEFAULT_CHANNELNO);
		message.setFrontTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		try {
			String hostName = InetAddress.getLocalHost().getHostName();
			message.setFrontNo(hostName);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 设置账务返回结果报文头
	 * @param repMsg
	 * @param trntransaction
	 */
	public static void setRepMsg(Response repMsg, TrnTransaction trntransaction) {
		createCommMsg(repMsg);
		repMsg.setAccDate(trntransaction.getAccDate());
		repMsg.setAccTranNo(trntransaction.getAccTranNo());
		repMsg.setAppTranNo(trntransaction.getAppTranNo());
	}

}
