package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTranDrawDao;
import com.somnus.smart.base.domain.TrnTranDraw;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 出款附属  */
public class TranDraw extends TrnTranDraw implements DomainModel<TranDraw, TrnTranDraw> {

	private static final long serialVersionUID = 1L;

	private static TrnTranDrawDao     dao;

	private TranDraw() { }

    public static TranDraw getInstance() {
        return (TranDraw) DomainHelper.getDomainInstance(TranDraw.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTranDrawDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    /*private static TranDraw getTranDraw(TrnTranDrawDao model) {
        TranDraw tranDraw = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranDraw, model);
        }
        return tranDraw;
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
