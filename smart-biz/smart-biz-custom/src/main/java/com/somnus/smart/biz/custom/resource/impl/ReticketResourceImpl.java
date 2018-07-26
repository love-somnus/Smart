package com.somnus.smart.biz.custom.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.custom.common.CusConstants;
import com.somnus.smart.biz.custom.resource.ReticketResource;
import com.somnus.smart.biz.custom.service.ReticketService;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.reticket.ReticketRequest;
import com.somnus.smart.message.reticket.ReticketResponse;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

@Component
@Validated
public class ReticketResourceImpl implements ReticketResource {

    private transient Logger      log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ReticketService       reticketService;

    @Autowired
    private MessageSourceAccessor msa;

    @Override
    public ReticketResponse returnTicket(ReticketRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        ReticketResponse repMsg = new ReticketResponse();
        try {
            // 1、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(request.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            String tranCode = request.getTranCode();//交易类型
            String oriAppTranNo = request.getOriAppTranNo();//原交易流水号
            // 2、创建记账交易流水
            Transaction trntransaction = reticketService.createTransaction(request);

            // 3、出款退票同步记账
            if (tranCode.equals("1209")) {
                reticketService.drawReticketSynAccount(trntransaction, oriAppTranNo);
            }
            // 3、重复收款退款退票同步记账
            else if (tranCode.equals("6101")) {
                reticketService.refundReticketSysAccount(trntransaction, oriAppTranNo, CusConstants.ENTRY_KEY_RETICKET2);
            }
            // 3、退款退票同步记账
            else if (tranCode.equals("1809")) {
                reticketService.refundReticketSysAccount(trntransaction, oriAppTranNo, CusConstants.ENTRY_KEY_RETICKET6);
            } else {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_301008, new Object[] { "交易类型" }));
            }
            MessageUtil.setRepMsg(repMsg, trntransaction);
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
