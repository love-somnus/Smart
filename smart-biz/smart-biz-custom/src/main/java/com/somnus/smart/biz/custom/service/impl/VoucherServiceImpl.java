package com.somnus.smart.biz.custom.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.dao.CusSubAccInfoDao;
import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.TrnLedgerDetail;
import com.somnus.smart.base.domain.TrnTranVoucher;
import com.somnus.smart.biz.custom.common.VoucherConstant;
import com.somnus.smart.biz.custom.common.VoucherTransfer;
import com.somnus.smart.biz.custom.service.VoucherService;
import com.somnus.smart.domain.account.AccEntryCfg;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.LedgerDetail;
import com.somnus.smart.domain.account.TranVoucher;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.voucher.VoucherEnterRequest;
import com.somnus.smart.message.voucher.VoucherEnterRequest.Voucher;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.CusSubAccInfoUtil;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.ValidateUtil;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private BasBizService         basBizService;


    @Autowired
    private CusSubAccInfoDao      cusSubAccInfoDao;


    @Transactional
    @Override
    public void voucherEnterAccount(Transaction transaction, TranVoucher tranVoucher) throws Exception {
        //落地记账交易流水
        transaction.save();
        //落地凭证附属流水
        tranVoucher.save();
        //创建台账
        LedgerDetail ledgerDetail = createLedgerDetail(transaction, tranVoucher);
        //落地台账
        ledgerDetail.save();
        Account account=Account.getInstance();
        //分户明细账创建和更新余额
        account.addAccount(true, ledgerDetail);
    }

    private LedgerDetail createLedgerDetail(Transaction transaction, TranVoucher tranVoucher) {
        LedgerDetail ledgerDetail = LedgerDetail.getInstance();
        AccEntryCfg accEntryCfg = AccEntryCfg.getInstance();
        accEntryCfg.setAccEntryCode("9900000001");
        accEntryCfg.setAccEntryType("0");
        ledgerDetail.dealCommon(transaction, accEntryCfg, transaction.getAccDate(), ledgerDetail);
        dealFirst(tranVoucher, ledgerDetail);
        dealSecond(tranVoucher, ledgerDetail);
        dealThird(tranVoucher, ledgerDetail);
        dealFouth(tranVoucher, ledgerDetail);
        ledgerDetail.checkBalance(ledgerDetail);
        return ledgerDetail;
    }


    private void dealFouth(TrnTranVoucher trnTranVoucher, TrnLedgerDetail trnLedgerDetail) {
        if (trnTranVoucher.getTranAmt4th() != null && (trnTranVoucher.getTranAmt4th().compareTo(new BigDecimal(0)) != 0)) {
            trnLedgerDetail.setRelSubCode4th(trnTranVoucher.getSubCode4th());
            trnLedgerDetail.setSubCode4th(trnTranVoucher.getSubCode4th().substring(0, 2));
            CusSubaccinfo cusSubAccInfo4th = cusSubAccInfoDao.selectBycode(CusSubAccInfoUtil.getTableBySubType(trnLedgerDetail.getSubType4th()),trnLedgerDetail.getRelSubCode4th(), null, trnLedgerDetail.getCcyCode());
            if (null == cusSubAccInfo4th) {
                throw new BizException("科目号4与币种对应的账户信息不存在，请填写正确的科目号4或币种");
            }
            trnLedgerDetail.setSubAccCode4th(cusSubAccInfo4th.getSubAccCode());
            trnLedgerDetail.setAccCode4th(cusSubAccInfo4th.getMerAccCode());
            if (trnLedgerDetail.getRelSubCode4th().startsWith("8")) {
                trnLedgerDetail.setSubType4th(VoucherConstant.SUB_TYPE_CUS);
            } else {
                trnLedgerDetail.setSubType4th(VoucherConstant.SUB_TYPE_INNER);
            }
            trnLedgerDetail.setSubIndex4th(new Short("4"));
            trnLedgerDetail.setCdFlag4th(trnTranVoucher.getCdFlag4th());
            trnLedgerDetail.setTranAmt4th(trnTranVoucher.getTranAmt4th());
        }
    }

    private void dealThird(TrnTranVoucher trnTranVoucher, TrnLedgerDetail trnLedgerDetail) {
        if (trnTranVoucher.getTranAmt3rd() != null && (trnTranVoucher.getTranAmt3rd().compareTo(new BigDecimal(0)) != 0)) {
            trnLedgerDetail.setRelSubCode3rd(trnTranVoucher.getSubCode3rd());
            trnLedgerDetail.setSubCode3rd(trnTranVoucher.getSubCode3rd().substring(0, 2));
            CusSubaccinfo cusSubAccInfo3rd = cusSubAccInfoDao.selectBycode(CusSubAccInfoUtil.getTableBySubType(trnLedgerDetail.getSubType3rd()),trnLedgerDetail.getRelSubCode3rd(), null, trnLedgerDetail.getCcyCode());
            if (null == cusSubAccInfo3rd) {
                throw new BizException("科目号3与币种对应的账户信息不存在，请填写正确的科目号3或币种");
            }
            trnLedgerDetail.setSubAccCode3rd(cusSubAccInfo3rd.getSubAccCode());
            trnLedgerDetail.setAccCode3rd(cusSubAccInfo3rd.getMerAccCode());
            if (trnLedgerDetail.getRelSubCode3rd().startsWith("8")) {
                trnLedgerDetail.setSubType3rd(VoucherConstant.SUB_TYPE_CUS);
            } else {
                trnLedgerDetail.setSubType3rd(VoucherConstant.SUB_TYPE_INNER);
            }
            trnLedgerDetail.setSubIndex3rd(new Short("3"));
            trnLedgerDetail.setCdFlag3rd(trnTranVoucher.getCdFlag3rd());
            trnLedgerDetail.setTranAmt3rd(trnTranVoucher.getTranAmt3rd());
        }
    }

    private void dealSecond(TrnTranVoucher trnTranVoucher, TrnLedgerDetail trnLedgerDetail) {
        trnLedgerDetail.setRelSubCode2nd(trnTranVoucher.getSubCode2nd());
        trnLedgerDetail.setSubCode2nd(trnTranVoucher.getSubCode2nd().substring(0, 2));
        CusSubaccinfo cusSubAccInfo2nd = cusSubAccInfoDao.selectBycode(CusSubAccInfoUtil.getTableBySubType(trnLedgerDetail.getSubType2nd()),trnLedgerDetail.getRelSubCode2nd(), null, trnLedgerDetail.getCcyCode());
        if (null == cusSubAccInfo2nd) {
            throw new BizException("科目号2与币种对应的账户信息不存在，请填写正确的科目号2或币种");
        }
        trnLedgerDetail.setSubAccCode2nd(cusSubAccInfo2nd.getSubAccCode());
        trnLedgerDetail.setAccCode2nd(cusSubAccInfo2nd.getMerAccCode());
        if (trnLedgerDetail.getRelSubCode2nd().startsWith("8")) {
            trnLedgerDetail.setSubType2nd(VoucherConstant.SUB_TYPE_CUS);
        } else {
            trnLedgerDetail.setSubType2nd(VoucherConstant.SUB_TYPE_INNER);
        }
        trnLedgerDetail.setSubIndex2nd(new Short("2"));
        trnLedgerDetail.setCdFlag2nd(trnTranVoucher.getCdFlag2nd());
        trnLedgerDetail.setTranAmt2nd(trnTranVoucher.getTranAmt2nd());
    }

    private void dealFirst(TrnTranVoucher trnTranVoucher, TrnLedgerDetail trnLedgerDetail) {
        trnLedgerDetail.setRelSubCode1st(trnTranVoucher.getSubCode1st());
        trnLedgerDetail.setFirSubCode1st(trnTranVoucher.getSubCode1st().substring(0, 2));
        if (trnLedgerDetail.getRelSubCode1st().startsWith("8")) {
            trnLedgerDetail.setSubType1st(VoucherConstant.SUB_TYPE_CUS);
        } else {
            trnLedgerDetail.setSubType1st(VoucherConstant.SUB_TYPE_INNER);
        }
        CusSubaccinfo cusSubAccInfo1st = cusSubAccInfoDao.selectBycode(CusSubAccInfoUtil.getTableBySubType(trnLedgerDetail.getSubType1st()),trnLedgerDetail.getRelSubCode1st(), null, trnLedgerDetail.getCcyCode());
        if (null == cusSubAccInfo1st) {
            throw new BizException("科目号1与币种对应的账户信息不存在，请填写正确的科目号1或币种");
        }
        trnLedgerDetail.setSubAccCode1st(cusSubAccInfo1st.getSubAccCode());
        trnLedgerDetail.setAccCode1st(cusSubAccInfo1st.getMerAccCode());
        
        trnLedgerDetail.setSubIndex1st(new Short("1"));
        trnLedgerDetail.setCdFlag1st(trnTranVoucher.getCdFlag1st());
        trnLedgerDetail.setTranAmt1st(trnTranVoucher.getTranAmt1st());
    }

    @Override
    public void checkRquest(Voucher voucher) {
        if (voucher.getTranAmt1st() == null || (voucher.getTranAmt1st().compareTo(new BigDecimal(0)) == 0)) {
            throw new BizException("金额1不可为空或等于0");
        }
        if (ValidateUtil.isEmpty(voucher.getCdFlag1st())) {
            throw new BizException("借贷方向1不可为空");
        } else if (!VoucherConstant.CD_FLAG_C.equals(voucher.getCdFlag1st()) && !VoucherConstant.CD_FLAG_D.equals(voucher.getCdFlag1st())) {
            throw new BizException("借贷方向1应当为C或D");
        }
        if (ValidateUtil.isEmpty(voucher.getSubCode1st())) {
            throw new BizException("科目号1不能为空");
        } else if (voucher.getSubCode1st().length() < 2) {
            throw new BizException("科目号1应当大于2位");
        }

        if (voucher.getTranAmt2nd() == null || (voucher.getTranAmt2nd().compareTo(new BigDecimal(0)) == 0)) {
            throw new BizException("金额2不可为空或等于0");
        }
        if (ValidateUtil.isEmpty(voucher.getCdFlag2nd())) {
            throw new BizException("借贷方向2不可为空");
        } else if (!VoucherConstant.CD_FLAG_C.equals(voucher.getCdFlag2nd()) && !VoucherConstant.CD_FLAG_D.equals(voucher.getCdFlag2nd())) {
            throw new BizException("借贷方向2应当为C或D");
        }
        if (ValidateUtil.isEmpty(voucher.getSubCode2nd())) {
            throw new BizException("科目号2不能为空");
        } else if (voucher.getSubCode2nd().length() < 2) {
            throw new BizException("科目号2应当大于2位");
        }

        if (voucher.getTranAmt3rd() != null && (voucher.getTranAmt3rd().compareTo(new BigDecimal(0)) != 0)) {
            if (ValidateUtil.isEmpty(voucher.getSubCode3rd())) {
                throw new BizException("科目号3不能为空");
            } else if (voucher.getSubCode3rd().length() < 2) {
                throw new BizException("科目号3应当大于2位");
            }
            if (ValidateUtil.isEmpty(voucher.getCdFlag3rd())) {
                throw new BizException("借贷方向3不可为空");
            } else if (!VoucherConstant.CD_FLAG_C.equals(voucher.getCdFlag3rd()) && !VoucherConstant.CD_FLAG_D.equals(voucher.getCdFlag3rd())) {
                throw new BizException("借贷方向3应当为C或D");
            }
        }
        if (voucher.getTranAmt4th() != null && (voucher.getTranAmt4th().compareTo(new BigDecimal(0)) != 0)) {
            if (ValidateUtil.isEmpty(voucher.getSubCode4th())) {
                throw new BizException("科目号4不能为空");
            } else if (voucher.getSubCode4th().length() < 2) {
                throw new BizException("科目号4应当大于2位");
            }
            if (ValidateUtil.isEmpty(voucher.getCdFlag4th())) {
                throw new BizException("借贷方向4不可为空");
            } else if (!VoucherConstant.CD_FLAG_C.equals(voucher.getCdFlag4th()) && !VoucherConstant.CD_FLAG_D.equals(voucher.getCdFlag4th())) {
                throw new BizException("借贷方向4应当为C或D");
            }
        }
    }

    @Override
    public Transaction createTransaction(Voucher voucher, VoucherEnterRequest request) {
        Transaction transaction = VoucherTransfer.msgToTransaction(voucher, request);
        transaction.setTranAmt(new BigDecimal(0));
        transaction.setOrdAmt(new BigDecimal(0));
        transaction.setAccTranNo(basBizService.getAccTranNo());
        transaction.setAccDate(basBizService.getSystemAccDate());
        return transaction;
    }

    @Override
    public TranVoucher createTranVoucher(Voucher voucher) {
        TranVoucher tranVoucher = VoucherTransfer.msgToTranVoucher(voucher);
        return tranVoucher;
    }

}
