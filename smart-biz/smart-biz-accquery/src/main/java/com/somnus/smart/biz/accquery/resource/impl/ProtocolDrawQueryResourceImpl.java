package com.somnus.smart.biz.accquery.resource.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.accquery.resource.ProtocolDrawQueryResource;
import com.somnus.smart.biz.accquery.service.ProtocolDrawQueryService;
import com.somnus.smart.message.accquery.ProtocolDrawQueryRequest;
import com.somnus.smart.message.accquery.ProtocolDrawQueryResponse;
import com.somnus.smart.message.accquery.ProtocolTransferQueryRequest;
import com.somnus.smart.message.accquery.ProtocolTransferQueryResponse;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

@Component
@Validated
public class ProtocolDrawQueryResourceImpl implements ProtocolDrawQueryResource {

    private transient Logger         log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ProtocolDrawQueryService protocolDrawQueryService;

    public ProtocolDrawQueryResponse protocolDrawQuery(ProtocolDrawQueryRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        ProtocolDrawQueryResponse response = new ProtocolDrawQueryResponse();
        try {
            response = protocolDrawQueryService.protocolDrawQuery(request);
            //返回成功报文
            MessageUtil.createCommMsg(response);
        } catch (BizException e) {
            log.error(Constants.BUSINESS_ERROR, e);
            // 组织错误报文
            MessageUtil.errRetrunInAction(response, e);
        } catch (Exception ex) {
            log.error(Constants.EXCEPTION_ERROR, ex);
            // 组织错误报文
            MessageUtil.createErrorMsg(response);
        }
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
        return response;
    }

    @Override
    public ProtocolTransferQueryResponse protocolTransferQuery(ProtocolTransferQueryRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        ProtocolTransferQueryResponse response = new ProtocolTransferQueryResponse();
        try {
            response = protocolDrawQueryService.protocolDrawQuery(request);
            //返回成功报文
            MessageUtil.createCommMsg(response);
        } catch (BizException e) {
            log.error(Constants.BUSINESS_ERROR, e);
            // 组织错误报文
            MessageUtil.errRetrunInAction(response, e);
        } catch (Exception ex) {
            log.error(Constants.EXCEPTION_ERROR, ex);
            // 组织错误报文
            MessageUtil.createErrorMsg(response);
        }
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(response));
        return response;
    }

}
