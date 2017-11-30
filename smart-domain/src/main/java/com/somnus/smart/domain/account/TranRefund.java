package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTranRefundDao;
import com.somnus.smart.base.domain.TrnTranRefund;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 出款附属 */
public class TranRefund extends TrnTranRefund implements DomainModel<TranRefund, TrnTranRefund> {

	private static final long serialVersionUID = 1L;

	private static TrnTranRefundDao   dao;

	private TranRefund() { }

    public static TranRefund getInstance() {
        return (TranRefund) DomainHelper.getDomainInstance(TranRefund.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTranRefundDao.class);
    }

    /**
     * 根据TrnTranRefund返回TranRefund
     * 
     * @param model
     * @return
     */
    /*private static TranRefund getTranRefund(TrnTranRefund model) {
        TranRefund tranRefund = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranRefund, model);
        }
        return tranRefund;
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
