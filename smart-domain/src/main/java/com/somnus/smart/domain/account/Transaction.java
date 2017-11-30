package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTransactionDao;
import com.somnus.smart.base.domain.TrnTransaction;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 交易流水 */
public class Transaction extends TrnTransaction implements DomainModel<Transaction, TrnTransaction> {

	private static final long serialVersionUID = 1L;

	private static TrnTransactionDao  trnTransactionDao;

	private Transaction() { }

    public static Transaction getInstance() {
        return (Transaction) DomainHelper.getDomainInstance(Transaction.class);
    }

    public static void init(ApplicationContext context) {
        trnTransactionDao = context.getBean(TrnTransactionDao.class);
    }

    /**
     * 根据TrnTransaction返回Transaction
     * 
     * @param model
     * @return
     */
    public static Transaction getTransaction(TrnTransaction model) {
        Transaction transaction = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(transaction, model);
        }
        return transaction;
    }

    /**
     * 更新
     * 
     * @return
     */
    public boolean update() {
        if (getAccTranNo() == null) {
            return false;
        }

        int ret = trnTransactionDao.update(this);
        if (ret == 0) {
            return false;
        }
        return true;
    }

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {
        int count = 0;
        if (this.getAccTranNo() != null) {
            count = trnTransactionDao.insertNoSerq(this);
        } else {
            count = trnTransactionDao.insert(this);
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Transaction selectByAppTranNo(String appTranNo) {
        if (appTranNo == null || "".equals(appTranNo.trim())) {
            return null;
        }

        TrnTransaction trnTransaction = trnTransactionDao.selectByAppTranNo(appTranNo);
        if (trnTransaction == null) {
            return null;
        }
        return getTransaction(trnTransaction);
    }

    /**
     * 根据原应用流水号和交易码查询
     * 
     * @param appTranNo
     * @return
     */
    public static Transaction selectByOriAppTranNoAndTranCode(String oriAppTranNo, String tranCode) {
        if (oriAppTranNo == null || "".equals(oriAppTranNo.trim())) {
            return null;
        }

        TrnTransaction trnTransaction = trnTransactionDao.selectByOrgAppTranNoAndTranCode(oriAppTranNo, tranCode);
        if (trnTransaction == null) {
            return null;
        }
        return getTransaction(trnTransaction);
    }

    public static Transaction selectByAccTranNo(String errTranNo) {
        if (errTranNo == null || "".equals(errTranNo.trim())) {
            return null;
        }
        TrnTransaction trnTransaction = trnTransactionDao.selectByAccTranNo(errTranNo);
        if (trnTransaction == null) {
            return null;
        }
        return getTransaction(trnTransaction);
    }
}
