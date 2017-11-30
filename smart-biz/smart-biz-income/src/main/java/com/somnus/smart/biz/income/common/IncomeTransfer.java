package com.somnus.smart.biz.income.common;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.IncomeRequest;
import com.somnus.smart.message.account.IpsPayRequest;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.CommonTransfer;
import com.somnus.smart.support.util.DateUtil;

public class IncomeTransfer extends CommonTransfer {

    /**
     * 报文转换为实体
     * 
     * @param incomeRequest
     */
    public static Transaction msgToTransaction(IncomeRequest incomeRequest) {
        Transaction transaction = Transaction.getInstance();
        BeanUtils.copyProperties(incomeRequest, transaction);
        msgAdditional(incomeRequest, transaction);
        incomeSet(transaction);
        return transaction;
    }

    private static void msgAdditional(IncomeRequest incomeRequest, Transaction transaction) {
        transaction.setMachineNo(incomeRequest.getFrontNo());
        transaction.setCusTranNo(incomeRequest.getMerBillNo());
        transaction.setChlTranNo(incomeRequest.getPayTranNo());
        transaction.setFeeFlag(incomeRequest.getFeeWay());
        transaction.setThirdAccCode(incomeRequest.getThdAccCode());
    }

    private static void incomeSet(Transaction transaction) {
        CommonTransfer.initial(transaction);
        transaction.setBlnStatus(IncomeConstants.BLN_STATUS_NOTENTER);
        transaction.setAccMode(IncomeConstants.ACC_MODE_ASYN);
        transaction.setAccStatus(IncomeConstants.ACC_STATUS_HANDLING);
    }

    public static Transaction msgToTransaction(IpsPayRequest request) {
        Transaction transaction = Transaction.getInstance();
        BeanUtils.copyProperties(request, transaction);
        transaction.setMachineNo(request.getFrontNo());
        transaction.setCusTranNo(request.getMerBillNo());
        transaction.setFeeFlag(request.getFeeWay());
        transaction.setThirdAccCode(request.getThdAccCode());
        transaction.setSubmitTime(new Date());
        transaction.setIsDeposit(IncomeConstants.IS_DEPOSIT_FALSE);
        transaction.setAccMode(IncomeConstants.ACC_MODE_SYN);
        transaction.setAccStatus(IncomeConstants.ACC_STATUS_SUCCESS);
        transaction.setBlnMode(IncomeConstants.BLN_MODE_NOW);
        transaction.setBlnStatus(IncomeConstants.BLN_STATUS_ENTERED);
        transaction.setCreateTime(DateUtil.getCurrentTimeStamp());
        transaction.setModifyTime(transaction.getCreateTime());
        transaction.setCreateBy(IncomeConstants.DEFAULT_OPERATOR);
        transaction.setModifyBy(transaction.getCreateBy());
        transaction.setFrozenFlag(BasConstants.FROZEN_NO);
        transaction.setStatus(IncomeConstants.STATUS_NORMAL);
        transaction.setRefundedAmt(new BigDecimal(0));
        transaction.setDishonoredAmt(new BigDecimal(0));
        return transaction;
    }

}
