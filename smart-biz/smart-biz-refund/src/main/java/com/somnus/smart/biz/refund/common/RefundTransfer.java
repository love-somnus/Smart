package com.somnus.smart.biz.refund.common;

import org.springframework.beans.BeanUtils;

import com.somnus.smart.domain.account.RefundTransaction;
import com.somnus.smart.domain.account.TranRefund;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AutoRefundRequest;
import com.somnus.smart.message.account.BankRefundRequest;
import com.somnus.smart.message.account.IpsRefundRequest;
import com.somnus.smart.service.common.CommonTransfer;
import com.somnus.smart.support.util.DateUtil;

public class RefundTransfer extends CommonTransfer{
	/**
	 * 报文转换为实体
	 * @param request
	 */
	public static Transaction msgToTransaction(BankRefundRequest request){
		Transaction transaction = Transaction.getInstance();
		BeanUtils.copyProperties(request, transaction);
		msgAdditional(request, transaction);
		CommonTransfer.initial(transaction);
		return transaction;
	}
	/**
	 * 将退款请求转换成退款附属流水
	 * 
	 * @param request
	 * @return
	 */
	public static TranRefund msgToTranRefund(BankRefundRequest request){
		TranRefund tranRefund = TranRefund.getInstance();
		BeanUtils.copyProperties(request, tranRefund);
		tranRefund.setOriAuthNo(request.getOriAuthCode());
		tranRefund.setAppRefundDate(request.getAppTranDate());
		
		tranRefund.setRefundAmt(request.getTranAmt());
		tranRefund.setStatus(RefundConstants.REF_STATUS_HANDING);
		tranRefund.setSubmitTime(DateUtil.getCurrentTimeStamp());
		tranRefund.setCreateTime(DateUtil.getCurrentTimeStamp());
		tranRefund.setModifyTime(tranRefund.getCreateTime());
		tranRefund.setCreateBy(RefundConstants.DEFAULT_OPERATOR);
		tranRefund.setModifyBy(tranRefund.getCreateBy());
		tranRefund.setBizType(RefundConstants.BIZ_TYPE_BANKREFUND);
		tranRefund.setCardholder(request.getCardHolder());
		return tranRefund;
	}
	/**
	 * 
	 * 
	 * @param transaction
	 * @param tranRefund
	 * @return
	 */
	public static RefundTransaction RefundTransaction(Transaction transaction,TranRefund tranRefund){
	    RefundTransaction refundTransaction = RefundTransaction.getInstance();
		refundTransaction.setSysCode(transaction.getSysCode());
		refundTransaction.setBillNo(transaction.getIpsBillNo());
		refundTransaction.setMerBillNo(transaction.getCusTranNo());
		refundTransaction.setAppTranNo(transaction.getAppTranNo());
		refundTransaction.setPayTranNo(null);
		refundTransaction.setPrdCode(transaction.getPrdCode());
		refundTransaction.setMerCode(transaction.getPayerCode());
		//trnRefund.setmerName;
		refundTransaction.setMerAccCode(transaction.getPayerAccCode());
		//trnRefund.setmerAccName;
		refundTransaction.setMerBizKind(transaction.getPayerType());
		refundTransaction.setTranType(transaction.getTranType());
		refundTransaction.setCcyCode(transaction.getCcyCode());
		refundTransaction.setTranAmount(transaction.getTranAmt());
		refundTransaction.setTranDate(transaction.getAppTranDate());
		//01 网关退款 02 CAT退款 
		refundTransaction.setBizType(tranRefund.getBizType());
		refundTransaction.setPayeeBankAccCode(tranRefund.getCardNo());
		refundTransaction.setPayeeBankAccCodeEx(tranRefund.getCardNoEx());
		refundTransaction.setPayeeBankAccName(tranRefund.getCardholder());
		refundTransaction.setPayeeBankAccType(null);
		//trnRefund.setPayerBankCode(trntransaction.getBankAccCode());
		refundTransaction.setPayerBankName(null);
		refundTransaction.setIsOnline(RefundConstants.IS_ONLINE_TRUE);
		refundTransaction.setChannelCode(transaction.getChannelCode());
		refundTransaction.setChannelName(null);
		refundTransaction.setIsExpired(null);
		refundTransaction.setFileId(null);
		refundTransaction.setTabTime(null);
		refundTransaction.setOriSupplierBillNo(tranRefund.getOriSupplierBillNo());
		refundTransaction.setOriAppTranNo(tranRefund.getOriAppTranNo());
		refundTransaction.setOriRefrenceNo(tranRefund.getOriRefrenceNo());
		refundTransaction.setOriAuthNo(tranRefund.getOriAuthNo());
		refundTransaction.setOriBillDate(tranRefund.getOriBillDate());
		refundTransaction.setOriTranAmount(tranRefund.getOriTranAmt());
		refundTransaction.setRefType(tranRefund.getRefType());
		refundTransaction.setStatus("00");
		refundTransaction.setRefundArg(tranRefund.getRefundArg());
		refundTransaction.setFinishFlag("0");
		refundTransaction.setFinishNo(null);
		refundTransaction.setFinishDate(null);
		refundTransaction.setErrorMessage(null);
		refundTransaction.setRemark(null);
		refundTransaction.setCreateTime(DateUtil.getCurrentTimeStamp());
		refundTransaction.setCreateBy(RefundConstants.DEFAULT_OPERATOR);
		refundTransaction.setModifyTime(refundTransaction.getCreateTime());
		refundTransaction.setModifyBy(refundTransaction.getCreateBy());
		
		refundTransaction.setPayeeBankCode(transaction.getSupplierCode());
		return refundTransaction;
	}
	private static void msgAdditional(BankRefundRequest request,Transaction trntransaction){
		trntransaction.setMachineNo(request.getFrontNo());
		trntransaction.setCusTranNo(request.getMerBillNo());
		trntransaction.setOrdAmt(request.getTranAmt());
	}
	
	public static Transaction msgToTransaction(IpsRefundRequest request) {
		Transaction transaction = Transaction.getInstance();
		BeanUtils.copyProperties(request, transaction);
		transaction.setMachineNo(request.getFrontNo());
		transaction.setCusTranNo(request.getMerBillNo());
		transaction.setOrdAmt(request.getTranAmt());
		CommonTransfer.initial(transaction);
		transaction.setRefundedAmt(request.getTranAmt());
		return transaction;
	}
	public static Transaction msgToTransaction(AutoRefundRequest request) {
		Transaction trntransaction = Transaction.getInstance();
		BeanUtils.copyProperties(request, trntransaction);
		trntransaction.setMachineNo(request.getFrontNo());
		trntransaction.setCusTranNo(request.getMerBillNo());
		trntransaction.setOrdAmt(request.getTranAmt());
		CommonTransfer.initial(trntransaction);
		trntransaction.setRefundedAmt(request.getTranAmt());
		return trntransaction;
	}
	public static TranRefund msgToTranRefund(AutoRefundRequest request){
		TranRefund tranRefund = TranRefund.getInstance();
		BeanUtils.copyProperties(request, tranRefund);
		tranRefund.setOriAuthNo(request.getOriAuthCode());
		tranRefund.setAppRefundDate(request.getAppTranDate());
		tranRefund.setRefundAmt(request.getTranAmt());
		tranRefund.setStatus(RefundConstants.REF_STATUS_HANDING);
		tranRefund.setSubmitTime(DateUtil.getCurrentTimeStamp());
		tranRefund.setCreateTime(DateUtil.getCurrentTimeStamp());
		tranRefund.setModifyTime(tranRefund.getCreateTime());
		tranRefund.setCreateBy(RefundConstants.DEFAULT_OPERATOR);
		tranRefund.setModifyBy(tranRefund.getCreateBy());
		tranRefund.setBizType(RefundConstants.BIZ_TYPE_AUTOREFUND);
		tranRefund.setOriTranAmt(request.getOriBillAmt());
		tranRefund.setCardholder(request.getCardHolder());
		return tranRefund;
	}
}
