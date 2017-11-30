package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnTranRefund extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String refTranNo;

    private String accTranNo;

    private String oriAppTranNo;

    private String oriMerBillNo;

    private BigDecimal oriTranAmt;

    private Date oriBillDate;

    private String oriSupplierBillNo;

    private String oriRefrenceNo;

    private String oriAuthNo;

    private BigDecimal refundAmt;

    private String channelCode;

    private String cardNo;

    private String cardNoEx;

    private String cardholder;

    private Date appRefundDate;

    private String billRemark;

    private String refundArg;

    private String refType;

    private String status;

    private String submitTime;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    private String bizType;

    public String getRefTranNo() {
        return refTranNo;
    }

    public void setRefTranNo(String refTranNo) {
        this.refTranNo = refTranNo == null ? null : refTranNo.trim();
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
    }

    public String getOriAppTranNo() {
        return oriAppTranNo;
    }

    public void setOriAppTranNo(String oriAppTranNo) {
        this.oriAppTranNo = oriAppTranNo == null ? null : oriAppTranNo.trim();
    }

    public String getOriMerBillNo() {
        return oriMerBillNo;
    }

    public void setOriMerBillNo(String oriMerBillNo) {
        this.oriMerBillNo = oriMerBillNo == null ? null : oriMerBillNo.trim();
    }

    public BigDecimal getOriTranAmt() {
        return oriTranAmt;
    }

    public void setOriTranAmt(BigDecimal oriTranAmt) {
        this.oriTranAmt = oriTranAmt;
    }

    public Date getOriBillDate() {
        return oriBillDate;
    }

    public void setOriBillDate(Date oriBillDate) {
        this.oriBillDate = oriBillDate;
    }

    public String getOriSupplierBillNo() {
        return oriSupplierBillNo;
    }

    public void setOriSupplierBillNo(String oriSupplierBillNo) {
        this.oriSupplierBillNo = oriSupplierBillNo == null ? null : oriSupplierBillNo.trim();
    }

    public String getOriRefrenceNo() {
        return oriRefrenceNo;
    }

    public void setOriRefrenceNo(String oriRefrenceNo) {
        this.oriRefrenceNo = oriRefrenceNo == null ? null : oriRefrenceNo.trim();
    }

    public String getOriAuthNo() {
        return oriAuthNo;
    }

    public void setOriAuthNo(String oriAuthNo) {
        this.oriAuthNo = oriAuthNo == null ? null : oriAuthNo.trim();
    }

    public BigDecimal getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(BigDecimal refundAmt) {
        this.refundAmt = refundAmt;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getCardNoEx() {
        return cardNoEx;
    }

    public void setCardNoEx(String cardNoEx) {
        this.cardNoEx = cardNoEx == null ? null : cardNoEx.trim();
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder == null ? null : cardholder.trim();
    }

    public Date getAppRefundDate() {
        return appRefundDate;
    }

    public void setAppRefundDate(Date appRefundDate) {
        this.appRefundDate = appRefundDate;
    }

    public String getBillRemark() {
        return billRemark;
    }

    public void setBillRemark(String billRemark) {
        this.billRemark = billRemark == null ? null : billRemark.trim();
    }

    public String getRefundArg() {
        return refundArg;
    }

    public void setRefundArg(String refundArg) {
        this.refundArg = refundArg == null ? null : refundArg.trim();
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType == null ? null : refType.trim();
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }
    
}