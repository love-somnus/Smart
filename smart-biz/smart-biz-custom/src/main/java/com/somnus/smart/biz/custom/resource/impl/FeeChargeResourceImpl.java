package com.somnus.smart.biz.custom.resource.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.biz.custom.common.CusConstants;
import com.somnus.smart.biz.custom.resource.FeeChargeResource;
import com.somnus.smart.biz.custom.service.FeeChargeService;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.custom.FeeChargeRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 费用收取记账接口
 */
@Component
@Validated
public class FeeChargeResourceImpl implements FeeChargeResource {

    @Autowired
    private MessageSourceAccessor msa;

    private transient Logger      log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BasBizService         basBizService;

    @Resource
    private FeeChargeService      feeChargeService;

    @Override
    public AccountResponse charge(FeeChargeRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(request.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            // 2、校验商户是否有足够的余额出款
            feeChargeService.checkMerAllowOut(request.getPayerType(), request.getPayerCode(), request.getPayerAccCode(), request.getCcyCode(),
                request.getTranAmt());
            // 3、创建收费记账交易流水
            Transaction transaction = feeChargeService.createTransaction(request);

            // 4、收费同步记账
            feeChargeService.feeChargeSynAccount(transaction, transaction.getAccDate(), true, CusConstants.ENTRY_KEY_FEE_CHARGE);
            // 5、返回费用收取处理结果
            MessageUtil.setRepMsg(repMsg, transaction);
        } catch (BizException e) {
            log.error(Constants.BUSINESS_ERROR, e);
            // 组织错误报文
            MessageUtil.errRetrunInAction(repMsg, e);
        } catch (Exception ex) {
            log.error(Constants.EXCEPTION_ERROR, ex);
            // 组织错误报文
            MessageUtil.createErrorMsg(repMsg);
        }
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
        return repMsg;
    }

    @Override
    public AccountResponse unFeeCharge(FeeChargeRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(request.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            // 2、获取付款方功能账户
            CusSubaccinfo cusSubaccinfo = basBizService.getCusSubaccinfo(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + request.getPayerAccCode(),
                request.getPayerAccCode(), request.getCcyCode());
            if (cusSubaccinfo == null) {
                log.error("relSubCode:{}, merAccCode:{}, ccyCode:{}", new Object[] { BasConstants.REL_SUB_CODE_FREE + request.getPayerAccCode(),
                        request.getPayerAccCode(), request.getCcyCode() });
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302001, new Object[] { request.getPayerAccCode() + "商户余额信息" }));
            }
            // 3、创建记账交易流水
            Transaction transaction = feeChargeService.createTransaction(request);
            // 4、计算可用余额 可用余额=当前余额-冻结余额-监管冻结余额
            BigDecimal availableBal = cusSubaccinfo.getCurBal().subtract(cusSubaccinfo.getBizFreezeBal()).subtract(cusSubaccinfo.getMagFreezeBal())
                .subtract(cusSubaccinfo.getPayableFreezeBal());
            // 5、如果余额不足做超额冻结，交易流水、收费附属流水落地，返回处理结果。
            if (availableBal.compareTo(request.getTranAmt()) < 0) {
                TrnTransaction trnTransaction = feeChargeService.totalFeeChargeDeal(transaction, cusSubaccinfo.getSubAccCode());
                // 返回费用收取处理结果
                MessageUtil.setRepMsg(repMsg, trnTransaction);
                return repMsg;
            }
            // 6、收费同步记账
            feeChargeService.unFeeChargeSynAccount(transaction, transaction.getAccDate(), true, CusConstants.ENTRY_KEY_UNFEE_CHARGE);
            // 7、返回费用收取处理结果
            MessageUtil.setRepMsg(repMsg, transaction);
        } catch (BizException e) {
            log.error(Constants.BUSINESS_ERROR, e);
            // 组织错误报文
            MessageUtil.errRetrunInAction(repMsg, e);
        } catch (Exception ex) {
            log.error(Constants.EXCEPTION_ERROR, ex);
            // 组织错误报文
            MessageUtil.createErrorMsg(repMsg);
        }
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
        return repMsg;
    }

}
