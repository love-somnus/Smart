package com.somnus.smart.biz.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.dao.TrnTranIssuedDao;
import com.somnus.smart.biz.account.common.AccountTransfer;
import com.somnus.smart.biz.account.service.AccountService;
import com.somnus.smart.biz.account.service.BatchIpsDrawService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.TranIssued;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.message.account.BatchIpsDrawRequest;
import com.somnus.smart.message.account.BatchIpsDrawRequest.Order;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.AccountConstants;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

@Service
public class BatchIpsDrawServiceImpl implements BatchIpsDrawService {

    protected static Logger  log = LoggerFactory.getLogger(BatchIpsDrawServiceImpl.class);

    @Resource
    private BasBizService    		basBizService;

    @Resource
    private AccountService   		accountService;

    @Autowired
    private TrnTranIssuedDao 		trnTranIssuedDao;

    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    public void checkAccountRequest(BatchIpsDrawRequest request) throws Exception {
        // 记账请求参数校验
        if (request.getOrders() == null) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302020, new Object[] {}));
        }
        if (request.getBatchCount() != request.getOrders().size()) {
            log.error("下发总笔数:" + request.getBatchCount() + "明细总笔数:" + request.getOrders().size());
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302021, new Object[] {}));
        }
        BigDecimal orderAmts = new BigDecimal(0);
        BigDecimal feeAmts = new BigDecimal(0);
        BigDecimal tranAmts = new BigDecimal(0);
        for (Order order : request.getOrders()) {
            orderAmts = orderAmts.add(order.getOrdAmt());
            feeAmts = feeAmts.add(order.getFeeAmt());
            tranAmts = tranAmts.add(order.getTranAmt());
        }
        if (orderAmts.compareTo(request.getBatchAmt()) != 0) {
            log.error("下发订单总额:" + request.getBatchAmt() + "明细总额:" + orderAmts);
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302022, new Object[] {}));
        }
        if (feeAmts.compareTo(request.getTotalFeeAmt()) != 0) {
            log.error("下发手续费总额:" + request.getTotalFeeAmt() + "明细手续费总额:" + feeAmts);
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302023, new Object[] {}));
        }
        if (AccountConstants.FEE_FLAG_PAY.equals(request.getFeeWay())) {
            if (tranAmts.compareTo(orderAmts.add(feeAmts)) != 0) {
                log.error("下发交易总额:" + tranAmts + "下发订单总额:" + orderAmts + "明细手续费总额:" + feeAmts);
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302024, new Object[] {}));
            }
        } else {
            if (tranAmts.compareTo(orderAmts) != 0) {
                log.error("下发交易总额:" + request.getTotalFeeAmt() + "明细订单总额:" + feeAmts);
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302025, new Object[] {}));
            }
        }
    }

    @Override
    public Transaction createTransaction(BatchIpsDrawRequest request) {
        Transaction transaction = AccountTransfer.msgToTransaction(request);
        transaction.setAccDate(basBizService.getSystemAccDate());
        transaction.setAccTranNo(basBizService.getAccTranNo());
        return transaction;
    }

    @Override
    @Transactional
    public void batchIpsDrawSynAccount(final BatchIpsDrawRequest request, final Transaction transaction) throws Exception {

        // 流动资金总帐扣取
        String entryKey = AccountConstants.ENTRY_KEY_ISSUEDIPS_TOTAL_PRE + transaction.getFeeFlag() + transaction.getFeeStlMode();
        final List<Transaction> transactions = new ArrayList<Transaction>(request.getOrders().size());
        final Account account = Account.getInstance();
        account.synAccount(transaction, entryKey, transaction.getAccDate(), true, new AccountCallBack() {

            @Override
            public Object callBack() throws Exception {
                for (Order order : request.getOrders()) {
                    TranIssued tranIssued = AccountTransfer.msgToTranIssued(request, order);
                    tranIssued.setAccTranNo(transaction.getAccTranNo());
                    tranIssued.setIssTranNo(trnTranIssuedDao.getIssTranNo());

                    Transaction tempTran = Transaction.getInstance();
                    BeanUtils.copyProperties(transaction, tempTran);
                    tranIssued.setStatus(AccountConstants.ISSUED_STATUS_OUT);
                    BeanUtils.copyProperties(tranIssued, tempTran, new String[] { "submitTime" });
                    tempTran.setAccTranNo(tranIssued.getIssTranNo());
                    tranIssued.save();
                    transactions.add(tempTran);
                }
                return transactions;
            }
            
        });

        for (Transaction tran : transactions) {
            account.multiAccount(tran, AccountConstants.ENTRY_KEY_ISSUEDIPS_DETAIL_PRE + tran.getFeeFlag() + tran.getFeeStlMode(), false,
                tran.getAccDate());
        }
    }
}
