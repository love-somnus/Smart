package com.somnus.smart.biz.draw.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.draw.resource.DrawWriteOffResource;
import com.somnus.smart.biz.draw.service.DrawWriteOffService;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.Message;
import com.somnus.smart.message.writeoff.DrawWriteOffRequest;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 出款核销记账接口
 */
@Component
@Validated
public class DrawWriteOffResourceImpl implements DrawWriteOffResource {

    protected static Logger     log = LoggerFactory.getLogger(DrawWriteOffResourceImpl.class);

    /** 核销接口 */
    @Resource
    private DrawWriteOffService drawWriteOffService;

    @Override
    public Message writeOff(DrawWriteOffRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        Message repMsg = new Message();
        try {
            //查询出款流水
            DrawTransaction drawTransaction = null;DrawTransaction.selectWriteOffConfirm(request.getDrawId());
            String entrykey = null;
            //出款成功核销
            if (DrawConstants.WRITEOFF_TYPE_SUCCESS.equals(request.getWriteOffType())) {
            	drawTransaction = DrawTransaction.selectWriteOffConfirm(request.getDrawId());
            	if (null == drawTransaction) {
                    throw new BizException(request.getDrawId() + "对应的出款信息不存在或无法做核销");
                }
                if (drawTransaction.getBizType().equals(DrawConstants.DRAW_BIZ_TYPE_REFUSE)) {
                    entrykey = DrawConstants.ENTRY_KEY_REFUSEPAY_NODRAW;
                } else {
                    entrykey = DrawConstants.ENTRY_KEY_DRAW_CONFIRM;
                }
            }
            //出款撤销核销
            else if (DrawConstants.WRITEOFF_TYPE_FAIL.equals(request.getWriteOffType())) {
            	drawTransaction = DrawTransaction.selectWriteOffCancel(request.getDrawId());
            	if (null == drawTransaction) {
                    throw new BizException(request.getDrawId() + "对应的出款信息不存在或无法做核销");
                }
                entrykey = DrawConstants.ENTRY_KEY_DRAW_CANCEL;
            } else {
                throw new BizException("核销类型不正确");
            }
            //创建记账交易流水
            Transaction transaction = drawWriteOffService.createTransaction(drawTransaction);
            //出款成功核销记账
			drawWriteOffService.drawWriteOffSynAccount(transaction, drawTransaction, entrykey, false);
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
