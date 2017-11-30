package com.somnus.smart.domain.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;

import com.somnus.smart.base.dao.CusSubAccInfoDao;
import com.somnus.smart.base.domain.CoreAccountItems;
import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.TrnAccDetail;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;
import com.somnus.smart.service.MeracctInnerService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.CheckUtil;
import com.somnus.smart.service.common.CusSubAccInfoUtil;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

/** 账户 */
public class SubAccInfo extends CusSubaccinfo implements DomainModel<SubAccInfo, CusSubaccinfo> {

	private static final long serialVersionUID = 1L;

	private static CusSubAccInfoDao      cusSubAccInfoDao;

    private static MeracctInnerService   meracctInnerService;

    private static MessageSourceAccessor messageSourceAccessor;

    private static Logger                LOGGER = LoggerFactory.getLogger(SubAccInfo.class);

    private SubAccInfo() { }

    public static SubAccInfo getInstance() {
        return (SubAccInfo) DomainHelper.getDomainInstance(SubAccInfo.class);
    }

    public static void init(ApplicationContext ctx) {
        cusSubAccInfoDao = ctx.getBean(CusSubAccInfoDao.class);
        messageSourceAccessor = ctx.getBean(MessageSourceAccessor.class);
        meracctInnerService=ctx.getBean(MeracctInnerService.class);
    }

    /**
     * 根据TrnTransaction返回Transaction
     * 
     * @param model
     * @return
     */
    public static SubAccInfo getSubAccInfo(CusSubaccinfo model) {
        SubAccInfo subAccInfo = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(subAccInfo, model);
        }
        return subAccInfo;
    }

    /**
     * 获取实际账户，如果不存在就创建
     * 
     * @param relSubCode
     * @param merAccCode
     * @param ccyCode
     * @return
     */
    public SubAccInfo getSubAccInfo(String subType, String relSubCode, String merAccCode, String ccyCode) {
        
        CusSubaccinfo cusSubaccinfo = null;
        if(merAccCode == null){
            cusSubaccinfo = cusSubAccInfoDao.selectBycode(CusSubAccInfoUtil.getTableByAccCodeLength(0), relSubCode, merAccCode, ccyCode);
        }else{
            cusSubaccinfo = cusSubAccInfoDao.selectBycode(CusSubAccInfoUtil.getTableByAccCodeLength(merAccCode.length()), relSubCode, merAccCode, ccyCode);
        }
        if ((cusSubaccinfo == null) && (merAccCode == null)) {
            LOGGER.info("内部账户不存在，自动开户relSubCode:{},merAccCode:{},ccyCode:{}", new Object[] { relSubCode, merAccCode, ccyCode });
            try {
                cusSubaccinfo = meracctInnerService.txnMerInnerCreate(relSubCode, ccyCode);
            } catch (Exception e) {
                LOGGER.error("创建账户失败", e);
                throw new BizException(messageSourceAccessor.getMessage(MsgCodeList.ERROR_505019));
            }
        } else if (cusSubaccinfo == null) {
            LOGGER.error("内部账户不存在relSubCode:{},merAccCode:{},ccyCode:{}", new Object[] { relSubCode, merAccCode, ccyCode });
            throw new BizException(messageSourceAccessor.getMessage(MsgCodeList.ERROR_302001, new Object[] { merAccCode + "对应的实际账户" }));
        }
        return SubAccInfo.getSubAccInfo(cusSubaccinfo);
    }

    /**
     * 业务冻结
     * 
     * @param subAccCode
     * @param tranAmt
     */
    public static void addBizFreezeBal(String subAccCode, BigDecimal tranAmt) {
        cusSubAccInfoDao.addBizFreezeBal(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode),subAccCode, tranAmt);
    }

    /**
     * 监管冻结
     * 
     * @param subAccCode
     * @param tranAmt
     */
    public static void addMagFreezeBal(String subAccCode, BigDecimal tranAmt) {
        cusSubAccInfoDao.addMagFreezeBal(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode),subAccCode, tranAmt);
    }

    /**
     * 新增应付冻结金额
     * 
     * @param subAccCode
     * @param tranAmt
     */
    public static void addPayableFreezeBal(String subAccCode, BigDecimal tranAmt) {
        int count = cusSubAccInfoDao.addPayableFreezeBal(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode),subAccCode, tranAmt);
        if (count == 0) {
            throw new BizException("账户余额不足");
        }
    }

    /**
     * 更新余额
     * 
     * @return
     */
    public CoreAccountItems updateBal(AccDetail accDetail, boolean checkRed) {

        CoreAccountItems coreAccountItems = new CoreAccountItems();
        try {
            // 传入信息审核
            checkTrnAccDetailToAcct(accDetail);
            // 验证是否已记账
            boolean existAccDetail = accDetail.checkExistAccDetail(accDetail);
            if (existAccDetail) {
                coreAccountItems.setReqMsgCode(MsgCodeList.SUCCESS_000000);
                return coreAccountItems;
            }
            // 根据账户时间设置起初余额
            if (accDetail.getAccDate().after(this.getAccDate())) {// 如果交易账户账务日期大于账户余额账务日期，已经日切到第二天，余额未更新
                // 交易帐务日期大于账户日期1
                if (DateUtil.getBetweenDays(accDetail.getAccDate(), this.getAccDate()) > 1) {// 如果交易账户账务日期大于账户余额账务日期一天，
                    this.setLastInitialBal(BigDecimal.ZERO.add(this.getCurBal()));
                } else {
                    this.setLastInitialBal(BigDecimal.ZERO.add(this.getInitialBal()));
                }
                this.setAccDate(accDetail.getAccDate());
                this.setInitialBal(BigDecimal.ZERO.add(this.getCurBal()));
                if (accDetail.getCdFlag().equals(BasConstants.CD_FLAG_D)) {
                    this.setDebitAmt(BigDecimal.ZERO.add(accDetail.getTranAmt()));
                    this.setCreditAmt(BigDecimal.ZERO);
                } else {
                    this.setDebitAmt(BigDecimal.ZERO);
                    this.setCreditAmt(BigDecimal.ZERO.add(accDetail.getTranAmt()));
                }
            } else if (accDetail.getAccDate().before(this.getAccDate())) {// 如果交易账户账务日期小于账户余额账务日期
                BigDecimal initialBal = BigDecimal.ZERO;
                initialBal = this.getInitialBal();
                if (this.getBalDir().equals(accDetail.getCdFlag())) {
                    initialBal = initialBal.add(accDetail.getTranAmt());
                } else {
                    initialBal = initialBal.subtract(accDetail.getTranAmt());
                }
                this.setInitialBal(initialBal);
            } else {// 如果是同一天
                    // 借贷方余额变动
                if (accDetail.getCdFlag().equals(BasConstants.CD_FLAG_D)) {
                    this.setDebitAmt(this.getDebitAmt().add(accDetail.getTranAmt()));
                } else {
                    this.setCreditAmt(this.getCreditAmt().add(accDetail.getTranAmt()));
                }
            }

            // 当前余额 = CurBal
            BigDecimal curBal = BigDecimal.ZERO;
            curBal = curBal.add(this.getCurBal());
            // 余额变动 - 增加|减少 如果余额科目性质的借贷方向和它出现借贷方向一致，那么余额就是加上发生额，否则就是减
            //另外整个功能账户体系里，只有三个才是借方科目类型(应收银行、银行成本、非银行成本)，其余全是贷方科目
            //也就是说上述三种，如果出现在excel左边（借方），基本都是相加，出现在右边，则是相减
            //其余的科目类型，出现在右边才都是相加，出现在左边都是相减
            if (this.getBalDir().equals(accDetail.getCdFlag())) {
                curBal = curBal.add(accDetail.getTranAmt());
            } else {
                curBal = curBal.subtract(accDetail.getTranAmt());
                if (BasConstants.OVER_FLAG_FALSE.equals(this.getOverFlag())) {
                    if (curBal.compareTo(BigDecimal.ZERO) < 0) {
                        LOGGER.error("TranAmt: " + accDetail.getTranAmt() + " CurBal:" + this.getCurBal() + " SubAccCode:" + this.getSubAccCode());
                        throw new BizException(MsgCodeList.ERROR_304001);
                    }
                }

                // 判断交易余额是否为负
                if (checkRed && BasConstants.REL_SUB_CODE_FREE.equals(this.getSubCode())) {
                    // 可用余额=当前余额-冻结金额-应付冻结金额
                    BigDecimal availableBal = curBal.subtract(this.getBizFreezeBal()).subtract(this.getMagFreezeBal())
                        .subtract(this.getPayableFreezeBal());
                    if (availableBal.compareTo(BigDecimal.ZERO) < 0) {
                        LOGGER.error(" availableBal TranAmt: " + accDetail.getTranAmt() + " CurBal:" + this.getCurBal() + " SubAccCode:"
                                     + this.getSubAccCode());
                        throw new BizException(MsgCodeList.ERROR_304001);
                    }
                }
            }
            accDetail.setCurBal(curBal);
            this.setCurBal(curBal);
            // 可用余额=当前余额-冻结金额-应付冻结金额
            BigDecimal availableBal = curBal.subtract(this.getBizFreezeBal()).subtract(this.getMagFreezeBal()).subtract(this.getPayableFreezeBal());
            if (availableBal.compareTo(BigDecimal.ZERO) < 0) {
                availableBal = BigDecimal.ZERO;
            }
            this.setAvailBal(availableBal);

            // 设置更新人更新日期
            this.setModifyBy(accDetail.getCreateBy());
            this.setModifyTime(accDetail.getCreateTime());

            // 更新余额
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableName", CusSubAccInfoUtil.getTableBySubAccCode(this.getSubAccCode()));
            map.put("curBal", this.getCurBal());
            map.put("accDate", this.getAccDate());
            map.put("availBal", this.getAvailBal());
            map.put("bizFreezeBal", this.getBizFreezeBal());
            map.put("magFreezeBal", this.getMagFreezeBal());
            map.put("payableFreezeBal", this.getPayableFreezeBal());
            map.put("initialBal", this.getInitialBal());
            map.put("debitAmt", this.getDebitAmt());
            map.put("creditAmt", this.getCreditAmt());
            map.put("modifyTime", this.getModifyTime());
            map.put("modifyBy", this.getModifyBy());
            map.put("lastInitialBal", this.getLastInitialBal());
            map.put("subAccCode", this.getSubAccCode());
            int succnt = cusSubAccInfoDao.updateAcctBal(map);

            if (succnt != 1) {
                // 余额更新失败
                throw new BizException(MsgCodeList.ERROR_304002);
            }

            coreAccountItems.setReqMsgCode(MsgCodeList.SUCCESS_000000);
            return coreAccountItems;
        } catch (BizException e) {
            LOGGER.error(e.getMessage(), e);
            coreAccountItems.setReqMsgCode(e.getMessage());
            coreAccountItems.setReqMsg(e.getLocalizedMessage());
            return coreAccountItems;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            coreAccountItems.setReqMsgCode(MsgCodeList.ERROR_999999);
            coreAccountItems.setReqMsg(e.getMessage());
            return coreAccountItems;
        } finally {

        }
    }

    /**
     * 审核账户明细内容
     * 
     * @param trnAccDetail
     *            账户明细
     */
    private void checkTrnAccDetailToAcct(TrnAccDetail trnAccDetail) {
        CheckUtil.isEmpty(trnAccDetail.getLedTranNo(), "台账流水号");
        CheckUtil.isEmpty(trnAccDetail.getSubIndex(), "科目顺序号");
        CheckUtil.isEmpty(trnAccDetail.getCdFlag(), "借贷方向");
        CheckUtil.isEmpty(trnAccDetail.getSubCode(), "科目号");
        CheckUtil.isEmpty(trnAccDetail.getTranAmt().toString(), "发生额");
        CheckUtil.isEmpty(trnAccDetail.getRelSubCode(), "实际科目号");
        CheckUtil.isEmpty(trnAccDetail.getAccDate(), "记账处理日期");
        CheckUtil.isEmpty(trnAccDetail.getCcyCode(), "币种");
    }

    public static void subtractBizFreezeBal(String subAccCode, BigDecimal tranAmt) {
        int count = cusSubAccInfoDao.addBizFreezeBal(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode),subAccCode, tranAmt.multiply(new BigDecimal(-1)));
        if (count == 0) {
            throw new BizException("解冻失败,解冻金额超额");
        }
    }

    public static void subtractMagFreezeBal(String subAccCode, BigDecimal tranAmt) {
        int count = cusSubAccInfoDao.addMagFreezeBal(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode),subAccCode, tranAmt.multiply(new BigDecimal(-1)));
        if (count == 0) {
            throw new BizException("解冻失败,解冻金额超额");
        }
    }

    /**
     * 减少应付冻结金额
     * 
     * @param subAccCode
     * @param tranAmt
     */
    public static void subtracPayableFreezeBal(String subAccCode, BigDecimal tranAmt) {
        int count = cusSubAccInfoDao.addPayableFreezeBal(CusSubAccInfoUtil.getTableBySubAccCode(subAccCode),subAccCode, tranAmt.multiply(new BigDecimal(-1)));
        if (count == 0) {
            throw new BizException("解冻失败,解冻金额超额");
        }
    }

    public BigDecimal getAvailableBal() {
        //计算可用余额 可用余额=当前余额-冻结余额-监管冻结余额
        return this.getCurBal().subtract(this.getBizFreezeBal()).subtract(this.getMagFreezeBal()).subtract(this.getPayableFreezeBal());
    }

}
