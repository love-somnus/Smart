package com.somnus.smart.domain.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTranIssuedDao;
import com.somnus.smart.base.domain.TrnTranIssued;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 下发审核附属  */
public class TranIssued extends TrnTranIssued implements DomainModel<TranIssued, TrnTranIssued> {

	private static final long serialVersionUID = 1L;
	
	private static TrnTranIssuedDao   dao;

	private TranIssued() { }

    public static TranIssued getInstance() {
        return (TranIssued) DomainHelper.getDomainInstance(TranIssued.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTranIssuedDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    private static TranIssued getTranIssued(TrnTranIssued model) {
        TranIssued tranIssued = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranIssued, model);
        }
        return tranIssued;
    }

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {
        if (this.getIssTranNo()==null) {
            dao.insert(this);
        } else {
            dao.insertNoSeq(this);
        }
        return true;
    }

    public boolean update() {
        int count = dao.updateByPrimaryKey(this);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static TranIssued selectByPrimaryKey(String issTranNo) {
        TrnTranIssued trnTranIssued = dao.selectByPrimaryKey(issTranNo);
        if (trnTranIssued != null) {
            return getTranIssued(trnTranIssued);
        } else {
            return null;
        }
    }

    public boolean updateByPrimaryKeyStatus() {
        int count = dao.updateByPrimaryKeyStatus(this);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static List<TranIssued> selectByAccTranNoStatus(TranIssued tranIssued) {
        List<TrnTranIssued> issuedList = dao.selectByAccTranNoStatus(tranIssued);
        if (issuedList == null || issuedList.size() < 0) {
            return null;
        }
        List<TranIssued> tranIssueds = new ArrayList<TranIssued>();
        for (TrnTranIssued trnTranIssued : issuedList) {
            tranIssueds.add(getTranIssued(trnTranIssued));
        }
        return tranIssueds;
    }
    
    public static String getIssuedTranNo(){
        return  dao.getIssTranNo();
    }
}
