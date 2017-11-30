package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnRefundDao;
import com.somnus.smart.base.domain.TrnRefund;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 退款交易流水 */
public class RefundTransaction extends TrnRefund implements DomainModel<RefundTransaction, TrnRefund> {

	private static final long serialVersionUID = 1L;

	private static TrnRefundDao       dao;

	private RefundTransaction() { }

    public static RefundTransaction getInstance() {
        return (RefundTransaction) DomainHelper.getDomainInstance(RefundTransaction.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnRefundDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    private static RefundTransaction getRefundTransaction(TrnRefund model) {
        RefundTransaction refundTransaction = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(refundTransaction, model);
        }
        return refundTransaction;
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
    
    public boolean update(){
        int count=dao.updateByPrimaryKeySelective(this);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
    
    public static RefundTransaction selectWriteOffConfirm(String refundId){
        TrnRefund trnRefund=dao.selectWriteOffConfirm(refundId);
        return getRefundTransaction(trnRefund);
    }


    public static RefundTransaction selectWriteOffCancel(String refundId){
        TrnRefund trnRefund=dao.selectWriteOffCancel(refundId);
        return getRefundTransaction(trnRefund);
    }
    /**
     * 根据交易流水号查询已销账退款流水记录
     * 
     * @param appTranNo
     * @return
     */
    public static RefundTransaction queryRefundFlagByApp(String appTranNo) {
        TrnRefund trnRefund=dao.selectByAppFlag(appTranNo);
         return getRefundTransaction(trnRefund);
    }

}
