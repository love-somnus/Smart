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
    private static ThreadLocal<Map<String, SubAccInfo>> subAccInfoLocks = new ThreadLocal<Map<String, SubAccInfo>>();

    /** RelSubCode和SubCode映射 */
    private static ThreadLocal<Map<String, String>>     relSubCodeSubCodeMap = new ThreadLocal<Map<String, String>>();

    /** RelSubCode锁顺序 */
    private static ThreadLocal<List<String>>            relSubAccCodeList = new ThreadLocal<List<String>>();
    
    /** 是否多笔台账记账 */
    private static ThreadLocal<Boolean>					isBatchAccount = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

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
    public void putSubAccInfoLocks(Map<String, SubAccInfo> map) {
        subAccInfoLocks.set(map);
    }

    /**
     * 获取交易账户锁
     * 
     * @return
     */
    public Map<String, SubAccInfo> getSubAccInfoLocks() {
        return subAccInfoLocks.get();
    }

    /**
     * 设置RelSubCode和SubCode映射
     * 
     * @param map
     */
    public void putRelSubCodeSubCodeMap(Map<String, String> map) {
        relSubCodeSubCodeMap.set(map);
    }

    /**
     * 获取RelSubCode和SubCode映射
     * 
     * @return
     */
    public Map<String, String> getRelSubCodeSubCodeMap() {
        return relSubCodeSubCodeMap.get();
    }

    /**
     * 设置RelSubCode锁顺序
     * 
     * @param list
     */
    public void putRelSubAccCodeList(List<String> list) {
        relSubAccCodeList.set(list);
    }

    /**
     * 获取RelSubCode锁顺序
     * 
     * @return
     */
    public List<String> getRelSubAccCodeList() {
        return relSubAccCodeList.get();
    }
    /**
     * 线程变量Locks remove
     */
    public void removeLocks(){
    	subAccInfoLocks.remove();
    }

    /**
     * 线程变量remove
     */
    public void remove() {
        subAccInfoLocks.remove();
        relSubCodeSubCodeMap.remove();
        relSubAccCodeList.remove();
        isBatchAccount.remove();
    }

    
    public boolean isBatchAccount() {
        return isBatchAccount.get();
    }

    
    public void setBatchAccount(boolean isBatch) {
    	isBatchAccount.set(isBatch);
    }
    
}