package com.somnus.smart.biz.refund.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.TrnTranReverse;
import com.somnus.smart.biz.refund.common.RefundConstants;
import com.somnus.smart.biz.refund.common.ReverseConstants;
import com.somnus.smart.biz.refund.service.ReverseService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.TranReverse;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.ReverseRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.CoreService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 冲正服务实现
 */
@Service
public class ReverseServiceImpl implements ReverseService {

    protected static Logger 		log = LoggerFactory.getLogger(ReverseServiceImpl.class);
    /** coreService */
    @Autowired
    private CoreService     		coreService;
    /** basBizService */
    @Autowired
    private BasBizService   		basBizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    @Transactional
    public TrnTranReverse reverseSynAccount(ReverseRequest request, Transaction oriTransaction, Transaction trntransaction) throws Exception {
        //创建冲正记账附属流水
        TranReverse tranReverse = TranReverse.getInstance();
        tranReverse.setAccTranNo(trntransaction.getAccTranNo());
        tranReverse.setCreateBy(trntransaction.getCreateBy());
        tranReverse.setCreateTime(trntransaction.getCreateTime());

        //入账冲正
        if (BasConstants.BLN_STATUS_ENTERED.equals(oriTransaction.getBlnStatus())) {
            recordedReverse(request, oriTransaction, trntransaction, tranReverse);
            return tranReverse;
        }

        //未入账冲正
        else if (BasConstants.BLN_STATUS_NOTENTER.equals(oriTransaction.getBlnStatus())) {
            tranReverse.setStatus(ReverseConstants.REVERSE_STATUS_REVERSEED);
            //获取记账分录key
            String entryKey = ReverseConstants.ENTRY_KEY_NOT_ENTERED_RESERVE;
            reverseAccount(oriTransaction, trntransaction, tranReverse, entryKey);
            return tranReverse;
        } else {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302054, new Object[] {}));
        }
    }

    /**
     * 未入账冲正记账
     * 
     * @param oriTransaction
     * @param transaction
     * @param tranReverse
     * @throws Exception
     */
    private void reverseAccount(final Transaction oriTransaction, Transaction transaction, final TranReverse tranReverse, String entryKey) throws Exception {
        //收款方日结需要付手续费
        if (BasConstants.FEE_FLAG_REC.equals(oriTransaction.getFeeFlag()) && BasConstants.FEE_STL_MODE_TRAN.equals(oriTransaction.getFeeStlMode())) {
            transaction.setFeeAmt(oriTransaction.getFeeAmt());
        }
        //同步记账
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKey, transaction.getAccDate(), true, new AccountCallBack() {
        	//沖正记账回调
            @Override
            public Object callBack() {
                if (BasConstants.REVERSE_STATUS_CANCELED.equals(tranReverse.getStatus())
                    || BasConstants.REVERSE_STATUS_HANDING_CANCELED.equals(tranReverse.getStatus())) {
                    tranReverse.update();
                } else {
                    tranReverse.save();
                }
                //更新原交易状态为冲正
                oriTransaction.setStatus(BasConstants.STATUS_REVERSE);
                if(BasConstants.DEPOSIT_STATUS_GATHERED.equals(oriTransaction.getDepositStatus())){
                	oriTransaction.setDepositStatus(BasConstants.DEPOSIT_STATUS_BACK);
                }
                oriTransaction.update();
                //return tranReverse;
				return tranReverse;
            }
        });
    }

    /**
     * 入账冲正
     * 
     * @param request
     * @param oriTransaction
     * @param transaction
     * @param tranReverse
     * @throws Exception
     */
    private void recordedReverse(ReverseRequest request, Transaction oriTransaction, Transaction transaction, TranReverse tranReverse)
                                                                                                                                      throws Exception {
        String subAccCode;
        String entryKey = null;
        // 检查商户流动资金账户余额是否足够退款
        CusSubaccinfo cusSubaccinfo = basBizService.getCusSubaccinfo(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + request.getPayeeAccCode(),
            request.getPayeeAccCode(), request.getCcyCode());
        if (cusSubaccinfo == null) {
            log.error("relSubCode:" + BasConstants.REL_SUB_CODE_FREE + request.getPayeeAccCode() + " CcyCode:" + request.getCcyCode());
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302041, new Object[] {}));
        }
        subAccCode = cusSubaccinfo.getSubAccCode();
        BigDecimal availBal = cusSubaccinfo.getCurBal().subtract(cusSubaccinfo.getBizFreezeBal()).subtract(cusSubaccinfo.getMagFreezeBal())
            .subtract(cusSubaccinfo.getPayableFreezeBal());
        BigDecimal canRefundBal = new BigDecimal(0);
        if(oriTransaction.getFeeFlag().equals(BasConstants.FEE_FLAG_REC)){
        	canRefundBal=availBal.add(oriTransaction.getFeeAmt());
        }
        if(oriTransaction.getDepositStatus()!=null&&oriTransaction.getDepositStatus().equals(BasConstants.DEPOSIT_STATUS_GATHERED)){
        	canRefundBal=canRefundBal.add(oriTransaction.getDishonoredAmt());
        }

        //可用余额不足
        if (canRefundBal.compareTo(request.getTranAmt()) < 0) {
            //设置冲正记账附属流水状态为"受理中"
            tranReverse.setStatus(ReverseConstants.REVERSE_STATUS_HANDING);
            //落地冲正记账流水、落地冲正记账附属流水、更新原始记账流水，冻结冲正金额
            reverseFreezeAndInsertTran(subAccCode, transaction, tranReverse,oriTransaction);
            return;
        }
        //可用余额足够
        //检查是否允许出款
        boolean isDraw = basBizService.isAccAllowOut(BasConstants.BIZ_KIND_CUS, transaction.getPayeeCode(), transaction.getPayeeAccCode());
        if (!isDraw) {
            log.warn("商户" + transaction.getPayeeCode() + "出款关闭,无法冲正,交易信息");
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302055, 
            		new Object[] {transaction.getPayeeCode()}));
        }
        //设置冲正记账附属流水状态为"已冲正"
        tranReverse.setStatus(ReverseConstants.REVERSE_STATUS_REVERSEED);
        entryKey = ReverseConstants.ENTRY_KEY_ENTERED_DEPOSIT_RESERVE;

        //如果原交易是保证金交易
        if (BasConstants.IS_DEPOSIT_TRUE.equals(oriTransaction.getIsDeposit())) {

            //原交易如果沒有缴纳保证金,直接抛异常返回记账失败
            if (BasConstants.DEPOSIT_STATUS_NOTGATHER.equals(oriTransaction.getDepositStatus())) {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302056, new Object[] {}));
            }
            //原交易已经缴纳保证金
            else if (BasConstants.DEPOSIT_STATUS_GATHERED.equals(oriTransaction.getDepositStatus())) {
                transaction.setSecurityDeposit(oriTransaction.getSecurityDeposit());
                transaction.setReturnedDepositAmt(oriTransaction.getSecurityDeposit());
                transaction.setIsDeposit(RefundConstants.IS_DEPOSIT_FALSE);
            }
        }
        //原交易为非保证金交易
        else {
            transaction.setIsDeposit(RefundConstants.IS_DEPOSIT_FALSE);
            transaction.setReturnedDepositAmt(BigDecimal.ZERO);
        }

        //冲正记账
        reverseAccount(oriTransaction, transaction, tranReverse, entryKey);
    }

    /**
     * 冲正记账可用余额不足，冻结余额，落地冲正记账流水和冲正记账附属流水
     * 
     * @param subAccCode
     * @param transaction
     * @param tranReserve
     */
    private void reverseFreezeAndInsertTran(String subAccCode, Transaction transaction, TranReverse tranReserve,Transaction oriTransaction) {
        //落地冲正记账流水和冲正记账附属流水
        transaction.save();
        tranReserve.save();
        //冻结可用余额
        coreService.addBizFreezeBal(AccountType.BIZ,subAccCode, transaction.getTranAmt());
        //更新原交易状态为冲正
        oriTransaction.setStatus(BasConstants.STATUS_REVERSE);
        oriTransaction.update();
    }
}
