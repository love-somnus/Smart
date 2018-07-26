package com.somnus.smart.biz.draw.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.biz.draw.resource.RefusePayResource;
import com.somnus.smart.biz.draw.service.RefuseService;
import com.somnus.smart.domain.account.TranRefuse;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.refusepay.RefusePayRequest;
import com.somnus.smart.message.refusepay.RefusePayResponse;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 拒付记账接口
 */
@Component
@Validated
public class RefusePayResourceImpl implements RefusePayResource {

    protected static Logger       log = LoggerFactory.getLogger(RefusePayResourceImpl.class);

    @Autowired
    private MessageSourceAccessor msa;

    /** 余额变更处理service */
    @Resource
    private BasBizService         basBizService;

    /** 拒付服务 */
    @Resource
    private RefuseService         refuseService;

    @Override
    public RefusePayResponse refusePay(RefusePayRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        RefusePayResponse repMsg = new RefusePayResponse();
        try {
            // 1、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(request.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            // 2数据合法性校验
            // 获取原交易
            Transaction oriTransaction = Transaction.selectByAppTranNo(request.getOriAppTranNo());
            if (oriTransaction == null) {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302001, new Object[] { "原交易" }));
            } else if (!BasConstants.ACC_STATUS_SUCCESS.equals(oriTransaction.getAccStatus())) {//原交易记账状态必须为正常记账
                throw new BizException("原交易记账状态不正常");
            }
            // 3、原交易校验
            refuseService.checkOriTransaction(oriTransaction, request.getRefKind(), request.getTranAmt());

            // 4、检查商户信息是否存在
            CusSubaccinfo cusSubaccinfo = basBizService.getCusSubaccinfo(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + request.getPayerAccCode(),
                request.getPayerAccCode(), request.getCcyCode());
            if (cusSubaccinfo == null) {
                log.error("relSubCode:" + BasConstants.REL_SUB_CODE_FREE + request.getPayerAccCode() + " CcyCode:" + request.getCcyCode());
                throw new BizException("账户余额信息不存在");
            }
            // 5、根据商户类型、商户号、交易账户号验证是否允许出款
            basBizService.checkAllowOut(request.getPayerType(), request.getPayerCode(), request.getPayerAccCode());
            // 6、创建交易交易流水
            Transaction transaction = refuseService.createTransaction(request,oriTransaction.getSupplierCode());
            // 7、创建拒付附属
            TranRefuse tranRefuse = refuseService.createTranRefuse(request);
            // 8、拒付记账处理
            refuseService.refusePaySynAccount(request.getRefKind(), request.getTranAmt(), oriTransaction, transaction, tranRefuse,
                request.getPayerAccCode(), request.getCcyCode());
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
