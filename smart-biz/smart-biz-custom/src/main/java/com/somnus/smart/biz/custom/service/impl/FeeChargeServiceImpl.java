package com.somnus.smart.biz.custom.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.dao.TcorTrnTranFeeDao;
import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.biz.custom.common.CustomTransfer;
import com.somnus.smart.biz.custom.service.FeeChargeService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.TranFee;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.custom.FeeChargeRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.CoreService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

@Service
public class FeeChargeServiceImpl implements FeeChargeService {

    @Resource
    private TcorTrnTranFeeDao 		tcorTrnTranFeeDao;

    @Resource
    private BasBizService     		basBizService;

    @Autowired
    private CoreService       		coreService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    public void createTrnFree(TrnTransaction trnTransaction, String status) {
        TranFee tranFee = TranFee.getInstance();
        tranFee.setFeeTranNo(tcorTrnTranFeeDao.getFeeTranNo());
        tranFee.setAccTranNo(trnTransaction.getAccTranNo());
        tranFee.setStatus(status);
        tranFee.setSubmitTime(DateUtil.getCurrentTimeStamp());
        tranFee.setCreateTime(DateUtil.getCurrentTimeStamp());
        tranFee.setModifyTime(tranFee.getCreateTime());
        tranFee.setCreateBy(BasConstants.DEFAULT_OPERATOR);
        tranFee.setModifyBy(tranFee.getCreateBy());
        tranFee.save();
    }

    @Transactional
    public Transaction totalFeeChargeDeal(Transaction transaction, String subAccCode) {
        // 超额冻结
        coreService.addBizFreezeBal(AccountType.BIZ,subAccCode, transaction.getTranAmt());
        // 账务流水落地
        transaction.setModifyTime(DateUtil.getCurrentTimeStamp());
        // 设置记账状态为正常记账
        transaction.setAccStatus(BasConstants.ACC_STATUS_SUCCESS);
        // 账务流水落地
        transaction.save();
        // 收费附属流水落地
        createTrnFree(transaction, BasConstants.FEECHARGE_STATUS_FREEZE);
        return transaction;
    }

    @Override
    public void checkMerAllowOut(String payerType, String payerCode, String payerAccCode, String ccyCode, BigDecimal tranAmt) {
        // 添加是否允许出款
        basBizService.checkAllowOut(payerType, payerCode, payerAccCode);
        // 检查商户流动资金账户余额是否足够出款？
        BigDecimal availableBal = basBizService.getAvailableBal(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + payerAccCode, payerAccCode, ccyCode);

        if (availableBal.compareTo(tranAmt) < 0) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_304001, 
            		new Object[] {payerAccCode}));
        }
    }

    @Override
    public Transaction createTransaction(FeeChargeRequest request) {
        Transaction transaction = CustomTransfer.msgToTransaction(request);
        transaction.setOrdAmt(request.getTranAmt());
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        // 设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    @Transactional
    public void feeChargeSynAccount(Transaction transaction, Date accDate, boolean checkRed, String entryKeyFeeCharge) throws Exception {
        if (transaction == null) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302032, new Object[] {}));
        }
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKeyFeeCharge, accDate, checkRed, null);
    }

    @Override
    @Transactional
    public Transaction unFeeChargeSynAccount(final Transaction transaction, Date accDate, boolean checkRed, String entryKeyUnfeeCharge)
                                                                                                                                       throws Exception {
        if (transaction == null) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302032, new Object[] {}));
        }
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKeyUnfeeCharge, accDate, checkRed, null);
        return transaction;
    }

}
