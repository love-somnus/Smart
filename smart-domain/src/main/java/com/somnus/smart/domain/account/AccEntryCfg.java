package com.somnus.smart.domain.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;

import com.somnus.smart.base.domain.CfgAccEntry;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;
import com.somnus.smart.service.common.AccountConstants;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.service.common.EntryCodeConstant;
import com.somnus.smart.service.common.enums.TranCode;
import com.somnus.smart.service.component.cache.CfgAccEntryCache;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;

/** 分账户明细  */
public class AccEntryCfg extends CfgAccEntry implements DomainModel<AccEntryCfg, CfgAccEntry> {
    
	private static final long serialVersionUID = 1L;
	
	/** messageSourceAccessor */
    private static MessageSourceAccessor messageSourceAccessor;
    /** cfgAccEntryCache */
    private static CfgAccEntryCache      cfgAccEntryCache;

    private static Logger                LOGGER = LoggerFactory.getLogger(AccEntryCfg.class);
    
    private AccEntryCfg(){ }

    /**
     * 获取实例
     * 
     * @return
     */
    public static AccEntryCfg getInstance() {
        return (AccEntryCfg) DomainHelper.getDomainInstance(AccEntryCfg.class);
    }
    /**
     * 初始化
     * 
     * @param context
     */
    public static void init(ApplicationContext context) {
        messageSourceAccessor = context.getBean(MessageSourceAccessor.class);
        cfgAccEntryCache = context.getBean(CfgAccEntryCache.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    private static AccEntryCfg getAccDetail(CfgAccEntry model) {
        AccEntryCfg accEntryCfg = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(accEntryCfg, model);
        }
        return accEntryCfg;
    }

    /**
     * 根据accEntryKey获取所有的会计分录
     * 
     * @param accEntryKey
     * @return
     */
    public List<AccEntryCfg> getAllAccEntryList(String accEntryKey) {
        List<AccEntryCfg> accEntryList = new ArrayList<AccEntryCfg>();
        // 根据accEntryKey获取会计分录配置
        String keyValue = EntryCodeConstant.entryCodeMap.get(accEntryKey);
        if (keyValue == null) {
            LOGGER.error("会计分录未配置accEntryKey:{}", new Object[] { accEntryKey });
            throw new BizException(messageSourceAccessor.getMessage(MsgCodeList.ERROR_302001, new Object[] { "会计分录配置" }));
        }
        String[] accEntryCodes = keyValue.split("[|]");
        CfgAccEntry cfgAccEntry = null;
        for (String entryCode : accEntryCodes) {
            if (entryCode.startsWith("-")) {
                // 根据会计分录代码获取会计分录
                cfgAccEntry = getCfgAccEntry(entryCode.substring(1));
                if (cfgAccEntry == null) {
                    LOGGER.error("会计分录未配置entryCode:{}", new Object[] { entryCode.substring(1) });
                    throw new BizException(messageSourceAccessor.getMessage(MsgCodeList.ERROR_302001, new Object[] { "会计分录配置" }));
                }
                cfgAccEntry.setSubAmtSign1st(BasConstants.IS_RED_TRUE);
                cfgAccEntry.setSubAmtSign2nd(BasConstants.IS_RED_TRUE);
                cfgAccEntry.setSubAmtSign3rd(BasConstants.IS_RED_TRUE);
                cfgAccEntry.setSubAmtSign4th(BasConstants.IS_RED_TRUE);
            } else {
                cfgAccEntry = getCfgAccEntry(entryCode);
                if (cfgAccEntry == null) {
                    LOGGER.error("会计分录未配置entryCode:{}", new Object[] { entryCode });
                    throw new BizException(messageSourceAccessor.getMessage(MsgCodeList.ERROR_302001, new Object[] { "会计分录配置" }));
                }
            }

            accEntryList.add(getAccDetail(cfgAccEntry));
        }
        return accEntryList;
    }

    /**
     * 根据会计分录代码获取会计分录
     * 
     * @param accEntryCode
     * @return
     */
    private CfgAccEntry getCfgAccEntry(String accEntryCode) {
        // 根据会计分录代码获取会计分录
        CfgAccEntry cfgAccEntry = cfgAccEntryCache.selectByKey(accEntryCode);
        if (cfgAccEntry == null) {
            LOGGER.error("会计分录未配置accEntryCode:{}", accEntryCode);
            throw new BizException(messageSourceAccessor.getMessage(MsgCodeList.ERROR_302001, new Object[] { "会计分录配置" }));
        }
        return cfgAccEntry;
    }

    /**
     * 通过
     * 
     * @param tranCode
     * @param feeFlag
     *            0 默认 1 付款人承担 2 收款人承担 3 第三方承担
     * @param feeStlMode
     *            0：默认 1：日结 2：预付费 3：后结算
     * @param blnMode
     * 			  0：默认 1:T+0实时入账 2：T+0延时入账 3：T+N入账 4：人工触发入账
     * @return
     */
    public static String getEntryKeyByTranCode(String tranCode, String feeFlag, String feeStlMode,String blnMode) throws Exception{
        //商户提现
        if (tranCode.equals(TranCode.MERCHANT_WITHDRAW.getCode())||tranCode.equals(TranCode.MERCHANT_WITHDRAW_OLD.getCode())) {
            return DrawConstants.ENTRY_KEY_DRAW_PRE + feeFlag + feeStlMode;
        }
        //个人标准账户提现
        else if (tranCode.equals(TranCode.INDIVIDUAL_WITHDRAW.getCode())&&!feeFlag.equals(BasConstants.FEE_FLAG_THD)) {
            return DrawConstants.ENTRY_KEY_DRAW_PRE + feeFlag + feeStlMode;
        }
        //个人专用提现
        else if (tranCode.equals(TranCode.INDIVIDUAL_SPECIAL_WITHDRAW.getCode())) {
            return DrawConstants.ENTRY_KEY_SPECIAL_DRAW_PRE + feeFlag + feeStlMode;
        }
        //标准账户转账到标准账户
        else if(tranCode.equals(TranCode.ACC_TRANS_STD_2_STD.getCode())||tranCode.equals(TranCode.ACC_TRANS_STD_2_STD_OLD.getCode())){
            return AccountConstants.ENTRY_KEY_STD_TRANS_PRE+feeFlag+feeStlMode;
        }
        //标准账户转账到专用账户
        else if(tranCode.equals(TranCode.ACC_TRANS_STD_2_SPE.getCode())){
            return AccountConstants.ENTRY_KEY_STD_2_SPE_TRANS_PRE+feeFlag+feeStlMode;
        }
        //专用账户转账到标准账户
        else if(tranCode.equals(TranCode.ACC_TRANS_SPE_2_STD.getCode())){
            return AccountConstants.ENTRY_KEY_SPE_2_STD_TRANS_PRE+feeFlag+feeStlMode;
        }
        //专用账户转账到专用账户
        else if(tranCode.equals(TranCode.ACC_TRANS_SPE_2_SPE.getCode())){
            return AccountConstants.ENTRY_KEY_SPE_2_SPE_TRANS_PRE+feeFlag+feeStlMode;
        }
        //个人账户标准账户充值
        else if(tranCode.equals(TranCode.INDIVIDUAL_CHARGE.getCode())){
            return AccountConstants.ENTRY_KEY_STD_CHARGE_PRE+blnMode+feeFlag+feeStlMode;
        }
        //个人账户专用账户充值
        else if(tranCode.equals(TranCode.INDIVIDUAL_SPECIAL_CHARGE.getCode())){
            return AccountConstants.ENTRY_KEY_SPE_CHARGE_PRE+blnMode+feeFlag+feeStlMode;
        } 
        else{
        	throw new BizException("tranCode 为："+tranCode+"entryKey未配置");
        }
    }

}
