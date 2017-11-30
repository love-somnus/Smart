package com.somnus.smart.base.domain;

import java.math.BigDecimal;

public class TrnFreeze  extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String freezeNo;

    private String appTranNo;

    private String merAccCode;

    private String ccyCode;
    
    /** 冻结金额_冻结金额 */
    private BigDecimal tranAmt;
    
    /** 已解冻金额_余额冻结时才起作用 */
    private BigDecimal thawAmt;

    private String freezeType;

    private String initialFreezeType;

    private String bizType;

    private String status;

    private String voucherNo;

    private String tranRemark;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getFreezeNo() {
        return freezeNo;
    }

    public void setFreezeNo(String freezeNo) {
        this.freezeNo = freezeNo == null ? null : freezeNo.trim();
    }

    public String getAppTranNo() {
        return appTranNo;
    }

    public void setAppTranNo(String appTranNo) {
        this.appTranNo = appTranNo == null ? null : appTranNo.trim();
    }

    public String getMerAccCode() {
        return merAccCode;
    }

    public void setMerAccCode(String merAccCode) {
        this.merAccCode = merAccCode == null ? null : merAccCode.trim();
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode == null ? null : ccyCode.trim();
    }
    /**
     * 冻结金额_冻结金额
     * @return
     */
    public BigDecimal getTranAmt() {
        return tranAmt;
    }
    /**
     * 冻结金额_冻结金额
     * 
     */
    public void setTranAmt(BigDecimal tranAmt) {
        this.tranAmt = tranAmt;
    }
    /**
     * 解冻金额_解冻金额
     * @return
     */
    public BigDecimal getThawAmt() {
        return thawAmt;
    }
    /**
     * 解冻金额_解冻金额
     * 
     */
    public void setThawAmt(BigDecimal thawAmt) {
        this.thawAmt = thawAmt;
    }

    public String getFreezeType() {
        return freezeType;
    }

    public void setFreezeType(String freezeType) {
        this.freezeType = freezeType == null ? null : freezeType.trim();
    }

    public String getInitialFreezeType() {
        return initialFreezeType;
    }

    public void setInitialFreezeType(String initialFreezeType) {
        this.initialFreezeType = initialFreezeType == null ? null : initialFreezeType.trim();
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo == null ? null : voucherNo.trim();
    }

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark == null ? null : tranRemark.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }
}