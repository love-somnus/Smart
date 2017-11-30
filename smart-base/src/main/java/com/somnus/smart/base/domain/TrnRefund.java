package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnRefund extends Entity {
	
	private static final long serialVersionUID = 1L;
	
    private String refundId;

    private String sysCode;

    private String billNo;

    private String merBillNo;

    private String appTranNo;

    private String payTranNo;

    private String prdCode;

    private String merCode;

    private String merName;

    private String merAccCode;

    private String merAccName;

    private String merBizKind;

    private String tranType;

    private String ccyCode;

    private BigDecimal tranAmount;

    private Date tranDate;

    private String bizType;

    private String payeeBankAccCode;

    private String payeeBankAccCodeEx;

    private String payeeBankAccName;

    private String payeeBankAccType;

    private String payerBankCode;

    private String payerBankName;

    private String isOnline;

    private String channelCode;

    private String channelName;

    private String isExpired;

    private String fileId;

    private Date tabTime;

    private String oriSupplierBillNo;

    private String oriAppTranNo;

    private String oriRefrenceNo;

    private String oriAuthNo;

    private Date oriBillDate;

    private BigDecimal oriTranAmount;

    private String ipsAccount;

    private String posId;

    private String refundArg;

    private String refType;

    private BigDecimal bankCost;

    private String status;

    private String finishFlag;

    private String finishNo;

    private Date finishDate;

    private String errorMessage;

    private String remark;

    private String resv1;

    private String resv2;

    private String resv3;

    private String resv4;

    private String resv5;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;
    
    private String payeeBankCode;

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId == null ? null : refundId.trim();
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    public String getMerBillNo() {
        return merBillNo;
    }

    public void setMerBillNo(String merBillNo) {
        this.merBillNo = merBillNo == null ? null : merBillNo.trim();
    }

    public String getAppTranNo() {
        return appTranNo;
    }

    public void setAppTranNo(String appTranNo) {
        this.appTranNo = appTranNo == null ? null : appTranNo.trim();
    }

    public String getPayTranNo() {
        return payTranNo;
    }

    public void setPayTranNo(String payTranNo) {
        this.payTranNo = payTranNo == null ? null : payTranNo.trim();
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode == null ? null : prdCode.trim();
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

    public String getMerAccName() {
        return merAccName;
    }

    public void setMerAccName(String merAccName) {
        this.merAccName = merAccName == null ? null : merAccName.trim();
    }

    public String getMerBizKind() {
        return merBizKind;
    }

    public void setMerBizKind(String merBizKind) {
        this.merBizKind = merBizKind == null ? null : merBizKind.trim();
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType == null ? null : tranType.trim();
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

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }

    public String getPayeeBankAccCode() {
        return payeeBankAccCode;
    }

    public void setPayeeBankAccCode(String payeeBankAccCode) {
        this.payeeBankAccCode = payeeBankAccCode == null ? null : payeeBankAccCode.trim();
    }

    public String getPayeeBankAccCodeEx() {
        return payeeBankAccCodeEx;
    }

    public void setPayeeBankAccCodeEx(String payeeBankAccCodeEx) {
        this.payeeBankAccCodeEx = payeeBankAccCodeEx == null ? null : payeeBankAccCodeEx.trim();
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

    public String getPayerBankCode() {
        return payerBankCode;
    }

    public void setPayerBankCode(String payerBankCode) {
        this.payerBankCode = payerBankCode == null ? null : payerBankCode.trim();
    }

    public String getPayerBankName() {
        return payerBankName;
    }

    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName == null ? null : payerBankName.trim();
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline == null ? null : isOnline.trim();
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired == null ? null : isExpired.trim();
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    public Date getTabTime() {
        return tabTime;
    }

    public void setTabTime(Date tabTime) {
        this.tabTime = tabTime;
    }

    public String getOriSupplierBillNo() {
        return oriSupplierBillNo;
    }

    public void setOriSupplierBillNo(String oriSupplierBillNo) {
        this.oriSupplierBillNo = oriSupplierBillNo == null ? null : oriSupplierBillNo.trim();
    }

    public String getOriAppTranNo() {
        return oriAppTranNo;
    }

    public void setOriAppTranNo(String oriAppTranNo) {
        this.oriAppTranNo = oriAppTranNo == null ? null : oriAppTranNo.trim();
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

    public Date getOriBillDate() {
        return oriBillDate;
    }

    public void setOriBillDate(Date oriBillDate) {
        this.oriBillDate = oriBillDate;
    }

    public BigDecimal getOriTranAmount() {
        return oriTranAmount;
    }

    public void setOriTranAmount(BigDecimal oriTranAmount) {
        this.oriTranAmount = oriTranAmount;
    }

    public String getIpsAccount() {
        return ipsAccount;
    }

    public void setIpsAccount(String ipsAccount) {
        this.ipsAccount = ipsAccount == null ? null : ipsAccount.trim();
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId == null ? null : posId.trim();
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

    public BigDecimal getBankCost() {
        return bankCost;
    }

    public void setBankCost(BigDecimal bankCost) {
        this.bankCost = bankCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(String finishFlag) {
        this.finishFlag = finishFlag == null ? null : finishFlag.trim();
    }

    public String getFinishNo() {
        return finishNo;
    }

    public void setFinishNo(String finishNo) {
        this.finishNo = finishNo == null ? null : finishNo.trim();
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getResv1() {
        return resv1;
    }

    public void setResv1(String resv1) {
        this.resv1 = resv1 == null ? null : resv1.trim();
    }

    public String getResv2() {
        return resv2;
    }

    public void setResv2(String resv2) {
        this.resv2 = resv2 == null ? null : resv2.trim();
    }

    public String getResv3() {
        return resv3;
    }

    public void setResv3(String resv3) {
        this.resv3 = resv3 == null ? null : resv3.trim();
    }

    public String getResv4() {
        return resv4;
    }

    public void setResv4(String resv4) {
        this.resv4 = resv4 == null ? null : resv4.trim();
    }

    public String getResv5() {
        return resv5;
    }

    public void setResv5(String resv5) {
        this.resv5 = resv5 == null ? null : resv5.trim();
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

    public String getPayeeBankCode() {
        return payeeBankCode;
    }

    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode == null ? null : payeeBankCode.trim();
    }
}