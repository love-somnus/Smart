package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class MerBasicPrdCfg extends Entity {
	
	private static final long serialVersionUID = 1L;

	private BigDecimal basicPrdCfgId;

    private String acctCode;

    private String systemCode;

    private String basicPrdCode;

    private Long activationFee;

    private Long annualFee;

    private Long additionalFee;

    private Short allowGw;

    private Short allowDirect;

    private Short allowWs;

    private Short allowMerBackend;

    private Short allowShortMsg;

    private Short allowIvr;

    private Short allowMobileApp;

    private BigDecimal recognizePeriod;

    private String feeStlMode;

    private String refundFeeBk;

    private String ladderMode;

    private String feeTarget;

    private String feeTargetAcctNo;

    private Short depositPer;

    private Short depositLimit;

    private String remark;

    private Integer version;

    private String status;

    private String operFlag;

    private String effectDate;

    private String loseEffectDate;

    private String modifyBy;

    private Date modifyTime;

    public BigDecimal getBasicPrdCfgId() {
        return basicPrdCfgId;
    }

    public void setBasicPrdCfgId(BigDecimal basicPrdCfgId) {
        this.basicPrdCfgId = basicPrdCfgId;
    }

    public String getAcctCode() {
        return acctCode;
    }

    public void setAcctCode(String acctCode) {
        this.acctCode = acctCode == null ? null : acctCode.trim();
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    public String getBasicPrdCode() {
        return basicPrdCode;
    }

    public void setBasicPrdCode(String basicPrdCode) {
        this.basicPrdCode = basicPrdCode == null ? null : basicPrdCode.trim();
    }

    public Long getActivationFee() {
        return activationFee;
    }

    public void setActivationFee(Long activationFee) {
        this.activationFee = activationFee;
    }

    public Long getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(Long annualFee) {
        this.annualFee = annualFee;
    }

    public Long getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(Long additionalFee) {
        this.additionalFee = additionalFee;
    }

    public Short getAllowGw() {
        return allowGw;
    }

    public void setAllowGw(Short allowGw) {
        this.allowGw = allowGw;
    }

    public Short getAllowDirect() {
        return allowDirect;
    }

    public void setAllowDirect(Short allowDirect) {
        this.allowDirect = allowDirect;
    }

    public Short getAllowWs() {
        return allowWs;
    }

    public void setAllowWs(Short allowWs) {
        this.allowWs = allowWs;
    }

    public Short getAllowMerBackend() {
        return allowMerBackend;
    }

    public void setAllowMerBackend(Short allowMerBackend) {
        this.allowMerBackend = allowMerBackend;
    }

    public Short getAllowShortMsg() {
        return allowShortMsg;
    }

    public void setAllowShortMsg(Short allowShortMsg) {
        this.allowShortMsg = allowShortMsg;
    }

    public Short getAllowIvr() {
        return allowIvr;
    }

    public void setAllowIvr(Short allowIvr) {
        this.allowIvr = allowIvr;
    }

    public Short getAllowMobileApp() {
        return allowMobileApp;
    }

    public void setAllowMobileApp(Short allowMobileApp) {
        this.allowMobileApp = allowMobileApp;
    }

    public BigDecimal getRecognizePeriod() {
        return recognizePeriod;
    }

    public void setRecognizePeriod(BigDecimal recognizePeriod) {
        this.recognizePeriod = recognizePeriod;
    }

    public String getFeeStlMode() {
        return feeStlMode;
    }

    public void setFeeStlMode(String feeStlMode) {
        this.feeStlMode = feeStlMode == null ? null : feeStlMode.trim();
    }

    public String getRefundFeeBk() {
        return refundFeeBk;
    }

    public void setRefundFeeBk(String refundFeeBk) {
        this.refundFeeBk = refundFeeBk == null ? null : refundFeeBk.trim();
    }

    public String getLadderMode() {
        return ladderMode;
    }

    public void setLadderMode(String ladderMode) {
        this.ladderMode = ladderMode == null ? null : ladderMode.trim();
    }

    public String getFeeTarget() {
        return feeTarget;
    }

    public void setFeeTarget(String feeTarget) {
        this.feeTarget = feeTarget == null ? null : feeTarget.trim();
    }

    public String getFeeTargetAcctNo() {
        return feeTargetAcctNo;
    }

    public void setFeeTargetAcctNo(String feeTargetAcctNo) {
        this.feeTargetAcctNo = feeTargetAcctNo == null ? null : feeTargetAcctNo.trim();
    }

    public Short getDepositPer() {
        return depositPer;
    }

    public void setDepositPer(Short depositPer) {
        this.depositPer = depositPer;
    }

    public Short getDepositLimit() {
        return depositLimit;
    }

    public void setDepositLimit(Short depositLimit) {
        this.depositLimit = depositLimit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOperFlag() {
        return operFlag;
    }

    public void setOperFlag(String operFlag) {
        this.operFlag = operFlag == null ? null : operFlag.trim();
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate == null ? null : effectDate.trim();
    }

    public String getLoseEffectDate() {
        return loseEffectDate;
    }

    public void setLoseEffectDate(String loseEffectDate) {
        this.loseEffectDate = loseEffectDate == null ? null : loseEffectDate.trim();
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}