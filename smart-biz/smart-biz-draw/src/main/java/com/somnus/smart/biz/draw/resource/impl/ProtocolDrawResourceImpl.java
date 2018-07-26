package com.somnus.smart.biz.draw.resource.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.TrnTransfer;
import com.somnus.smart.biz.draw.resource.ProtocolDrawResource;
import com.somnus.smart.biz.draw.service.ProtocolDrawService;
import com.somnus.smart.message.account.ProtocolDrawRequest;
import com.somnus.smart.message.account.ProtocolDrawResponse;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.JmsService;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.ValidateUtil;

/**
 * 协议划款记账接口
 */
@Component
@Validated
public class ProtocolDrawResourceImpl implements ProtocolDrawResource {

    protected static Logger     log = LoggerFactory.getLogger(ProtocolDrawResourceImpl.class);

    @Resource
    private JmsService          jmsService;

    /** 协议出款服务 */
    @Resource
    private ProtocolDrawService protocolDrawService;

    /** 余额变更处理service */
    @Resource
    private BasBizService       basBizService;

    @Override
    public ProtocolDrawResponse protocolDraw(ProtocolDrawRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        ProtocolDrawResponse repMsg = new ProtocolDrawResponse();
        try {
            //扣划类型：全部
            if (DrawConstants.TRANSFER_TYPE_ALL.equals(request.getTransferType())) {
                String batchNo = protocolDrawService.protocolTransfer(request);
                jmsService.procotolDrawSend(batchNo);
                setRepMsg(repMsg, "0");
            }
            //扣划类型：指定商户
            else if (DrawConstants.TRANSFER_TYPE_SPECIFIC.equals(request.getTransferType())) {
                Date accDate = basBizService.getSystemAccDate();
                for (ProtocolDrawRequest.Order order : request.getOrders()) {
                    try {
                        protocolDrawService.protocolSpecificDraw(order.getMerAccCode(), accDate);
                    } catch (Exception ex) {
                        log.error(order.getMerAccCode() + Constants.EXCEPTION_ERROR, ex);
                    }
                }
                setRepMsg(repMsg, "1");
            }
            //扣划类型：指定重做
            else if (DrawConstants.TRANSFER_TYPE_SPECIFIC_REDO.equals(request.getTransferType())) {
                Date accDate = basBizService.getSystemAccDate();
                TrnTransfer trnTransferQuery = new TrnTransfer();
                trnTransferQuery.setStatus(DrawConstants.TRANSFER_STATUS_FAIL);
                trnTransferQuery.setAccDate(new Date());
                String batchNo = protocolDrawService.selectTransferBatchNo(trnTransferQuery);
                if (ValidateUtil.isEmpty(batchNo)) {
                    throw new BizException("今日没有失败的批次，可重划");
                }
                trnTransferQuery.setBatchNo(batchNo);
                List<TrnTransfer> transferList = protocolDrawService.selectProtocolTransfer(trnTransferQuery);
                for (TrnTransfer trnTransfer : transferList) {
                    try {
                        protocolDrawService.protocolDraw(trnTransfer, accDate);
                    } catch (Exception ex) {
                        log.error("异常账户号：" + trnTransfer.getMerAccCode());
                        log.error(Constants.EXCEPTION_ERROR, ex);
                    }
                }
                setRepMsg(repMsg, "1");
            } else {
                throw new BizException("传入划款类型不正确");
            }
        } catch (BizException e) {
            log.error(Constants.BUSINESS_ERROR, e);
            // 组织错误报文
            MessageUtil.errRetrunInAction(repMsg, e);
            repMsg.setTransferStatus("2");
        } catch (Exception ex) {
            log.error(Constants.EXCEPTION_ERROR, ex);
            // 组织错误报文
            MessageUtil.createErrorMsg(repMsg);
        }
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
        return repMsg;
    }

    private void setRepMsg(ProtocolDrawResponse repMsg, String transferStatus) {
        MessageUtil.createCommMsg(repMsg);
        repMsg.setTransferStatus(transferStatus);
    }

}
