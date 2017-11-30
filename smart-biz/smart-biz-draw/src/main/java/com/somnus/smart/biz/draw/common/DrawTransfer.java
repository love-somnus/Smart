package com.somnus.smart.biz.draw.common;

import org.springframework.beans.BeanUtils;

import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.TranDraw;
import com.somnus.smart.domain.account.TranIssued;
import com.somnus.smart.domain.account.TranRefuse;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.BankDrawRequest;
import com.somnus.smart.message.account.BatchBankDrawRequest;
import com.somnus.smart.message.account.BatchBankDrawRequest.Order;
import com.somnus.smart.message.refusepay.RefusePayRequest;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.CommonTransfer;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;


public class DrawTransfer extends CommonTransfer {

    /**
     * 报文转换为实体
     * 
     * @param request
     */
    public static Transaction msgToTransaction(BankDrawRequest request) {
        Transaction transaction = Transaction.getInstance();
        BeanUtils.copyProperties(request, transaction);
        msgAdditional(request, transaction);
        CommonTransfer.initial(transaction);
        return transaction;
    }

    public static TranDraw msgToTranDraw(BankDrawRequest request) {
        TranDraw tranDraw = TranDraw.getInstance();
        BeanUtils.copyProperties(request, tranDraw);
        tranDraw.setStatus(DrawConstants.DRAW_STATUS_OUT);
        tranDraw.setSubmitTime(DateUtil.getCurrentTimeStamp());
        tranDraw.setCreateTime(DateUtil.getCurrentTimeStamp());
        tranDraw.setModifyTime(tranDraw.getCreateTime());
        tranDraw.setCreateBy(DrawConstants.DEFAULT_OPERATOR);
        tranDraw.setModifyBy(tranDraw.getCreateBy());
        return tranDraw;
    }

    public static DrawTransaction getTrawTransaction(Transaction transaction, TranDraw tranDraw) {
        DrawTransaction drawTransaction = DrawTransaction.getInstance();
        drawTransaction.setAppBatchNo(transaction.getAppBatchNo());
        drawTransaction.setAppTranNo(transaction.getAppTranNo());
        drawTransaction.setSupplierBillNo(null);
        drawTransaction.setPrdCode(transaction.getPrdCode());
        drawTransaction.setMerCode(transaction.getPayerCode());
        //trnDraw.setMerName(merMerchant.getMerName());
        drawTransaction.setMerAccCode(transaction.getPayerAccCode());
        //trnDraw.setMerAccName(merAccount.getAcctName());
        drawTransaction.setMerBizKind(transaction.getPayerType());
        drawTransaction.setCcyCode(transaction.getCcyCode());
        drawTransaction.setTranAmount(transaction.getOrdAmt());
        drawTransaction.setChannelCode(transaction.getChannelCode());
        drawTransaction.setChannelName(null);
        drawTransaction.setPayeeBankCode(tranDraw.getPayeeBankCode());
        drawTransaction.setPayeeBraBankCode(tranDraw.getPayeeBraBankCode());
        drawTransaction.setPayeeBraBankName(tranDraw.getPayeeBraBankName());
        drawTransaction.setPayerBankCode(null);
        drawTransaction.setPayerBraBankCode(null);
        drawTransaction.setPayerBraBankName(null);
        drawTransaction.setIpsAccount(null);
        drawTransaction.setPayeeBankAccCode(tranDraw.getPayeeBankAccCode());
        drawTransaction.setPayeeBankAccName(tranDraw.getPayeeBankAccName());
        drawTransaction.setIsOnline(DrawConstants.IS_ONLINE_TRUE);
        drawTransaction.setErrorMessage(null);
        // 1123 协议出款
        drawTransaction.setTranType(transaction.getTranType());
        drawTransaction.setFileId(null);
        // 出款状态 00 待处理
        drawTransaction.setStatus("00");
        drawTransaction.setRemark(null);
        drawTransaction.setPayeeBankAccType(tranDraw.getPayeeBankAccType());
        drawTransaction.setBizType(DrawTransfer.getBizType(transaction.getPrdCode()));
        drawTransaction.setPriNo(BasConstants.DRAW_PRI_NO_DEFAULT);
        drawTransaction.setTranDate(transaction.getAppTranDate());
        drawTransaction.setTabTime(null);
        drawTransaction.setSysCode(transaction.getSysCode());
        drawTransaction.setFinishFlag("0");
        drawTransaction.setFinishNo(null);
        drawTransaction.setFinishTime(null);
        drawTransaction.setCreateTime(DateUtil.getCurrentTimeStamp());
        drawTransaction.setCreateBy(DrawConstants.DEFAULT_OPERATOR);
        drawTransaction.setModifyTime(tranDraw.getCreateTime());
        drawTransaction.setModifyBy(tranDraw.getCreateBy());
        return drawTransaction;
    }

    private static void msgAdditional(BankDrawRequest request, TrnTransaction trntransaction) {
        trntransaction.setMachineNo(request.getFrontNo());
        trntransaction.setCusTranNo(request.getMerBillNo());
        trntransaction.setFeeFlag(request.getFeeWay());
        trntransaction.setThirdAccCode(request.getThdAccCode());
    }

    public static Transaction msgToTransaction(BatchBankDrawRequest request) {
        Transaction transaction = Transaction.getInstance();
        BeanUtils.copyProperties(request, transaction);
        transaction.setMachineNo(request.getFrontNo());
        transaction.setFeeFlag(request.getFeeWay());
        transaction.setAppTranNo(request.getAppBatchNo());
        //应用交易日期
        transaction.setAppTranDate(request.getOrders().get(0).getAppTranDate());
        transaction.setOrdAmt(request.getBatchAmt());
        if (DrawConstants.FEE_FLAG_PAY.equals(request.getFeeWay())) {
            transaction.setTranAmt(request.getBatchAmt().add(request.getTotalFeeAmt()));
        } else {
            transaction.setTranAmt(request.getBatchAmt());
        }
        transaction.setFeeAmt(request.getTotalFeeAmt());
        transaction.setThirdAccCode(request.getThdAccCode());
        CommonTransfer.initial(transaction);
        return transaction;
    }

    public static TranIssued msgToTranIssued(BatchBankDrawRequest request, Order order) {
        TranIssued tranIssued = TranIssued.getInstance();
        BeanUtils.copyProperties(request, tranIssued);
        BeanUtils.copyProperties(order, tranIssued);
        tranIssued.setSubmitTime(DateUtil.getCurrentTimeStamp());
        tranIssued.setCreateTime(DateUtil.getCurrentTimeStamp());
        tranIssued.setModifyTime(tranIssued.getCreateTime());
        tranIssued.setCreateBy(DrawConstants.DEFAULT_OPERATOR);
        tranIssued.setModifyBy(tranIssued.getCreateBy());
        return tranIssued;
    }

    public static String getBizType(String prdCode) {

        String bizType = null;
        // 01协议出款、02提现、03下发
        if (DrawConstants.ISSUED_PROTOCOL_DRAW.equals(prdCode)) {
            bizType = DrawConstants.BIZTYPE_PROTOCOLDRAW;
        } else if (DrawConstants.ISSUED_WITHDRAW_B2B.equals(prdCode) || DrawConstants.ISSUED_WITHDRAW_B2C.equals(prdCode)) {
            bizType = DrawConstants.BIZTYPE_WITHDRAW;
        } else if (DrawConstants.ISSUED_IMMEDIATE_B2B.equals(prdCode) || DrawConstants.ISSUED_IMMEDIATE_B2C.equals(prdCode)) {
            bizType = DrawConstants.BIZTYPE_ISSUED_IMMEDIATE_DRAW;
        } else if (DrawConstants.ISSUED_RAPID_B2B.equals(prdCode) || DrawConstants.ISSUED_RAPID_B2C.equals(prdCode)) {
            bizType = DrawConstants.BIZTYPE_ISSUED_RAPID_DRAW;
        } else if (DrawConstants.ISSUED_COMMON_B2B.equals(prdCode) || DrawConstants.ISSUED_COMMON_B2C.equals(prdCode)) {
            bizType = DrawConstants.BIZTYPE_ISSUED_RAPID_DRAW;
        } else {
            throw new BizException("产品编号不正确");
        }
        return bizType;
    }

    public static Transaction msgToTransaction(RefusePayRequest request) {
        Transaction transaction = Transaction.getInstance();
        BeanUtils.copyProperties(request, transaction);
        transaction.setMachineNo(request.getFrontNo());
        transaction.setOrdAmt(request.getTranAmt());
        CommonTransfer.initial(transaction);
        transaction.setDishonoredAmt(request.getTranAmt());
        return transaction;
    }

    public static TranRefuse msgToTranRefuse(RefusePayRequest request) {
        TranRefuse tranRefuse = TranRefuse.getInstance();
        BeanUtils.copyProperties(request, tranRefuse);
        tranRefuse.setStatus(DrawConstants.REFUSE_STATUS0);//受理中
        tranRefuse.setSubmitTime(DateUtil.getCurrentTimeStamp());
        tranRefuse.setCreateTime(DateUtil.getCurrentTimeStamp());
        tranRefuse.setModifyTime(tranRefuse.getCreateTime());
        tranRefuse.setCreateBy(BasConstants.DEFAULT_OPERATOR);
        tranRefuse.setModifyBy(tranRefuse.getCreateBy());
        return tranRefuse;
    }
}
