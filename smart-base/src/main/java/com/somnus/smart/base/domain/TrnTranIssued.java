package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnTranIssued extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String issTranNo;

    private String accTranNo;

    private String ipsBillNo;

    private String cusTranNo;

    private String appTranNo;

    private Date appTranDate;

    private BigDecimal tranAmt;

    private BigDecimal ordAmt;

    private BigDecimal feeAmt;

    private String payeeCode;

    private String payeeAccCode;

    private String payeeType;

    private String payeeBankCode;

    private String payeeBraBankCode;

    private String payeeBraBankName;

    private String payeeBankAccCode;

    private String payeeBankAccName;

    private String payeeBankAccType;

    private String status;

    private String tranRemark;

    private String submitTime;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getIssTranNo() {
        return issTranNo;
    }

    public void setIssTranNo(String issTranNo) {
        this.issTranNo = issTranNo == null ? null : issTranNo.trim();
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
    }

    public String getIpsBillNo() {
        return ipsBillNo;
    }

    public void setIpsBillNo(String ipsBillNo) {
        this.ipsBillNo = ipsBillNo == null ? null : ipsBillNo.trim();
    }

    public String getCusTranNo() {
        return cusTranNo;
    }

    public void setCusTranNo(String cusTranNo) {
        this.cusTranNo = cusTranNo == null ? null : cusTranNo.trim();
    }

    public String getAppTranNo() {
        return appTranNo;
    }

    public void setAppTranNo(String appTranNo) {
        this.appTranNo = appTranNo == null ? null : appTranNo.trim();
    }

    public Date getAppTranDate() {
        return appTranDate;
    }

    public void setAppTranDate(Date appTranDate) {
        this.appTranDate = appTranDate;
    }

    public BigDecimal getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(BigDecimal tranAmt) {
        this.tranAmt = tranAmt;
    }

    public BigDecimal getOrdAmt() {
        return ordAmt;
    }

    public void setOrdAmt(BigDecimal ordAmt) {
        this.ordAmt = ordAmt;
    }

    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getPayeeCode() {
        return payeeCode;
    }

    public void setPayeeCode(String payeeCode) {
        this.payeeCode = payeeCode == null ? null : payeeCode.trim();
    }

    public String getPayeeAccCode() {
        return payeeAccCode;
    }

    public void setPayeeAccCode(String payeeAccCode) {
        this.payeeAccCode = payeeAccCode == null ? null : payeeAccCode.trim();
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType == null ? null : payeeType.trim();
    }

    public String getPayeeBankCode() {
        return payeeBankCode;
    }

    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode == null ? null : payeeBankCode.trim();
    }

    public String getPayeeBraBankCode() {
        return payeeBraBankCode;
    }

    public void setPayeeBraBankCode(String payeeBraBankCode) {
        this.payeeBraBankCode = payeeBraBankCode == null ? null : payeeBraBankCode.trim();
    }

    public String getPayeeBraBankName() {
        return payeeBraBankName;
    }

    public void setPayeeBraBankName(String payeeBraBankName) {
        this.payeeBraBankName = payeeBraBankName == null ? null : payeeBraBankName.trim();
    }

    public String getPayeeBankAccCode() {
        return payeeBankAccCode;
    }

    public void setPayeeBankAccCode(String payeeBankAccCode) {
        this.payeeBankAccCode = payeeBankAccCode == null ? null : payeeBankAccCode.trim();
    }

    public String getPayeeBankAccName() {
        return payeeBankAccName;
    }

    public void setPayeeBankAccName(String payeeBankAccName) {
        this.payeeBankAccName = payeeBankAccName == null ? null : payeeBankAccName.trim();
    }

    public String getPayeeBankAccType() {
        return payeeBankAccType;
    }

    public void setPayeeBankAccType(String payeeBankAccType) {
        this.payeeBankAccType = payeeBankAccType == null ? null : payeeBankAccType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark == null ? null : tranRemark.trim();
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime == null ? null : submitTime.trim();
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