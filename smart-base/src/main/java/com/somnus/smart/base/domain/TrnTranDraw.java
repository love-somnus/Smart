package com.somnus.smart.base.domain;

public class TrnTranDraw extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String draTranNo;

    private String accTranNo;

    private String payeeBankCode;

    private String payeeBraBankCode;

    private String payeeBraBankName;

    private String payeeBankAccCode;

    private String payeeBankAccName;

    private String payeeBankAccType;

    private String status;

    private String submitTime;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getDraTranNo() {
        return draTranNo;
    }

    public void setDraTranNo(String draTranNo) {
        this.draTranNo = draTranNo == null ? null : draTranNo.trim();
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
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