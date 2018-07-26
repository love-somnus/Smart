package com.somnus.smart.biz.custom.resource.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.biz.custom.resource.VoucherResource;
import com.somnus.smart.biz.custom.service.VoucherService;
import com.somnus.smart.domain.account.TranVoucher;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.voucher.VoucherEnterRequest;
import com.somnus.smart.message.voucher.VoucherEnterResponse;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 凭证记账
 */
@Component
@Validated
public class VoucherResourceImpl implements VoucherResource {

    protected static Logger log = LoggerFactory.getLogger(VoucherResourceImpl.class);

    @Resource
    private VoucherService  voucherService;

    @Override
    public VoucherEnterResponse voucherEnter(VoucherEnterRequest request) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(request));
        if (null == request.getVouchers()) {
            throw new BizException("请求报文集合vouchers不可为空");
        }
        VoucherEnterResponse repMsg = new VoucherEnterResponse();
        try {
            List<VoucherEnterResponse.Result> results = new ArrayList<VoucherEnterResponse.Result>();

            for (VoucherEnterRequest.Voucher voucher : request.getVouchers()) {
                // 记账请求参数校验
                voucherService.checkRquest(voucher);
                // 1、重复记账检查
                Transaction queryTransaction = Transaction.selectByAppTranNo(voucher.getAppTranNo());
                VoucherEnterResponse.Result result = new VoucherEnterResponse.Result();
                if (queryTransaction != null) {
                    setRepMsg(result, queryTransaction, true);
                } else {
                    // 2、创建记账交易流水
                    Transaction transaction = voucherService.createTransaction(voucher, request);
                    try {
                        // 3、创建凭证附属流水
                        TranVoucher tranVoucher = voucherService.createTranVoucher(voucher);
                        tranVoucher.setAccTranNo(transaction.getAccTranNo());
                        // 4、记账
                        voucherService.voucherEnterAccount(transaction, tranVoucher);
                        setRepMsg(result, transaction, true);
                    } catch (Exception ex) {
                        log.error(Constants.EXCEPTION_ERROR, ex);
                        setRepMsg(result, transaction, false);
                    }
                }
                results.add(result);
                repMsg.setResults(results);
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

    private void setRepMsg(VoucherEnterResponse.Result result, TrnTransaction trntransaction, boolean isSuccess) {
        result.setAccDate(trntransaction.getAccDate());
        result.setAccTranNo(trntransaction.getAccTranNo());
        result.setAppTranNo(trntransaction.getAppTranNo());
        //0成功 1 失败
        if (isSuccess) {
            result.setAccStatus("0");
        } else {
            result.setAccStatus("1");
        }

    }
}
