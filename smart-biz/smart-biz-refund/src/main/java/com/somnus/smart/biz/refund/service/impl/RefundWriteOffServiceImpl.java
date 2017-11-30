package com.somnus.smart.biz.refund.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.biz.refund.common.RefundConstants;
import com.somnus.smart.biz.refund.service.RefundWriteOffService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.RefundTransaction;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.writeoff.RefundWriteOffRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

/**
 * 退款核销服务
 */
@Service
public class RefundWriteOffServiceImpl implements RefundWriteOffService {

    @Autowired
    private BasBizService     		basbizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Transactional
    @Override
    public void refundWriteOff(RefundWriteOffRequest request) {
        //退款成功核销
        if (RefundConstants.WRITEOFF_TYPE_SUCCESS.equals(request.getWriteOffType())) {

        } else if (RefundConstants.WRITEOFF_TYPE_FAIL.equals(request.getWriteOffType())) {//退款撤销核销

        } else {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302057, new Object[] {}));
        }
    }
    @Transactional
    public void refundedWriteOffAccount(RefundWriteOffRequest request) throws Exception {
        final RefundTransaction refundTransaction = RefundTransaction.selectWriteOffConfirm(request.getRefundId());
        if (null == refundTransaction) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302057, 
            		new Object[] {request.getRefundId()}));
        }
        //获取entryKey
        String entryKey = getEntryKey(refundTransaction.getBizType(), refundTransaction.getRefType());
        //获取原交易
        Transaction oriTrnTransaction = Transaction.selectByAppTranNo(refundTransaction.getOriAppTranNo());
        if (oriTrnTransaction == null) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302057, 
            		new Object[] {request.getRefundId()}));
        }
        //创建退款核心交易流水
        Transaction transaction = createTransaction(refundTransaction, oriTrnTransaction);
        //退款核心记账
        refundWriteOffAccountDo(refundTransaction, entryKey, transaction);
    }

    @Override
    @Transactional
    public void refundCancelWriteOffAccount(RefundWriteOffRequest request, RefundTransaction refundTransaction) throws Exception {
        if (null == refundTransaction) {
            throw new BizException(request.getRefundId() + "对应的出款信息不存在或无法做核销");
        }
        Transaction oriTrnTransaction = Transaction.selectByAppTranNo(refundTransaction.getOriAppTranNo());
        if (oriTrnTransaction == null) {
            throw new BizException(request.getRefundId() + "原收单交易没找到,原交易流水号" + refundTransaction.getOriAppTranNo());
        }
        String entryKey = null;
        if (RefundConstants.REF_TYPE_ACCOUNT.equals(refundTransaction.getRefType())) {
            entryKey = RefundConstants.ENTRY_KEY_ACCOUNTREFUND_CANCEL;
        } else {
            entryKey = RefundConstants.ENTRY_KEY_ROUTEREFUND_CANCEL;
        }

        oriTrnTransaction.setModifyTime(DateUtil.getCurrentTimeStamp());
        oriTrnTransaction.setRefundedAmt(oriTrnTransaction.getRefundedAmt().subtract(refundTransaction.getTranAmount()));
        //如果已退款金额等于已拒付金额
        if (oriTrnTransaction.getRefundedAmt().add(oriTrnTransaction.getDishonoredAmt()).compareTo(BigDecimal.ZERO) == 0) {
            oriTrnTransaction.setStatus(RefundConstants.STATUS_NORMAL);
        } else {
            oriTrnTransaction.setStatus(RefundConstants.STATUS_REFUND_PART);
        }
        oriTrnTransaction.update();

        //创建退款核心交易流水
        Transaction transaction = createTransaction(refundTransaction, oriTrnTransaction);
        refundWriteOffAccountDo(refundTransaction, entryKey, transaction);
    }

    /**
     * 退款核心记账
     * 
     * @param refundTransaction
     * @param entryKey
     * @param transaction
     * @throws Exception
     */
    private void refundWriteOffAccountDo(final RefundTransaction refundTransaction, String entryKey, Transaction transaction) throws Exception {
        Account account = Account.getInstance();
        account.synAccountNoTransaction(transaction, entryKey, transaction.getAccDate(), false, new AccountCallBack() {

            @Override
            public RefundTransaction callBack() {
                refundTransaction.setFinishFlag(RefundConstants.FINISH_FLAG_TRUE);
                refundTransaction.setFinishDate(new Date());
                refundTransaction.setModifyTime(DateUtil.getCurrentTimeStamp());
                refundTransaction.update();
                return refundTransaction;
            }
        });
    }

    /**
     * 创建退款核心记账交易流水
     * 
     * @param trnDraw
     * @return
     */
    private Transaction createTransaction(RefundTransaction refundTransaction, Transaction oriTrnTransaction) {
        Transaction transaction = Transaction.getInstance();
        transaction.setAccTranNo(refundTransaction.getRefundId());
        transaction.setCcyCode(refundTransaction.getCcyCode());
        transaction.setAppTranDate(new Date());
        transaction.setIpsBillNo(refundTransaction.getBillNo());
        transaction.setAppTranNo(refundTransaction.getAppTranNo());
        transaction.setTranType(refundTransaction.getTranType());
        transaction.setPrdCode(refundTransaction.getPrdCode());
        transaction.setSysCode(refundTransaction.getSysCode());
        transaction.setCusTranNo(null);
        transaction.setTranRemark(refundTransaction.getRemark());
        transaction.setCreateTime(DateUtil.getCurrentTimeStamp());
        transaction.setCreateBy(refundTransaction.getModifyBy());
        transaction.setModifyTime(transaction.getCreateTime());
        transaction.setModifyBy(transaction.getCreateBy());
        transaction.setTranAmt(refundTransaction.getTranAmount());
        transaction.setOrdAmt(refundTransaction.getTranAmount());
        transaction.setFeeAmt(BigDecimal.ZERO);
        transaction.setSecurityDeposit(BigDecimal.ZERO);
        transaction.setBankCost(refundTransaction.getBankCost());
        transaction.setPayerCode(refundTransaction.getMerCode());
        transaction.setPayerAccCode(refundTransaction.getMerAccCode());
        transaction.setPayerType(refundTransaction.getMerBizKind());
        transaction.setBankAccCode(refundTransaction.getIpsAccount());
        transaction.setSupplierCode(refundTransaction.getPayerBankCode());
        transaction.setModifyBy(RefundConstants.DEFAULT_OPERATOR);
        transaction.setModifyTime(DateUtil.getCurrentTimeStamp());
        transaction.setAccDate(basbizService.getSystemAccDate());
        if (null != oriTrnTransaction) {
            transaction.setPrdCode(oriTrnTransaction.getPrdCode());
            transaction.setSysCode(oriTrnTransaction.getSysCode());
            transaction.setBankAccCode(oriTrnTransaction.getBankAccCode());
            transaction.setSupplierCode(oriTrnTransaction.getSupplierCode());
        }
        return transaction;
    }

    /**
     * 根据业务类型和退款类型获取entryKey
     * 
     * @param bizType
     * @param refType
     * @return
     */
    private String getEntryKey(String bizType, String refType) {
        String entryKey = null;
        //银行退款
        if (RefundConstants.BIZ_TYPE_BANKREFUND.equals(bizType)) {
            //入账退款
            if (RefundConstants.REF_TYPE_ACCOUNT.equals(refType)) {
                entryKey = RefundConstants.ENTRY_KEY_ACCOUNTREFUND_CONFIRM;
            } else {//未入账退款
                entryKey = RefundConstants.ENTRY_KEY_ROUTEREFUND_CONFIRM;
            }//自动退款
        } else if (RefundConstants.BIZ_TYPE_AUTOREFUND.equals(bizType)) {
            entryKey = RefundConstants.ENTRY_KEY_AUTOREFUND_CONFIRM;
        } else {
            throw new BizException(bizType + "BizType不正确");
        }
        return entryKey;
    }

}
