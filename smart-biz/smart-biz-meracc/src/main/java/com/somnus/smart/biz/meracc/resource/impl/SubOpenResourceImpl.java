package com.somnus.smart.biz.meracc.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.meracc.resource.SubOpenResource;
import com.somnus.smart.message.Message;
import com.somnus.smart.message.meracct.SubOpenRequest;
import com.somnus.smart.service.MeracctInnerService;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

@Component
@Validated
public class SubOpenResourceImpl implements SubOpenResource {

    private transient Logger    log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MeracctInnerService meracctInnerService;

    @Override
    public Message open(SubOpenRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        Message message = new Message();
        try {
            meracctInnerService.txnMerInnerCreate(request.getSubCode(), request.getCcyCode());
            //返回成功报文
            message = MessageUtil.createCommMsg();
        } catch (BizException e) {
            log.error(Constants.BUSINESS_ERROR, e);
            // 组织错误报文
            message = MessageUtil.errRetrunInAction(e);
        } catch (Exception ex) {
            log.error(Constants.EXCEPTION_ERROR, ex);
            // 组织错误报文
            message = MessageUtil.createErrorMsg();
        }
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(message));
        return message;
    }

}
