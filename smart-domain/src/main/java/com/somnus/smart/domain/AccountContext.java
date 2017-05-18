package com.somnus.smart.domain;

import java.util.List;
import java.util.Map;

import com.somnus.smart.domain.account.SubAccInfo;

/**
 * 记账上下文
 */
public class AccountContext {
	
	private static ThreadLocal<AccountContext>		accountContext = new ThreadLocal<AccountContext>();
	
    /** 交易账户锁 */
    private Map<String, SubAccInfo> subAccInfoLocks;

    /** RelSubCode和SubCode映射 */
    private Map<String, String>     relSubCodeSubCodeMap;

    /** RelSubCode锁顺序 */
    private List<String>            relSubAccCodeList;
    
    /** 是否多笔台账记账 */
    private Boolean					isBatchAccount;

    private AccountContext() {}

    /**
     * Returns the AccountContext specific to the current thread.
     *
     * @return the AccountContext for the current thread, is never <tt>null</tt>.
     */
    public static AccountContext getContext() {
    	AccountContext context = accountContext.get();
    	if(context == null){
    		context = new AccountContext();
    		accountContext.set(context);
    	}
        return context;
    }

    /**
     * 设置交易账户锁
     * 
     * @param map
     */
    public void putSubAccInfoLocks(Map<String, SubAccInfo> subAccInfoLocks) {
        this.subAccInfoLocks = subAccInfoLocks;
    }

    /**
     * 获取交易账户锁
     * 
     * @return
     */
    public Map<String, SubAccInfo> getSubAccInfoLocks() {
        return subAccInfoLocks;
    }

    /**
     * 设置RelSubCode和SubCode映射
     * 
     * @param map
     */
    public void putRelSubCodeSubCodeMap(Map<String, String> relSubCodeSubCodeMap) {
        this.relSubCodeSubCodeMap = relSubCodeSubCodeMap;
    }

    /**
     * 获取RelSubCode和SubCode映射
     * 
     * @return
     */
    public Map<String, String> getRelSubCodeSubCodeMap() {
        return relSubCodeSubCodeMap;
    }

    /**
     * 设置RelSubCode锁顺序
     * 
     * @param list
     */
    public void putRelSubAccCodeList(List<String> relSubAccCodeList) {
        this.relSubAccCodeList = relSubAccCodeList;
    }

    /**
     * 获取RelSubCode锁顺序
     * 
     * @return
     */
    public List<String> getRelSubAccCodeList() {
        return relSubAccCodeList;
    }

    /**
     * 线程变量remove
     */
    public void remove() {
    	accountContext.remove();
    }

    
    public boolean isBatchAccount() {
        return isBatchAccount;
    }

    
    public void setBatchAccount(boolean isBatchAccount) {
    	this.isBatchAccount = isBatchAccount;
    }
    
}