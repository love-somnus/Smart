package com.somnus.smart.biz.custom.resource.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.TrnFreeze;
import com.somnus.smart.biz.custom.common.ReticketConstants;
import com.somnus.smart.biz.custom.resource.FreezeResource;
import com.somnus.smart.biz.custom.service.FreezeService;
import com.somnus.smart.domain.account.FreezeTransaction;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.Message;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.custom.AccountFreezeRequest;
import com.somnus.smart.message.custom.TranFreezeRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 账户余额冻结、解冻接口
 */
@Component
@Validated
public class FreezeResourceImpl implements FreezeResource {

    private transient Logger     log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private FreezeService        freezeService;

    @Resource
    private BasBizService        basbizService;

    @Override
    public Message accountFreeze(AccountFreezeRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        Message repMsg = new Message();
        try {
            //冻结
            if (ReticketConstants.FREEZE_TRANTYPE_FREEZE.equals(request.getTranType())) {
                // 1、重复冻结交易
                List<FreezeTransaction> queryTrnFreezeList = FreezeTransaction.selectTrnFreezeList(request.getAppFreezeNo());
                if (queryTrnFreezeList != null && queryTrnFreezeList.size() > 0) {
                    log.info("重复冻结：{}", request.getAppFreezeNo());
                    MessageUtil.createCommMsg(repMsg);
                    log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                    return repMsg;
                }
                // 2、创建冻结流水
                FreezeTransaction freezeTransaction = freezeService.createFreezeTransaction(request);
                // 3、校验商户的流动资金户
                CusSubaccinfo cusSubaccinfo = basbizService.getCusSubaccinfo(AccountType.BIZ,
                    BasConstants.REL_SUB_CODE_FREE.concat(freezeTransaction.getMerAccCode()), freezeTransaction.getMerAccCode(),
                    freezeTransaction.getCcyCode());
                if (cusSubaccinfo == null) {
                    throw new BizException("流动资金账户为空！");
                }
                // 4、冻结处理
                freezeService.freezeDeal(freezeTransaction, cusSubaccinfo);
                // 5、返回处理结果
                MessageUtil.createCommMsg(repMsg);
            }
            //解冻
            else if (ReticketConstants.FREEZE_TRANTYPE_UNFREEZE.equals(request.getTranType())) {
                // 1、判断原冻结交易是否存在
                List<FreezeTransaction> queryTrnFreezeList = FreezeTransaction.selectTrnFreezeList(request.getAppFreezeNo());
                if (queryTrnFreezeList == null || queryTrnFreezeList.isEmpty()) {
                    throw new BizException("冻结交易不存在");
                }
                FreezeTransaction freezeTransaction = queryTrnFreezeList.get(0);
                // 2、已解冻，直接返回成功
                if (ReticketConstants.FREEZE_STATUS_UNFREEZE.equals(freezeTransaction.getStatus())) {
                    log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                    return repMsg;
                }
                // 3、校验解冻金额 原解冻金额+请求解冻金额==冻结金额
                if (freezeTransaction.getThawAmt().add(request.getTranAmt()).compareTo(freezeTransaction.getTranAmt()) != 0) {
                    throw new BizException("解冻金额不等于未解冻金额！");
                }
                // 4、校验商户的流动资金户
                CusSubaccinfo cusSubaccinfo = basbizService.getCusSubaccinfo(AccountType.BIZ,
                    ReticketConstants.REL_SUB_CODE_FREE.concat(freezeTransaction.getMerAccCode()), freezeTransaction.getMerAccCode(),
                    freezeTransaction.getCcyCode());
                if (cusSubaccinfo == null) {
                    throw new BizException("流动资金账户为空！");
                }
                // 5、解冻处理，根据解冻业务类型更新流动资金户可用余额、业务冻结余额、监管冻结余额
                freezeService.unFreezeDeal(freezeTransaction, cusSubaccinfo, request.getTranAmt());
                // 6、返回处理结果
                MessageUtil.createCommMsg(repMsg);
            }
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
    public Message tranFreeze(TranFreezeRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        AccountResponse repMsg = new AccountResponse();
        try {
            List<FreezeTransaction> queryTrnFreezeList = FreezeTransaction.selectTrnFreezeList(request.getAppFreezeNo());
            //交易冻结
            if (ReticketConstants.FREEZE_TRANTYPE_FREEZE.equals(request.getTranType())) {
                // 1、校验冻结交易
                if (queryTrnFreezeList != null && queryTrnFreezeList.size() > 0) {
                    TrnFreeze freeze = queryTrnFreezeList.get(0);
                    if (freeze.getStatus().equals(ReticketConstants.FREEZE_STATUS_UNFREEZE)) {
                        throw new BizException("交易已解冻，不能做冻结操作！");
                    } else if (freeze.getStatus().equals(ReticketConstants.FREEZE_STATUS_FREEZE)) {
                        log.warn("交易已冻结,请不要重复冻结！");
                        MessageUtil.createErrorMsg(repMsg);
                        log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                        return repMsg;
                    }
                }
                // 2、获取原交易
                Transaction transaction = Transaction.selectByAppTranNo(request.getAppTranNo());
                // 3、原交易校验
                freezeService.checkTransaction(transaction);
                // 4、创建冻结流水
                FreezeTransaction freezeTransaction = freezeService.createFreezeTransaction(request, transaction);
                // 5、交易冻结处理
                freezeService.tranFreezeDeal(freezeTransaction, transaction);
            }
            //解冻
            else if (ReticketConstants.FREEZE_TRANTYPE_UNFREEZE.equals(request.getTranType())) {
                // 1、校验冻结交易
                if (queryTrnFreezeList == null || queryTrnFreezeList.isEmpty()) {
                    throw new BizException("原冻结交易不存在");
                }
                FreezeTransaction freezeTransaction = queryTrnFreezeList.get(0);
                if (ReticketConstants.FREEZE_STATUS_UNFREEZE.equals(freezeTransaction.getStatus())) {
                    log.info("交易已解冻，直接返回解冻成功！");
                    MessageUtil.createCommMsg(repMsg);
                    log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                    return repMsg;
                }
                // 2、交易原交易
                Transaction transaction = Transaction.selectByAppTranNo(request.getAppTranNo());
                if (transaction == null) {
                    throw new BizException("被冻结交易不存在！");
                }
                if (!BasConstants.FROZEN_YES.equals(transaction.getFrozenFlag())) {
                    throw new BizException("该交易已解冻，请不要重复发起解结操作");
                }
                // 3、交易解冻处理
                freezeService.tranUnFreezeDeal(freezeTransaction, transaction);
            } else {
                throw new BizException("交易类型不正确！");
            }
            MessageUtil.createCommMsg(repMsg);
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
