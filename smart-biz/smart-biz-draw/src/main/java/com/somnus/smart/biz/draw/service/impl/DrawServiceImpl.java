package com.somnus.smart.biz.draw.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.domain.MerAccount;
import com.somnus.smart.base.domain.MerMerchant;
import com.somnus.smart.base.domain.PerAccount;
import com.somnus.smart.biz.draw.common.DrawTransfer;
import com.somnus.smart.biz.draw.service.DrawService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.TranDraw;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.BankDrawRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 出款服务实现
 */
@Service
public class DrawServiceImpl implements DrawService {
	/** basBizService */
    @Resource
    private BasBizService 			basBizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    public void checkMerCanDraw(String payerType, String payerCode, String payerAccCode, String ccyCode, BigDecimal orderAmt,String feeWay,String thdAccCode) {
        //校验商户是否允许出款
        basBizService.checkAllowOut(payerType, payerCode, payerAccCode);
        //根据实际科目号、交易账户、币种查询账户余额，计算可用余额
        String accountType=null;
        if(payerType.equals(DrawConstants.BIZ_KIND_PERSON)){
            accountType=AccountType.IDV.getCode();
        }else if(payerType.equals(DrawConstants.BIZ_KIND_CUS)){
            accountType=AccountType.BIZ.getCode();
        }
        BigDecimal availableBal = basBizService.getAvailableBal(AccountType.getByCode(accountType),DrawConstants.REL_SUB_CODE_FREE + payerAccCode, payerAccCode, ccyCode);
        //如果可用余额小于订单金额
        if (availableBal.compareTo(orderAmt) < 0) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_304001, 
            		new Object[] {payerAccCode}));
        }
        //手續費如果是第三方承擔手续费校验第三方账户是否存在
        if(feeWay.equals(BasConstants.FEE_FLAG_THD)){
        	if (StringUtils.isBlank(thdAccCode)) {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302040, new Object[] {}));
            }
        	basBizService.accountExists(thdAccCode);
        }
    }

    @Override
    public Transaction createTransaction(BankDrawRequest request) {
        Transaction transaction = DrawTransfer.msgToTransaction(request);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    public TranDraw createTranDraw(BankDrawRequest request, String accTranNo) {
        TranDraw tranDraw = DrawTransfer.msgToTranDraw(request);
        tranDraw.setAccTranNo(accTranNo);
        return tranDraw;
    }

    @Override
    public DrawTransaction createDrawTransaction(Transaction transaction, TranDraw tranDraw) {
        DrawTransaction drawTransaction = DrawTransfer.getTrawTransaction(transaction, tranDraw);
        if (DrawConstants.BIZ_KIND_CUS.equals(transaction.getPayerType())) {
            MerMerchant merMerchant = basBizService.getMerMerchant(transaction.getPayerCode());
            if (null != merMerchant) {
                //设置商户名称
                drawTransaction.setMerName(merMerchant.getMerName());
            }
            MerAccount merAccount = basBizService.getMerAccount(transaction.getPayerCode(), transaction.getPayerAccCode());
            if (null != merAccount) {
                //设置交易账户名
                drawTransaction.setMerAccName(merAccount.getAcctName());
                //设置非协议出款出款优先级默认值
                drawTransaction.setPriNo(BasConstants.DRAW_PRI_NO_DEFAULT);
            }
        } else {
            PerAccount perAccount = basBizService.getPerAccount(transaction.getPayerAccCode());
            if (null != perAccount) {
                drawTransaction.setMerName(perAccount.getAcctName());
                drawTransaction.setMerAccName(perAccount.getAcctName());
                //如果是收款方承担手续费，出款流水的交易金额=记账流水的交易金额-手续费
                if(transaction.getFeeFlag().equals(BasConstants.FEE_FLAG_REC)){
                	 drawTransaction.setTranAmount(transaction.getTranAmt().subtract(transaction.getFeeAmt()));
                }
            }
        }
        return drawTransaction;
    }

    @Override
    @Transactional
    public void drawSynAccount(Transaction transaction, String entryKey, Date accDate, boolean checkRed, final TranDraw tranDraw,
                            final DrawTransaction drawTransaction) throws Exception {
        if (transaction == null || tranDraw == null || drawTransaction == null || entryKey == null) {
            throw new BizException("交易流水、账务日期、entryKey、出款附属流水、出款交易流水不能为空！");
        }
        //出款记账回调
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKey, accDate, checkRed, new AccountCallBack() {
            @Override
            public DrawTransaction callBack() {
                //落地出款附属
                tranDraw.save();
                //落地出款交易流水
                if (StringUtils.isBlank(drawTransaction.getPriNo())) {
                    // 出款优先级
                    drawTransaction.setPriNo(BasConstants.DRAW_PRI_NO_DEFAULT);
                }
                drawTransaction.save();
                return drawTransaction;
            }
        });
    }
}
