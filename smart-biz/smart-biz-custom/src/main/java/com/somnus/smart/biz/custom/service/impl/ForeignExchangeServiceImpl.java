package com.somnus.smart.biz.custom.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.biz.custom.common.CustomTransfer;
import com.somnus.smart.biz.custom.service.ForeignExchangeService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.custom.PurchaseExchangeRequest;
import com.somnus.smart.message.custom.PurchaseWriteOffRequest;
import com.somnus.smart.message.custom.SettlementExchangeRequest;
import com.somnus.smart.message.custom.SettlementWriteOffRequest;
import com.somnus.smart.service.BasBizService;

@Service
public class ForeignExchangeServiceImpl implements ForeignExchangeService {

    @Resource
    private BasBizService basBizService;

    @Override
    public Transaction createTransaction(SettlementExchangeRequest request) {
        Transaction transaction = CustomTransfer.msgToTransaction(request);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    @Transactional
    public void foreignExchangeSynAccount(Transaction transaction, Date accDate, boolean checkRed, String entryKeySettleExchange) throws Exception {
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKeySettleExchange, transaction.getAccDate(), true, null);
    }

    @Override
    public Transaction createTransaction(SettlementWriteOffRequest request) {
        Transaction transaction = CustomTransfer.msgToTransaction(request);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    public Transaction createTransaction(PurchaseWriteOffRequest request) {
        Transaction transaction = CustomTransfer.msgToTransaction(request);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    public Transaction createTransaction(PurchaseExchangeRequest request) {
        Transaction transaction = CustomTransfer.msgToTransaction(request);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

}
