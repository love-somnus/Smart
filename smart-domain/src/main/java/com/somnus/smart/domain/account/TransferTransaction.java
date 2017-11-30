package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTransferDao;
import com.somnus.smart.base.domain.TrnTransfer;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 划款交易流水 */
public class TransferTransaction extends TrnTransfer implements DomainModel<TransferTransaction, TrnTransfer> {

	private static final long serialVersionUID = 1L;

	private static TrnTransferDao     dao;

	private TransferTransaction() { }

    public static TransferTransaction getInstance() {
        return (TransferTransaction) DomainHelper.getDomainInstance(TransferTransaction.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTransferDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    /*private static TransferTransaction getTransfer(TrnTransfer model) {
        TransferTransaction transfer = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(transfer, model);
        }
        return transfer;
    }*/

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {

        int count = dao.insert(this);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean update() {
        int count = dao.updateByPrimaryKeySelective(this);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

}
