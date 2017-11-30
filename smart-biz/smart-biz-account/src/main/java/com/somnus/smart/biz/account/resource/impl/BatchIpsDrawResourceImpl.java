package com.somnus.smart.biz.account.resource.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.biz.account.resource.BatchIpsDrawResource;
import com.somnus.smart.biz.account.service.BatchIpsDrawService;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.account.BatchIpsDrawRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.AccountConstants;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 下发到isp账户记账
 */
@Component
@Validated
@Path("/com.somnus.smart.biz.account.resource.BatchIpsDrawResource")
public class BatchIpsDrawResourceImpl implements BatchIpsDrawResource {

    protected static Logger     log = LoggerFactory.getLogger(BatchIpsDrawResourceImpl.class);

    @Resource
    private BasBizService       basBizService;

    @Resource
    private BatchIpsDrawService batchIpsDrawService;

    @Path("/batchIpsDraw")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public AccountResponse batchIpsDraw(BatchIpsDrawRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、记账请求参数校验
            batchIpsDrawService.checkAccountRequest(request);
            // 2、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(request.getAppBatchNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            //  3、校验是否允许出款
            basBizService.checkAllowOut(request.getPayerType(), request.getPayerCode(), request.getPayerAccCode());
            //  4、验证付款方余额是否足够
            BigDecimal availableBal = basBizService.getAvailableBal(AccountType.BIZ,AccountConstants.REL_SUB_CODE_FREE + request.getPayerAccCode(),
                request.getPayerAccCode(), request.getCcyCode());
            if (availableBal.compareTo(request.getBatchAmt()) < 0) {
                throw new BizException(request.getPayerAccCode() + "账户余额不足");
            }
            // 5、创建记账交易流水
            Transaction transaction = batchIpsDrawService.createTransaction(request);

            batchIpsDrawService.batchIpsDrawSynAccount(request, transaction);

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
