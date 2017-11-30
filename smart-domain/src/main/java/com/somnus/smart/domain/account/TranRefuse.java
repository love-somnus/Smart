package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnTranrefuseDao;
import com.somnus.smart.base.domain.TrnTranrefuse;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 拒付附属 */
public class TranRefuse extends TrnTranrefuse implements DomainModel<TranRefuse, TrnTranrefuse> {

	private static final long serialVersionUID = 1L;
	
	private static TrnTranrefuseDao   dao;

	private TranRefuse() { }

    public static TranRefuse getInstance() {
        return (TranRefuse) DomainHelper.getDomainInstance(TranRefuse.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnTranrefuseDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    /*private static TranRefuse getTranRefuse(TrnTranrefuse model) {
        TranRefuse tranReverse = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(tranReverse, model);
        }
        return tranReverse;
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
