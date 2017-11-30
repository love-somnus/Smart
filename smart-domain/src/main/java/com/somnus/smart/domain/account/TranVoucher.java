package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTranVoucherDao;
import com.somnus.smart.base.domain.TrnTranVoucher;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 凭证附属 */
public class TranVoucher extends TrnTranVoucher implements DomainModel<TranVoucher, TrnTranVoucher> {

	private static final long serialVersionUID = 1L;

	private static TrnTranVoucherDao  dao;

	private TranVoucher() { }

    public static TranVoucher getInstance() {
        return (TranVoucher) DomainHelper.getDomainInstance(TranVoucher.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTranVoucherDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    /*private static TranVoucher getTranVoucher(TrnTranVoucher model) {
        TranVoucher tranVoucher = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranVoucher, model);
        }
        return tranVoucher;
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

    public boolean update() {
        int count = dao.updateByPrimaryKeySelective(this);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

}
