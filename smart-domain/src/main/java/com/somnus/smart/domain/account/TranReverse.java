package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTranReverseDao;
import com.somnus.smart.base.domain.TrnTranReverse;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 冲正附属 */
public class TranReverse extends TrnTranReverse implements DomainModel<TranReverse, TrnTranReverse> {

	private static final long serialVersionUID = 1L;

	private static TrnTranReverseDao  dao;

	private TranReverse() { }

    public static TranReverse getInstance() {
        return (TranReverse) DomainHelper.getDomainInstance(TranReverse.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTranReverseDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    private static TranReverse getTranReverse(TrnTranReverse model) {
        TranReverse tranReverse = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranReverse, model);
        }
        return tranReverse;
    }

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {

        if (getReverseTranNo() == null) {
            dao.insert(this);
            return true;
        } else {
            dao.updateByPrimaryKey(this);
            return true;
        }
    }
    
    public boolean update(){
        int count=dao.updateByPrimaryKey(this);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据AccTranNo查询TranReverse
     * 
     * @param accTranNo
     * @return
     */
    public static TranReverse selectReverseByAccTranNo(String accTranNo) {
        if (accTranNo == null || "".equals(accTranNo.trim())) {
            return null;
        }
        TrnTranReverse trnTranReverse = dao.selectByAccTranNo(accTranNo);
        if (trnTranReverse != null) {
            return getTranReverse(trnTranReverse);
        } else {
            return null;
        }
    }
}
