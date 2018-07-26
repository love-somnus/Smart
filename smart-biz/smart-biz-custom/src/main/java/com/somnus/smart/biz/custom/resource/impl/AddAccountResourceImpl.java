package com.somnus.smart.biz.custom.resource.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.custom.resource.AddAccountResource;
import com.somnus.smart.biz.custom.service.AddAccountService;
import com.somnus.smart.message.custom.AddAccountRequest;
import com.somnus.smart.message.custom.AddAccountResponse;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 差异补记账接口
 */
@Component
@Validated
public class AddAccountResourceImpl implements AddAccountResource {

    private transient Logger  log = LoggerFactory.getLogger(this.getClass());
    /** 补记账接口 */
    @Resource
    private AddAccountService addAccountService;

    /**
	 * 差异补记账
     *
     * @param request
     * @return
     */
    @Override
    public AddAccountResponse addAccount(AddAccountRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        AddAccountResponse repMsg = new AddAccountResponse();
        int totalCount = 0;
        int successCount = 0;
        int failCount = 0;
        //校验请求参数
        if (request == null || request.getOrders() == null || request.getOrders().size() == 0) {
            log.error(Constants.EXCEPTION_ERROR, "差异流水号不能为空！");
            // 组织错误报文
            MessageUtil.createErrorMsg(repMsg);
            repMsg.setRepMsg("处理失败，差异流水号不能为空！");
            return repMsg;
        }
        List<AddAccountRequest.Order> orders = request.getOrders();
        for (AddAccountRequest.Order order : orders) {
            try {
                //补记账
                addAccountService.addAccount(order.getErrNo());
                successCount++;
            } catch (BizException e) {
                failCount++;
                log.error(Constants.BUSINESS_ERROR, e);
                // 组织错误报文
                MessageUtil.errRetrunInAction(repMsg, e);
                
            } catch (Exception e) {
                failCount++;
                log.error(Constants.EXCEPTION_ERROR, e);
                // 组织错误报文
                MessageUtil.createErrorMsg(repMsg);
            }
        }
        totalCount=request.getOrders().size();
        //组织结果报文
        setRepMsg(repMsg, totalCount, successCount, failCount);
        log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
        return repMsg;
    }

    private void setRepMsg(AddAccountResponse repMsg, int totalCount, int successCount, int failCount) {
        MessageUtil.createCommMsg(repMsg);
        repMsg.setRepMsg("处理总条数：" + totalCount + " ，处理成功条数：" + successCount + " ，处理失败条数：" + failCount);
    }
}
