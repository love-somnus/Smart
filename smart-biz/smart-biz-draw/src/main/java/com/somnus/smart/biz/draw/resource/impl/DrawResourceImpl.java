package com.somnus.smart.biz.draw.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.biz.draw.resource.DrawResource;
import com.somnus.smart.biz.draw.service.DrawService;
import com.somnus.smart.domain.account.AccEntryCfg;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.TranDraw;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.account.BankDrawRequest;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 提现记账接口
 */
@Component
@Validated
public class DrawResourceImpl implements DrawResource {

    protected static Logger log = LoggerFactory.getLogger(DrawResourceImpl.class);
    /** drawService */
    @Resource
    private DrawService     drawService;

    @Override
    public AccountResponse bankDraw(BankDrawRequest request) {
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

            // 2、数据合法性校验
            drawService.checkMerCanDraw(request.getPayerType(), request.getPayerCode(), request.getPayerAccCode(), request.getCcyCode(),
                request.getOrdAmt(),request.getFeeWay(),request.getThdAccCode());

            // 3、创建创建出款记账流水
            Transaction transaction = drawService.createTransaction(request);

            // 4、创建出款附属流水
            TranDraw tranDraw = drawService.createTranDraw(request, transaction.getAccTranNo());

            // 5、创建出款交易流水
            DrawTransaction drawTransaction = drawService.createDrawTransaction(transaction, tranDraw);

            // 6、根据tranCode、手续费承担方、手续费结算模式获取entryKey
            String entryKey = AccEntryCfg.getEntryKeyByTranCode(request.getTranCode(),transaction.getFeeFlag(), transaction.getFeeStlMode(),null);
            
            // 7、同步记账
            drawService.drawSynAccount(transaction, entryKey, transaction.getAccDate(), true, tranDraw, drawTransaction);

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
