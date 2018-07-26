package com.somnus.smart.biz.custom.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.biz.custom.common.ReticketConstants;
import com.somnus.smart.biz.custom.resource.BuckleDrawResource;
import com.somnus.smart.biz.custom.service.BuckleDrawService;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.custom.BuckleDrawRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;


/**
 * 扣划记账接口
 */
@Component
@Validated
public class BuckleDrawResourceImpl implements BuckleDrawResource {

    private transient Logger  log = LoggerFactory.getLogger(this.getClass());

    /** 核销接口 */
    @Resource
    private BuckleDrawService buckleDrawService;

    @Resource
    private BasBizService     basbizService;

    /**
	 * 扣划（流动资金->扣划总户）
     * 
     * @param request
     * @return
     */
    @Override
    public AccountResponse buckleDraw(BuckleDrawRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、重复记账校验
            Transaction queryTransaction = Transaction.selectByAppTranNo(request.getAppTranNo());
            if (queryTransaction != null) {
                log.info("重复记账：{}", request.getAppTranNo());
                MessageUtil.setRepMsg(repMsg, queryTransaction);
                return repMsg;
            }

            // 2、创建记账交易流水
            Transaction transaction = buckleDrawService.createTransaction(request);

            // 3、校验商户的流动资金户
            CusSubaccinfo cusSubaccinfo = basbizService.getCusSubaccinfo(AccountType.BIZ,ReticketConstants.REL_SUB_CODE_FREE.concat(transaction.getPayerAccCode()),
                transaction.getPayerAccCode(), transaction.getCcyCode());
            if (cusSubaccinfo == null) {
                throw new BizException("流动资金账户为空！");
            }
            // 4、扣划记账
            buckleDrawService.buckDrawSynAccount(transaction,request,cusSubaccinfo.getSubAccCode());
            
            MessageUtil.setRepMsg(repMsg, queryTransaction);
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
