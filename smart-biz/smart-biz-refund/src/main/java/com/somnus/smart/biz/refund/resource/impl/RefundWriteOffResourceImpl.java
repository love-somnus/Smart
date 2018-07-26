package com.somnus.smart.biz.refund.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.refund.common.RefundConstants;
import com.somnus.smart.biz.refund.resource.RefundWriteOffResource;
import com.somnus.smart.biz.refund.service.RefundWriteOffService;
import com.somnus.smart.domain.account.RefundTransaction;
import com.somnus.smart.message.Message;
import com.somnus.smart.message.writeoff.RefundWriteOffRequest;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 退款核销记账接口
 */
@Component
@Validated
public class RefundWriteOffResourceImpl implements RefundWriteOffResource {

    protected static Logger       log = LoggerFactory.getLogger(RefundWriteOffResourceImpl.class);

    @Resource
    private RefundWriteOffService refundWriteOffService;

    @Override
    public Message writeOff(RefundWriteOffRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        Message repMsg = new Message();
        try {
            // 退款成功核销
            if (RefundConstants.WRITEOFF_TYPE_SUCCESS.equals(request.getWriteOffType())) {
                refundWriteOffService.refundedWriteOffAccount(request);
            } 
            // 退款撤销核销
            else if (RefundConstants.WRITEOFF_TYPE_FAIL.equals(request.getWriteOffType())) {
                // 获取待核销的退款交易
                RefundTransaction refundTransaction = RefundTransaction.selectWriteOffCancel(request.getRefundId());
                // 银行退款撤销核销
                if (RefundConstants.BIZ_TYPE_BANKREFUND.equals(refundTransaction.getBizType())) {
                    refundWriteOffService.refundCancelWriteOffAccount(request, refundTransaction);
                } 
                // 主动退款不支持撤销核销
                else if (RefundConstants.BIZ_TYPE_AUTOREFUND.equals(refundTransaction.getBizType())) {
                    throw new BizException("主动退款不支持撤销核销");
                } else {
                    throw new BizException(refundTransaction.getBizType() + "BizType不正确");
                }
            } else {
                throw new BizException("核销类型不正确");
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
