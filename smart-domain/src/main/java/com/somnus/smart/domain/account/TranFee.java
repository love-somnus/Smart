package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TcorTrnTranFeeDao;
import com.somnus.smart.base.domain.TcorTrnTranFee;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 收费附属 */
public class TranFee extends TcorTrnTranFee implements DomainModel<TranFee, TcorTrnTranFee> {

	private static final long serialVersionUID = 1L;
	
	private static TcorTrnTranFeeDao     dao;

    private TranFee() { }

    public static TranFee getInstance() {
        return (TranFee) DomainHelper.getDomainInstance(TranFee.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TcorTrnTranFeeDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    /*private static TranFee getTran(TcorTrnTranFeeDao model) {
        TranFee tranFee = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranFee, model);
        }
        return tranFee;
    }*/

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {
        dao.insert(this);
        return true;
    }

}
