package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class DiffTraninfo extends Entity {

	private static final long serialVersionUID = 1L;
	
    private String     errNo;

    private Date       accDate;

    private String     errTranNo;

    private String     ipsBillNo;

    private String     appTranNo;

    private String     tranType;

    private BigDecimal tranAmt;

    private String     errKind;

    private String     errCode;

    private String     errRemark;

    private String     status;

    private String     createTime;

    private String     createBy;

    private String     modifyTime;

    private String     modifyBy;

    public String getErrNo() {
        return errNo;
    }

    public void setErrNo(String errNo) {
        this.errNo = errNo;
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public String getErrTranNo() {
        return errTranNo;
    }

    public void setErrTranNo(String errTranNo) {
        this.errTranNo = errTranNo;
    }

    public String getIpsBillNo() {
        return ipsBillNo;
    }

    public void setIpsBillNo(String ipsBillNo) {
        this.ipsBillNo = ipsBillNo;
    }

    public String getAppTranNo() {
        return appTranNo;
    }

    public void setAppTranNo(String appTranNo) {
        this.appTranNo = appTranNo;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public BigDecimal getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(BigDecimal tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getErrKind() {
        return errKind;
    }

    public void setErrKind(String errKind) {
        this.errKind = errKind;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrRemark() {
        return errRemark;
    }

    public void setErrRemark(String errRemark) {
        this.errRemark = errRemark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}