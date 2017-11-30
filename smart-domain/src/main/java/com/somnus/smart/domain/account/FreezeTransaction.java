package com.somnus.smart.domain.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnFreezeDao;
import com.somnus.smart.base.domain.TrnFreeze;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 冻结交易流水 */
public class FreezeTransaction extends TrnFreeze implements DomainModel<FreezeTransaction, TrnFreeze> {

	private static final long serialVersionUID = 1L;

	private static TrnFreezeDao       dao;

	private FreezeTransaction() {
    }

    public static FreezeTransaction getInstance() {
        return (FreezeTransaction) DomainHelper.getDomainInstance(FreezeTransaction.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnFreezeDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    private static FreezeTransaction getFreezeTransaction(TrnFreeze model) {
        FreezeTransaction freezeTransatction = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(freezeTransatction, model);
        }
        return freezeTransatction;
    }

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {
        dao.insert(this);
        return true;
    }

    public boolean update() {
        int count = dao.updateByPrimaryKeySelective(this);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static List<FreezeTransaction> select(FreezeTransaction freezeTransaction) {
        List<TrnFreeze> trnFreezes = dao.select(freezeTransaction);
        List<FreezeTransaction> freezeTransactions = new ArrayList<FreezeTransaction>();
        if (trnFreezes == null || trnFreezes.size() < 0) {
            return null;
        }
        for (TrnFreeze trnFreeze : trnFreezes) {
            freezeTransactions.add(FreezeTransaction.getFreezeTransaction(trnFreeze));
        }
        return freezeTransactions;
    }
    
    public static List<FreezeTransaction> selectTrnFreezeList(String appFreezeNo){
        FreezeTransaction freezeTransaction = FreezeTransaction.getInstance();
        freezeTransaction.setAppTranNo(appFreezeNo);
        return select(freezeTransaction);
    }
}
