package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class PrdRecognizeDelay extends Entity {
	
	private static final long serialVersionUID = 1L;

	private BigDecimal delayId;

    private String systemCode;

    private String basicPrdCode;

    private BigDecimal merLevel;

    private String recognizeDelay;

    private String remark;

    private Integer version;

    private String status;

    private String operFlag;

    private String effectDate;

    private String loseEffectDate;

    private String modifyBy;

    private Date modifyTime;

    public BigDecimal getDelayId() {
        return delayId;
    }

    public void setDelayId(BigDecimal delayId) {
        this.delayId = delayId;
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

    public BigDecimal getMerLevel() {
        return merLevel;
    }

    public void setMerLevel(BigDecimal merLevel) {
        this.merLevel = merLevel;
    }

    public String getRecognizeDelay() {
        return recognizeDelay;
    }

    public void setRecognizeDelay(String recognizeDelay) {
        this.recognizeDelay = recognizeDelay == null ? null : recognizeDelay.trim();
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