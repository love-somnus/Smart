package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnTransfer extends Entity {
	
	private static final long serialVersionUID = 1L;
	
    private String transferId;

    private Date accDate;

    private String merCode;

    private String merName;

    private String merAccCode;

    private String ccyCode;

    private BigDecimal tranAmount;

    private BigDecimal curBal;

    private BigDecimal reserveBal;

    private BigDecimal freezeBal;

    private String status;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;
    private String batchNo;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId == null ? null : transferId.trim();
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode == null ? null : merCode.trim();
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName == null ? null : merName.trim();
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

    public BigDecimal getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(BigDecimal tranAmount) {
        this.tranAmount = tranAmount;
    }

    public BigDecimal getCurBal() {
        return curBal;
    }

    public void setCurBal(BigDecimal curBal) {
        this.curBal = curBal;
    }

    public BigDecimal getReserveBal() {
        return reserveBal;
    }

    public void setReserveBal(BigDecimal reserveBal) {
        this.reserveBal = reserveBal;
    }

    public BigDecimal getFreezeBal() {
        return freezeBal;
    }

    public void setFreezeBal(BigDecimal freezeBal) {
        this.freezeBal = freezeBal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }
}