package com.somnus.smart.base.domain;

public class TrnTranrefuse  extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String refTranNo;

    private String accTranNo;

    private String cardNo;

    private String cardNoEx;

    private String cardholder;

    private String status;

    private String submitTime;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getRefTranNo() {
        return refTranNo;
    }

    public void setRefTranNo(String refTranNo) {
        this.refTranNo = refTranNo;
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNoEx() {
        return cardNoEx;
    }

    public void setCardNoEx(String cardNoEx) {
        this.cardNoEx = cardNoEx;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
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