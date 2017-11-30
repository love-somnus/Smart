package com.somnus.smart.biz.draw.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.dao.TrnDrawDao;
import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.MerAccount;
import com.somnus.smart.base.domain.MerMerchant;
import com.somnus.smart.base.domain.PerAccount;
import com.somnus.smart.biz.draw.common.DrawTransfer;
import com.somnus.smart.biz.draw.service.RefuseService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.TranRefuse;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.refusepay.RefusePayRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.CoreService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

@Service
public class RefuseServiceImpl implements RefuseService {

    protected static Logger log = LoggerFactory.getLogger(RefuseServiceImpl.class);

    @Autowired
    private TrnDrawDao      		trnDrawDao;

    @Autowired
    private CoreService     		coreService;

    @Autowired
    private BasBizService   		basBizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    public void checkOriTransaction(Transaction oriTransaction, String refKind, BigDecimal tranAmt) {
        //原交易不能为已拒付
        if (BasConstants.STATUS_REFUSE.equals(oriTransaction.getStatus())) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302043, new Object[] {}));
        } else if (BasConstants.STATUS_REFUND_PART.equals(oriTransaction.getStatus()) && BasConstants.REF_KIND_ALL.equals(refKind)) {//原交易为部分拒付不能全额退款
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302044, new Object[] {}));
        }
        //原交易未入账，不允许部分拒付
        if (BasConstants.REF_KIND_PART.equals(refKind) && BasConstants.BLN_STATUS_NOTENTER.equals(oriTransaction.getBlnStatus())) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302045, new Object[] {}));
        }
        //原交易已退款，不能再次拒付
        if (BasConstants.STATUS_REFUND.equals(oriTransaction.getStatus())) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302046, new Object[] {}));
        }

        //原交易未入账，拒付金额大于原订单金额，不允许拒付
        if (BasConstants.BLN_STATUS_NOTENTER.equals(oriTransaction.getBlnStatus()) && BasConstants.FEE_FLAG_PAY.equals(oriTransaction.getFeeFlag())
            && tranAmt.compareTo(oriTransaction.getOrdAmt()) > 0) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302047, new Object[] {}));
        }
    }

    @Override
    public Transaction createTransaction(RefusePayRequest request, String supplierCode) {
        Transaction transaction = DrawTransfer.msgToTransaction(request);
        transaction.setSupplierCode(supplierCode);
        // 设置当前帐务日期
        transaction.setAccDate(basBizService.getSystemAccDate());
        // 设置记账流水号
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    @Transactional
    public void refusePaySynAccount(String refKind, BigDecimal tranAmt, final Transaction oriTransaction, final Transaction transaction,
                                    final TranRefuse tranRefuse, String payerAccCode, String ccyCode) throws Exception {

        //反向交易系统编号取原交易的系统编号
        transaction.setSysCode(oriTransaction.getSysCode());
        tranRefuse.setAccTranNo(transaction.getAccTranNo());
        oriTransaction.setDishonoredAmt(tranAmt.add(oriTransaction.getDishonoredAmt() == null ? BigDecimal.ZERO : oriTransaction.getDishonoredAmt()));
        if (BasConstants.REF_KIND_ALL.equals(refKind)) {
            oriTransaction.setStatus(BasConstants.STATUS_REFUSE);
        } else if (oriTransaction.getDishonoredAmt().compareTo(oriTransaction.getTranAmt()) == 0) {
            oriTransaction.setStatus(BasConstants.STATUS_REFUSE);
        } else {
            oriTransaction.setStatus(BasConstants.STATUS_REFUND_PART);
        }
        oriTransaction.setModifyBy(transaction.getCreateBy());
        oriTransaction.setModifyTime(transaction.getCreateTime());
        //原交易已入账
        if (BasConstants.BLN_STATUS_ENTERED.equals(oriTransaction.getBlnStatus())) {
            enteredRefuse(refKind, tranAmt, oriTransaction, transaction, tranRefuse, payerAccCode, ccyCode);
        }
        //原始交易未入账
        else if (BasConstants.BLN_STATUS_NOTENTER.equals(oriTransaction.getBlnStatus())) {
            unEnterRefuse(oriTransaction, transaction, tranRefuse);
        } else {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302048, new Object[] {}));
        }
    }

    private void unEnterRefuse(final Transaction oriTransaction, final Transaction transaction, final TranRefuse tranRefuse) throws Exception {
        String entryKey;
        tranRefuse.setStatus(DrawConstants.REFUSE_STATUS1);//1
        entryKey = DrawConstants.ENTRY_KEY_REFUSEPAY_ROUTE_PRE + oriTransaction.getFeeFlag() + oriTransaction.getFeeStlMode();
        // 收款方日结需要付手续费
        if (BasConstants.FEE_FLAG_REC.equals(oriTransaction.getFeeFlag()) && BasConstants.FEE_STL_MODE_TRAN.equals(oriTransaction.getFeeStlMode())) {
            transaction.setFeeFlag(BasConstants.FEE_FLAG_PAY);
            transaction.setFeeStlMode(oriTransaction.getFeeStlMode());
            transaction.setFeeAmt(oriTransaction.getFeeAmt());
        }
        refuseAccountDo(oriTransaction, transaction, tranRefuse, entryKey);
    }

    private void enteredRefuse(String refKind, BigDecimal tranAmt, final Transaction oriTransaction, final Transaction transaction,
                               final TranRefuse tranRefuse, String payerAccCode, String ccyCode) throws Exception {
        String subAccCode;
        String entryKey;
        if (BasConstants.IS_DEPOSIT_TRUE.equals(oriTransaction.getIsDeposit())) {
            if (!BasConstants.DEPOSIT_STATUS_BACK.equals(oriTransaction.getDepositStatus())) {
                BigDecimal depositAmt = null;
                if (BasConstants.REF_KIND_ALL.equals(refKind)) {
                    depositAmt = oriTransaction.getSecurityDeposit();
                } else {
                    depositAmt = calcDeposit(oriTransaction.getOrdAmt(), oriTransaction.getSecurityDeposit(), tranAmt);
                }
                BigDecimal returnedDepositAmt = oriTransaction.getReturnedDepositAmt() == null ? BigDecimal.ZERO : oriTransaction
                    .getReturnedDepositAmt();
                oriTransaction.setReturnedDepositAmt(depositAmt.add(returnedDepositAmt));
                if (oriTransaction.getReturnedDepositAmt().compareTo(oriTransaction.getSecurityDeposit()) >= 0) {
                    oriTransaction.setReturnedDepositAmt(oriTransaction.getSecurityDeposit());
                    depositAmt = oriTransaction.getSecurityDeposit().subtract(returnedDepositAmt);
                    oriTransaction.setDepositStatus(BasConstants.DEPOSIT_STATUS_BACK);
                    oriTransaction.setDepositRefundDate(new Date());
                }
                transaction.setSecurityDeposit(depositAmt);
                transaction.setReturnedDepositAmt(depositAmt);
                transaction.setIsDeposit(BasConstants.IS_DEPOSIT_FALSE);
            } else {
                transaction.setIsDeposit(BasConstants.IS_DEPOSIT_FALSE);
                transaction.setReturnedDepositAmt(BigDecimal.ZERO);
            }
        }

        // 检查商户信息是否存在
        CusSubaccinfo cusSubaccinfo = basBizService.getCusSubaccinfo(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + payerAccCode, payerAccCode, ccyCode);
        if (cusSubaccinfo == null) {
            log.error("relSubCode:" + BasConstants.REL_SUB_CODE_FREE + payerAccCode + " CcyCode:" + ccyCode);
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302041, new Object[] {}));
        }
        subAccCode = cusSubaccinfo.getSubAccCode();
        BigDecimal availBal = cusSubaccinfo.getCurBal().subtract(cusSubaccinfo.getBizFreezeBal()).subtract(cusSubaccinfo.getMagFreezeBal())
            .subtract(cusSubaccinfo.getPayableFreezeBal());
        BigDecimal canRefuseBal = availBal.add(transaction.getReturnedDepositAmt());
        //可拒付余额小于交易请求拒付金额
        if (canRefuseBal.compareTo(tranAmt) < 0) {
            tranRefuse.setStatus(DrawConstants.REFUSE_STATUS0);//0
            transaction.save();
            oriTransaction.update();
            coreService.addBizFreezeBal(AccountType.BIZ,subAccCode, tranAmt);
        } else {
            tranRefuse.setStatus(DrawConstants.REFUSE_STATUS1);//1
            //入账退款
            entryKey = DrawConstants.ENTRY_KEY_REFUSEPAY_ACCOUNT;
            refuseAccountDo(oriTransaction, transaction, tranRefuse, entryKey);
        }
    }

    @Transactional
    private void refuseAccountDo(final Transaction oriTransaction, 
    		final Transaction transaction, final TranRefuse tranRefuse, String entryKey) throws Exception {
        Account account = Account.getInstance();
        account.synAccount(transaction, entryKey, transaction.getAccDate(), true, new AccountCallBack() {

            @Override
            public Object callBack() throws Exception {
                //拒付附属表落地
                tranRefuse.save();
                DrawTransaction drawTransaction = createDrawTransaction(transaction, tranRefuse);
                if (BasConstants.BIZ_KIND_CUS.equals(drawTransaction.getMerBizKind())) {
                    MerMerchant merMerchant = basBizService.getMerMerchant(drawTransaction.getMerCode());
                    if (null != merMerchant) {
                        drawTransaction.setMerName(merMerchant.getMerName());
                    }
                    MerAccount merAccount = basBizService.getMerAccount(drawTransaction.getMerCode(), drawTransaction.getMerAccCode());
                    if (null != merAccount) {
                        drawTransaction.setMerAccName(merAccount.getAcctName());
                    }
                } else {
                    PerAccount perAccount = basBizService.getPerAccount(transaction.getPayerAccCode());
                    if (null != perAccount) {
                        drawTransaction.setMerName(perAccount.getAcctName());
                        drawTransaction.setMerAccName(perAccount.getAcctName());
                    }
                }
                //出款流水落地
                trnDrawDao.insertInSeq(drawTransaction);
                oriTransaction.update();
                return drawTransaction;
            }
        });
    }

    @Override
    public TranRefuse createTranRefuse(RefusePayRequest request) {
        TranRefuse tranRefuse = DrawTransfer.msgToTranRefuse(request);
        return tranRefuse;
    }

    private DrawTransaction createDrawTransaction(Transaction trntransaction, TranRefuse tranRefuse) {
        DrawTransaction drawTransaction = DrawTransaction.getInstance();
        drawTransaction.setAppBatchNo(trntransaction.getAppBatchNo());
        drawTransaction.setAppTranNo(trntransaction.getAppTranNo());
        drawTransaction.setSupplierBillNo(null);
        drawTransaction.setPrdCode(trntransaction.getPrdCode());
        drawTransaction.setMerCode(trntransaction.getPayerCode());
        drawTransaction.setMerAccCode(trntransaction.getPayerAccCode());
        drawTransaction.setMerBizKind(trntransaction.getPayerType());
        drawTransaction.setCcyCode(trntransaction.getCcyCode());
        drawTransaction.setTranAmount(trntransaction.getOrdAmt());
        drawTransaction.setChannelCode(trntransaction.getChannelCode());
        drawTransaction.setChannelName(null);
        drawTransaction.setPayeeBankCode(trntransaction.getSupplierCode());
        drawTransaction.setPayerBankCode(null);
        drawTransaction.setPayerBraBankCode(null);
        drawTransaction.setPayerBraBankName(null);
        drawTransaction.setIpsAccount(null);
        drawTransaction.setIsOnline(DrawConstants.IS_ONLINE_TRUE);
        drawTransaction.setErrorMessage(null);
        drawTransaction.setTranType(trntransaction.getTranType());
        drawTransaction.setFileId(null);
        // 出款状态 00 待处理
        drawTransaction.setStatus("00");
        drawTransaction.setRemark(null);
        drawTransaction.setBizType(DrawConstants.DRAW_BIZ_TYPE_REFUSE);//拒付
        drawTransaction.setPriNo(BasConstants.DRAW_PRI_NO_DEFAULT);
        drawTransaction.setTranDate(trntransaction.getAppTranDate());
        drawTransaction.setTabTime(null);
        drawTransaction.setSysCode(trntransaction.getSysCode());
        drawTransaction.setFinishFlag("0");
        drawTransaction.setFinishNo(null);
        drawTransaction.setFinishTime(null);
        drawTransaction.setCreateTime(DateUtil.getCurrentTimeStamp());
        drawTransaction.setCreateBy(DrawConstants.DEFAULT_OPERATOR);
        drawTransaction.setModifyTime(drawTransaction.getCreateTime());
        drawTransaction.setModifyBy(drawTransaction.getCreateBy());
        return drawTransaction;
    }

    private BigDecimal calcDeposit(BigDecimal ordAmt, BigDecimal oldDepositAmt, BigDecimal refundAmt) {
        return oldDepositAmt.multiply(refundAmt).divide(ordAmt, 2, RoundingMode.HALF_UP);
    }

}
