package com.somnus.smart.domain.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.CusSubAccInfoDao;
import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.domain.AccountContext;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.service.common.CusSubAccInfoUtil;

/** 交易账户锁 */
public class SubAccInfoLock {

    private static CusSubAccInfoDao dao;

    private SubAccInfoLock() { }

    public static SubAccInfoLock getInstance() {
        return (SubAccInfoLock) DomainHelper.getDomainInstance(SubAccInfoLock.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(CusSubAccInfoDao.class);

    }

    /**
     * 科目锁排序
     * 
     * @param relSubAccCodes
     * @return
     */
    public void lockSort(AccountContext accountContext) {

        Set<String> relSubAccCodes = accountContext.getRelSubCodeSubCodeMap().keySet();

        if (relSubAccCodes == null || relSubAccCodes.size() == 0) {
            return;
        }
        List<String> relSubAccCodeList = new ArrayList<String>();
        String[] relSubAccCodeArr = new String[relSubAccCodes.size()];

        relSubAccCodes.toArray(relSubAccCodeArr);
        Arrays.sort(relSubAccCodeArr);
        for (int i = 0; i < relSubAccCodeArr.length; i++) {
            relSubAccCodeList.add(relSubAccCodeArr[i]);
        }
        accountContext.putRelSubAccCodeList(relSubAccCodeList);
    }

    /**
     * 锁交易账户
     * 
     * @param relSubAccCode
     * @param accountContext
     * @return
     */
    public SubAccInfo lockSubAccInfo(String relSubAccCode, AccountContext accountContext) {
        if (accountContext.isBatchAccount()) {
            return lockMultiSubAccInfo(relSubAccCode, accountContext);
        } else {
            return lockSingleSubAccInfo(relSubAccCode, accountContext);
        }
    }

    /**
     * 锁单个账户
     * 
     * @param relSubAccCode
     * @param relSubAccCodeSubAccCode
     * @return
     */
    private SubAccInfo lockSingleSubAccInfo(String relSubAccCode, AccountContext accountContext) {
        Map<String, String> relSubCodeSubCodeMap = accountContext.getRelSubCodeSubCodeMap();
        String subAccCode = null;
        if (relSubCodeSubCodeMap == null) {
            return null;
        }
        if (relSubCodeSubCodeMap.get(relSubAccCode) == null) {
            return null;
        }
        subAccCode = relSubCodeSubCodeMap.get(relSubAccCode);
        CusSubaccinfo cusSubaccinfo = dao.selectBySubAcctForUpdate(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode), subAccCode);
        if (cusSubaccinfo == null) {
            return null;
        }
        return SubAccInfo.getSubAccInfo(cusSubaccinfo);
    }

    /**
     * 锁账户
     * 
     * @param relSubAccCode
     * @param relSubAccCodeSubAccCode
     * @return
     */
    private SubAccInfo lockMultiSubAccInfo(String relSubAccCode, AccountContext accountContext) {
        SubAccInfo subAccInfo = null;
        Map<String, SubAccInfo> subAccInfoLocks = accountContext.getSubAccInfoLocks();
        if (subAccInfoLocks != null) {
            subAccInfo = accountContext.getSubAccInfoLocks().get(relSubAccCode);
            if (subAccInfo != null) {
                return subAccInfo;
            }
        } else {
            accountContext.putSubAccInfoLocks(new HashMap<String, SubAccInfo>());
        }
        int index = accountContext.getRelSubAccCodeList().indexOf(relSubAccCode);
        if (index >= 0) {
            for (int i = 0; i <= index; i++) {
                String relSubAccCodeTemp = accountContext.getRelSubAccCodeList().get(i);
                if (accountContext.getSubAccInfoLocks().get(relSubAccCodeTemp) != null) {
                    continue;
                }
                subAccInfo = lockSingleSubAccInfo(relSubAccCodeTemp, accountContext);
                if (subAccInfo == null) {
                    return null;
                }
                accountContext.getSubAccInfoLocks().put(relSubAccCodeTemp, subAccInfo);
            }
        }
        return subAccInfo;
    }
}
