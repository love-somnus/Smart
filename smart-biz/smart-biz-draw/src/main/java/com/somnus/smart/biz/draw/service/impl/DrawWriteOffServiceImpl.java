package com.somnus.smart.biz.draw.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.biz.draw.service.DrawWriteOffService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.support.util.DateUtil;

@Service
public class DrawWriteOffServiceImpl implements DrawWriteOffService {

    @Autowired
    private BasBizService  			basbizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;


    @Override
    public Transaction createTransaction(DrawTransaction drawTransaction) {
        Transaction transaction = Transaction.getInstance();
        transaction.setAccTranNo(drawTransaction.getDrawId());
        transaction.setCcyCode(drawTransaction.getCcyCode());
        transaction.setAppTranDate(new Date());
        transaction.setIpsBillNo(null);
        transaction.setAppTranNo(drawTransaction.getAppTranNo());
        transaction.setTranType(drawTransaction.getTranType());
        transaction.setSysCode(drawTransaction.getSysCode());
        transaction.setPrdCode(drawTransaction.getPrdCode());
        transaction.setCusTranNo(null);
        transaction.setTranRemark(drawTransaction.getRemark());
        transaction.setCreateTime(DateUtil.getCurrentTimeStamp());
        transaction.setCreateBy(drawTransaction.getModifyBy());
        transaction.setModifyTime(transaction.getCreateTime());
        transaction.setModifyBy(transaction.getCreateBy());
        transaction.setTranAmt(drawTransaction.getTranAmount());
        transaction.setOrdAmt(drawTransaction.getTranAmount());
        transaction.setFeeAmt(new BigDecimal(0));
        transaction.setSecurityDeposit(new BigDecimal(0));
        transaction.setBankCost(drawTransaction.getBankCost());
        transaction.setPayerCode(drawTransaction.getMerCode());
        transaction.setPayerAccCode(drawTransaction.getMerAccCode());
        transaction.setPayerType(drawTransaction.getMerBizKind());
        transaction.setBankAccCode(drawTransaction.getIpsAccount());
        transaction.setSupplierCode(drawTransaction.getPayerBankCode());
        transaction.setAccDate(basbizService.getSystemAccDate());
        return transaction;
    }

    @Override
    @Transactional
    public void drawWriteOffSynAccount(Transaction transaction,final DrawTransaction drawTransaction,String entryKey,boolean checkRed) throws Exception {
        Account account = Account.getInstance();
        account.synAccountNoTransaction(transaction, entryKey, transaction.getAccDate(), checkRed, new AccountCallBack() {
            
            @Override
            public Object callBack() throws Exception {
                //更新出款流水状态为已核销
                drawTransaction.setFinishFlag(DrawConstants.FINISH_FLAG_TRUE);
                drawTransaction.setFinishTime(new Date());
                drawTransaction.setModifyTime(DateUtil.getCurrentTimeStamp());
                drawTransaction.update();
                return drawTransaction;
            }
        });
    }
}
