package com.somnus.smart.biz.custom.resource.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.custom.common.CusConstants;
import com.somnus.smart.biz.custom.resource.ForeignExchangeResource;
import com.somnus.smart.biz.custom.service.ForeignExchangeService;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.custom.PurchaseExchangeRequest;
import com.somnus.smart.message.custom.PurchaseWriteOffRequest;
import com.somnus.smart.message.custom.SettlementExchangeRequest;
import com.somnus.smart.message.custom.SettlementWriteOffRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 结汇记账接口
 */
@Component
@Validated
public class ForeignExchangeResourceImpl implements ForeignExchangeResource {

    private transient Logger       log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BasBizService          basBizService;

    @Resource
    private ForeignExchangeService foreignExchangeService;

    @Override
    public AccountResponse settlementExchange(SettlementExchangeRequest request) {
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
            // 2、创建记账交易流水
            Transaction transaction = foreignExchangeService.createTransaction(request);
            // 3、交易流水、台账、明细账落地，余额更新
            foreignExchangeService.foreignExchangeSynAccount(transaction, transaction.getAccDate(), true, CusConstants.ENTRY_KEY_SETTLE_EXCHANGE);
            //5)返回结汇处理结果
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
    public AccountResponse settlementWriteOff(SettlementWriteOffRequest request) {
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
            // 2、创建记账交易流水
            Transaction transaction = foreignExchangeService.createTransaction(request);
            // 3、交易流水、台账、明细账落地，余额更新
            foreignExchangeService.foreignExchangeSynAccount(transaction, transaction.getAccDate(), true, CusConstants.ENTRY_KEY_SETTLE_WRITEOFF);
            // 4、返回结汇处理结果
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
    public AccountResponse purchaseExchange(PurchaseExchangeRequest request) {
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
            // 2、验证是否允许出款
            basBizService.checkAllowOut(request.getPayerType(), request.getPayerCode(), request.getPayerAccCode());
            //验证余额是否足够出款
            BigDecimal availableBal = basBizService.getAvailableBal(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + request.getPayerAccCode(),
                request.getPayerAccCode(), request.getCcyCode());
            if (availableBal.compareTo(request.getTranAmt()) < 0) {
                throw new BizException("账户余额不足");
            }
            // 2、创建记账交易流水
            Transaction transaction = foreignExchangeService.createTransaction(request);
            // 3、交易流水、台账、明细账落地，余额更新
            foreignExchangeService.foreignExchangeSynAccount(transaction, transaction.getAccDate(), true, CusConstants.ENTRY_KEY_PURCHASE_EXCHANGE);
            // 4、返回结汇处理结果
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
    public AccountResponse purchaseWriteOff(PurchaseWriteOffRequest request) {
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

            // 2、创建记账交易流水
            Transaction transaction = foreignExchangeService.createTransaction(request);
            // 3、交易流水、台账、明细账落地，余额更新
            foreignExchangeService.foreignExchangeSynAccount(transaction, transaction.getAccDate(), true, CusConstants.ENTRY_KEY_PURCHASE_WRITEOFF);
            // 4、返回结汇处理结果
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
