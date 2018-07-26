package com.somnus.smart.biz.income.resource.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.somnus.smart.biz.income.common.IncomeConstants;
import com.somnus.smart.biz.income.resource.IncomeResource;
import com.somnus.smart.biz.income.service.IncomeService;
import com.somnus.smart.domain.account.AccEntryCfg;
import com.somnus.smart.domain.account.TranReverse;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.AccountResponse;
import com.somnus.smart.message.account.CancelReverseRequest;
import com.somnus.smart.message.account.DepositRequest;
import com.somnus.smart.message.account.IncomeRequest;
import com.somnus.smart.message.account.IpsPayRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.JmsService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.Constants;
import com.somnus.smart.service.common.MessageUtil;
import com.somnus.smart.service.common.enums.AccountType;
import com.somnus.smart.support.common.JsonUtils;
import com.somnus.smart.support.exceptions.BizException;

/**
 * 银行卡收单、撤销冲正、isp账户支付记账
 */
@Component
@Validated
public class IncomeResourceImpl implements IncomeResource {

    protected static Logger log = LoggerFactory.getLogger(IncomeResourceImpl.class);

    @Resource
    private JmsService      jmsService;

    @Resource
    private IncomeService   incomeService;

    @Resource
    private BasBizService   basBizService;

    @Override
    public AccountResponse bankIncome(IncomeRequest incomeRequest) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(incomeRequest));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、手续费结算模式校验
            incomeService.checkFee(incomeRequest.getFeeStlMode(), incomeRequest.getSysCode(), incomeRequest.getFeeWay(),
                incomeRequest.getThdAccCode());
            // 2、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(incomeRequest.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            // 3、数据合法性校验
            incomeService.checkMerAccAndMerSub(incomeRequest.getPayeeCode(), incomeRequest.getPayeeAccCode(), incomeRequest.getSupplierCode(),
                incomeRequest.getBankAccCode());
            // 4、创建收单记账流水
            Transaction transaction = incomeService.createTransaction(incomeRequest);
            // 5、记账流水落地
            transaction.save();
            // 6、发送jms消息
            jmsService.incomeSend(transaction);
            // 7、返回同步处理结果
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
    
    @Override
    public AccountResponse deposit(DepositRequest depositRequest){
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(depositRequest));
        AccountResponse repMsg = new AccountResponse();
        try {
            //1、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(depositRequest.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            //2、业务校验
            incomeService.depositBizCheck(depositRequest);
            //3、创建充值记账流水
            Transaction transaction = incomeService.createDepositTran(depositRequest);
            //4、组装记账主键key
            String entryKey = AccEntryCfg.getEntryKeyByTranCode(depositRequest.getTranCode(), transaction.getFeeFlag() , 
            		transaction.getFeeStlMode(),transaction.getBlnMode());
            //5、同步记账
            incomeService.depositSynAccount(transaction, entryKey, transaction.getAccDate(), true);
            //6、返回同步处理结果
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

    @Override
    public AccountResponse cancelReverse(CancelReverseRequest cancelReverseRequest) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(cancelReverseRequest));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、手续费结算模式校验
            incomeService.checkFee(cancelReverseRequest.getFeeStlMode(), cancelReverseRequest.getSysCode(), cancelReverseRequest.getFeeWay(),
                cancelReverseRequest.getThdAccCode());
            // 2、重复记账检查
            Transaction queryTransaction = Transaction.selectByAppTranNo(cancelReverseRequest.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            // 3、验证原交易是否存
            Transaction oriTransaction = Transaction.selectByAppTranNo(cancelReverseRequest.getOriAppTranNo());
            if (oriTransaction == null) {
                throw new BizException(cancelReverseRequest.getOriAppTranNo() + "原交易不存在!");
            }
            TranReverse tranReverse = TranReverse.selectReverseByAccTranNo(oriTransaction.getAccTranNo());
            //如果原沖正交易已经做过撤销冲正交易
            if (tranReverse != null && BasConstants.REVERSE_STATUS_CANCELED.equals(tranReverse.getStatus())
                || BasConstants.REVERSE_STATUS_HANDING_CANCELED.equals(tranReverse.getStatus())) {
                throw new BizException(cancelReverseRequest.getOriAppTranNo() + "原冲正交易已经撤销!");
            }
            // 4、数据合法性校验包括验证商户信息是否存在，银行银行科目是否存在，流水资金科目是否存在
            incomeService.checkMerAccAndMerSub(cancelReverseRequest.getPayeeCode(), cancelReverseRequest.getPayeeAccCode(),
                cancelReverseRequest.getSupplierCode(), cancelReverseRequest.getBankAccCode());
            // 5、创建撤销冲正记账交易流水
            Transaction transaction = incomeService.createCancelReverseTran(cancelReverseRequest);
            // 6、将原冲正记账附属流水状态更新为“已撤销冲正”
            if (tranReverse != null) {
                if (BasConstants.REVERSE_STATUS_HANDING.equals(tranReverse.getStatus())) {
                    tranReverse.setStatus(BasConstants.REVERSE_STATUS_HANDING_CANCELED);
                } else if (BasConstants.REVERSE_STATUS_REVERSEED.equals(tranReverse.getStatus())) {
                    tranReverse.setStatus(BasConstants.REVERSE_STATUS_CANCELED);
                }
            }
            // 7、同步记账
            String entryKey = IncomeConstants.ENTRY_KEY_INCOME_PRE + transaction.getBlnMode() + transaction.getFeeFlag()
                              + transaction.getFeeStlMode();
            incomeService.incomeSynAccount(transaction, entryKey, transaction.getAccDate(), true);
            // 8、返回同步处理结果
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

    @Override
    public AccountResponse ipsPay(IpsPayRequest ipsPayRequest) {
        log.info(Constants.REQUEST_MSG, JsonUtils.toString(ipsPayRequest));
        AccountResponse repMsg = new AccountResponse();
        try {
            // 1、手续费结算模式校验
            incomeService.checkFee(ipsPayRequest.getFeeStlMode(), ipsPayRequest.getSysCode(), ipsPayRequest.getFeeWay(),
                ipsPayRequest.getThdAccCode());
            // 2、记账流水重复校验
            Transaction queryTransaction = Transaction.selectByAppTranNo(ipsPayRequest.getAppTranNo());
            if (queryTransaction != null) {
            	MessageUtil.setRepMsg(repMsg, queryTransaction);
                log.info(Constants.REPONSE_MSG, JsonUtils.toString(repMsg));
                return repMsg;
            }
            // 3、数据合法性校验，校验收款方是否允许出款
            basBizService.checkAllowOut(ipsPayRequest.getPayerType(), ipsPayRequest.getPayerCode(), ipsPayRequest.getPayerAccCode());
            // 4、 检查商户流动资金账户余额是否足够出款？
            BigDecimal availableBal = basBizService.getAvailableBal(AccountType.BIZ,BasConstants.REL_SUB_CODE_FREE + ipsPayRequest.getPayerAccCode(),
                ipsPayRequest.getPayerAccCode(), ipsPayRequest.getCcyCode());
            if (availableBal.compareTo(ipsPayRequest.getTranAmt()) < 0) {
                throw new BizException("账户余额不足");
            }
            // 5、创建isp账户支付记载交易流水
            Transaction transaction = incomeService.createIPSPayTran(ipsPayRequest);
            // 6、同步记账
            String entryKey = IncomeConstants.ENTRY_KEY_IPSINCOME_PRE + transaction.getFeeFlag() + transaction.getFeeStlMode();
            incomeService.ipsPayAccount(transaction, entryKey, transaction.getAccDate(), true);

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
