package com.somnus.smart.biz.custom.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.biz.custom.common.CustomTransfer;
import com.somnus.smart.biz.custom.common.ReticketConstants;
import com.somnus.smart.biz.custom.service.BuckleDrawService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.FreezeTransaction;
import com.somnus.smart.domain.account.TranDraw;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.custom.BuckleDrawRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.CoreService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

/**
 * 扣划记账业务处理实现
 */
@Service
public class BuckleDrawServiceImpl implements BuckleDrawService {

    @Resource
    private BasBizService 			basBizService;

    @Resource
    private CoreService   			coreService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    public Transaction createTransaction(BuckleDrawRequest request) {
        Transaction transaction = CustomTransfer.msgToTransaction(request);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        //设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    @Transactional
    public Transaction buckDrawSynAccount(final Transaction transaction, BuckleDrawRequest request, String subAccCode) throws Exception {
        //如果是冻结扣划，先解冻
        if (request.getDeductType().equals("2")) {
            //查找原冻结流水
            FreezeTransaction freezeTransaction = FreezeTransaction.getInstance();
            freezeTransaction.setVoucherNo(request.getVoucherNo());
            List<FreezeTransaction> queryTrnFreezeList = FreezeTransaction.select(freezeTransaction);
            if (queryTrnFreezeList == null || queryTrnFreezeList.isEmpty()) {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302030, 
                		new Object[] { }));
            }
             freezeTransaction = queryTrnFreezeList.get(0);
            //解冻金额校验
            if (request.getTranAmt().compareTo(freezeTransaction.getTranAmt().subtract(freezeTransaction.getThawAmt())) > 0) {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302031, 
                		new Object[] {request.getTranAmt(),
                		freezeTransaction.getTranAmt().subtract(freezeTransaction.getThawAmt())}));
            }
            //解冻冻结金额
            coreService.subtractMagFreezeBal(AccountType.BIZ,subAccCode, transaction.getTranAmt());
            //扣划记账处理
            buckDrawAccountDo(transaction,ReticketConstants.ENTRY_KEY_BUCKLE);
            //更新冻结流水
            freezeTransaction.setThawAmt(freezeTransaction.getThawAmt().add(transaction.getTranAmt()));
            freezeTransaction.setModifyTime(DateUtil.getCurrentTimeStamp());
            freezeTransaction.update();

        } else {
            //检查商户流动资金账户余额是否足够出款？
            BigDecimal availableBal = basBizService.getAvailableBal(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + request.getPayerAccCode(),
                request.getPayerAccCode(), request.getCcyCode());
            //余额不足则失败
            if (availableBal.compareTo(request.getTranAmt()) < 0) {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_304001, 
                		new Object[] {request.getPayerAccCode()}));
            }
            //扣划记账处理
            buckDrawAccountDo(transaction,ReticketConstants.ENTRY_KEY_BUCKLE);
        }
        return transaction;

    }

    /*
     * 扣划记账处理
     */
    @Transactional
    private void buckDrawAccountDo(final Transaction transaction,String entryKeyBuckDraw) throws Exception {
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKeyBuckDraw, transaction.getAccDate(), true, new AccountCallBack() {

            @Override
            public TranDraw callBack() {
                TranDraw tranDraw = TranDraw.getInstance();
                tranDraw.setAccTranNo(transaction.getAccTranNo());
                tranDraw.setStatus(ReticketConstants.DRAW_STATUS_NOT);
                tranDraw.setCreateBy(ReticketConstants.DEFAULT_OPERATOR);
                tranDraw.setCreateTime(DateUtil.getCurrentTimeStamp());
                tranDraw.setModifyBy(tranDraw.getCreateBy());
                tranDraw.setModifyTime(tranDraw.getCreateTime());
                tranDraw.setSubmitTime(tranDraw.getCreateTime());
                tranDraw.save();
                return tranDraw;
            }
        });
    }

}
