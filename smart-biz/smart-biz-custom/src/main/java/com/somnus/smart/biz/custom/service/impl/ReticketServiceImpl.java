package com.somnus.smart.biz.custom.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.biz.custom.common.CusConstants;
import com.somnus.smart.biz.custom.service.ReticketService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.RefundTransaction;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.reticket.ReticketRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.CommonTransfer;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 退票记账业务处理实现
 */
@Service
public class ReticketServiceImpl implements ReticketService {

    @Resource
    private BasBizService 			basBizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    public Transaction createTransaction(ReticketRequest request) {
        Transaction transaction = Transaction.getInstance();
        BeanUtils.copyProperties(request, transaction);
        transaction.setMachineNo(request.getFrontNo());
        CommonTransfer.initial(transaction);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    @Transactional
    public void drawReticketSynAccount(Transaction trntransaction,String oriAppTranNo) throws Exception {
        DrawTransaction draw = DrawTransaction.queryDrawFlagByApp(oriAppTranNo);//根据交易流水号查询已销账出款流水记录
        if (draw == null) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302039, new Object[] {}));
        }
        trntransaction.setSupplierCode(draw.getPayerBankCode());//机构号
        trntransaction.setBankAccCode(draw.getIpsAccount());//通道对应银行收款账号
        trntransaction.setBankCost(draw.getBankCost());//银行成本
        trntransaction.setSysCode(draw.getSysCode());//原交易系统编号
        Account account = Account.getInstance();
        account.synAccount(trntransaction, CusConstants.ENTRY_KEY_RETICKET1, trntransaction.getAccDate(), true, null);
    }

    @Override
    @Transactional
    public void refundReticketSysAccount(Transaction trntransaction, String oriAppTranNo,String entryKey) throws Exception {
        RefundTransaction refundTransaction = RefundTransaction.queryRefundFlagByApp(oriAppTranNo);//根据交易流水号查询已销账退款流水记录
        if (refundTransaction == null) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302039, new Object[] {}));
        }
        trntransaction.setSupplierCode(refundTransaction.getPayerBankCode());//机构号
        trntransaction.setBankAccCode(refundTransaction.getIpsAccount());//通道对应银行收款账号
        trntransaction.setBankCost(refundTransaction.getBankCost());//银行成本
        trntransaction.setSysCode(refundTransaction.getSysCode());//原交易系统编号
        Account account=Account.getInstance();
        account.synAccount(trntransaction, entryKey, trntransaction.getAccDate(), true, null);
    }

}
