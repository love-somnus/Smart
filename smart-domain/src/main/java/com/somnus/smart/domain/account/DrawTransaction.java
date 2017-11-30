package com.somnus.smart.domain.account;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnDrawDao;
import com.somnus.smart.base.domain.TrnDraw;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;

/** 出款交易流水 */
public class DrawTransaction extends TrnDraw implements DomainModel<DrawTransaction, TrnDraw> {

	private static final long serialVersionUID = 1L;
	
	/** dao */
	private static TrnDrawDao dao;
	
	private DrawTransaction(){ }

	/** 获取实例 */
	public static DrawTransaction getInstance() {
		return (DrawTransaction) DomainHelper.getDomainInstance(DrawTransaction.class);
	}

	/** 初始化 */
	public static void init(ApplicationContext ctx) {
		dao = ctx.getBean(TrnDrawDao.class);
	}

	/**
	 * 根据TrnAccDetail返回AccDetail
	 * 
	 * @param model
	 * @return
	 */
	private static DrawTransaction getDrawTransaction(TrnDraw model) {
		DrawTransaction tranDraw = getInstance();
		if (model != null) {
			DomainHelper.setDomainData(tranDraw, model);
		}
		return tranDraw;
	}

	/**
	 * 获取出款交易流水ID
	 * 
	 * @return
	 */
	public String getDrawTransactionId() {
		return dao.getDrawId();
	}

	/**
	 * 落地
	 * 
	 * @return
	 */
	public boolean save() {
		if (this.getDrawId() != null) {
			dao.insert(this);
			return true;
		} else {
			dao.insertInSeq(this);
			return true;
		}

	}

	/**
	 * 更新
	 * 
	 * @return
	 */
	public boolean update() {
		int count = dao.updateByPrimaryKeySelective(this);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查找出款流水表中对应的待销账交易
	 * 
	 * @param refundId
	 * @return
	 */
	public static DrawTransaction selectWriteOffConfirm(String drawId) {
		TrnDraw trnDraw = dao.selectWriteOffConfirm(drawId);
		return getDrawTransaction(trnDraw);
	}

	/**
	 * 查询核销撤销的出款流水
	 * 
	 * @param drawId
	 * @return
	 */
	public static DrawTransaction selectWriteOffCancel(String drawId) {
		TrnDraw trnDraw = dao.selectWriteOffCancel(drawId);
		return getDrawTransaction(trnDraw);
	}

	/**
	 * 根据交易流水号查询已销账出款流水记录
	 * 
	 * @param appTranNo
	 * @return
	 */
	public static DrawTransaction queryDrawFlagByApp(String appTranNo) {
		TrnDraw trnDraw = dao.selectByAppFlag(appTranNo);
		return getDrawTransaction(trnDraw);
	}
}
