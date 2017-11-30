package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnTransactionHis  extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String accTranNo;

    private String sysCode;

    private String machineNo;

    private String channelNo;

    private String terminalNo;

    private String ipsBillNo;

    private String appTranNo;

    private String appBatchNo;

    private String cusTranNo;

    private String chlTranNo;

    private String prdCode;

    private String tranType;

    private String initiatorFlg;

    private String payeeCode;

    private String payeeAccCode;

    private String payeeType;

    private String payeeRelSubCode;

    private String payerCode;

    private String payerAccCode;

    private String payerType;

    private String payerRelSubCode;

    private String supplierCode;

    private String channelCode;

    private String ccyCode;

    private BigDecimal tranAmt;

    private BigDecimal ordAmt;

    private String isFee;

    private BigDecimal feeAmt;

    private String feeType;

    private BigDecimal feeRate;

    private String feeFlag;

    private String feeStlMode;

    private String thirdAccCode;

    private String refundFeeBk;

    private String bankAccCode;

    private BigDecimal bankCost;

    private String bankFeeType;

    private BigDecimal bankFeeRate;

    private Date appTranDate;

    private Date submitTime;

    private Date finnishTime;

    private Date accDate;

    private String isDeposit;

    private BigDecimal securityDeposit;

    private Date depositRefundDate;

    private String depositStatus;

    private BigDecimal returnedDepositAmt;

    private BigDecimal refundedAmt;

    private BigDecimal dishonoredAmt;

    private Date estAccTime;

    private String accMode;

    private String accStatus;

    private String status;

    private String blnMode;

    private String blnStatus;

    private Date canBlnDate;

    private Date blnDate;

    private String errMsg;

    private String voucherNo;

    private String oriAppTranNo;

    private String tranRemark;

    private String resv1;

    private String resv2;

    private String resv3;

    private String resv4;

    private String resv5;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
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

    public String getAppBatchNo() {
        return appBatchNo;
    }

    public void setAppBatchNo(String appBatchNo) {
        this.appBatchNo = appBatchNo;
    }

    public String getCusTranNo() {
        return cusTranNo;
    }

    public void setCusTranNo(String cusTranNo) {
        this.cusTranNo = cusTranNo;
    }

    public String getChlTranNo() {
        return chlTranNo;
    }

    public void setChlTranNo(String chlTranNo) {
        this.chlTranNo = chlTranNo;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getInitiatorFlg() {
        return initiatorFlg;
    }

    public void setInitiatorFlg(String initiatorFlg) {
        this.initiatorFlg = initiatorFlg;
    }

    public String getPayeeCode() {
        return payeeCode;
    }

    public void setPayeeCode(String payeeCode) {
        this.payeeCode = payeeCode;
    }

    public String getPayeeAccCode() {
        return payeeAccCode;
    }

    public void setPayeeAccCode(String payeeAccCode) {
        this.payeeAccCode = payeeAccCode;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getPayeeRelSubCode() {
        return payeeRelSubCode;
    }

    public void setPayeeRelSubCode(String payeeRelSubCode) {
        this.payeeRelSubCode = payeeRelSubCode;
    }

    public String getPayerCode() {
        return payerCode;
    }

    public void setPayerCode(String payerCode) {
        this.payerCode = payerCode;
    }

    public String getPayerAccCode() {
        return payerAccCode;
    }

    public void setPayerAccCode(String payerAccCode) {
        this.payerAccCode = payerAccCode;
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public String getPayerRelSubCode() {
        return payerRelSubCode;
    }

    public void setPayerRelSubCode(String payerRelSubCode) {
        this.payerRelSubCode = payerRelSubCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode;
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

    public String getIsFee() {
        return isFee;
    }

    public void setIsFee(String isFee) {
        this.isFee = isFee;
    }

    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String getFeeStlMode() {
        return feeStlMode;
    }

    public void setFeeStlMode(String feeStlMode) {
        this.feeStlMode = feeStlMode;
    }

    public String getThirdAccCode() {
        return thirdAccCode;
    }

    public void setThirdAccCode(String thirdAccCode) {
        this.thirdAccCode = thirdAccCode;
    }

    public String getRefundFeeBk() {
        return refundFeeBk;
    }

    public void setRefundFeeBk(String refundFeeBk) {
        this.refundFeeBk = refundFeeBk;
    }

    public String getBankAccCode() {
        return bankAccCode;
    }

    public void setBankAccCode(String bankAccCode) {
        this.bankAccCode = bankAccCode;
    }

    public BigDecimal getBankCost() {
        return bankCost;
    }

    public void setBankCost(BigDecimal bankCost) {
        this.bankCost = bankCost;
    }

    public String getBankFeeType() {
        return bankFeeType;
    }

    public void setBankFeeType(String bankFeeType) {
        this.bankFeeType = bankFeeType;
    }

    public BigDecimal getBankFeeRate() {
        return bankFeeRate;
    }

    public void setBankFeeRate(BigDecimal bankFeeRate) {
        this.bankFeeRate = bankFeeRate;
    }

    public Date getAppTranDate() {
        return appTranDate;
    }

    public void setAppTranDate(Date appTranDate) {
        this.appTranDate = appTranDate;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getFinnishTime() {
        return finnishTime;
    }

    public void setFinnishTime(Date finnishTime) {
        this.finnishTime = finnishTime;
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public String getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(String isDeposit) {
        this.isDeposit = isDeposit;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Date getDepositRefundDate() {
        return depositRefundDate;
    }

    public void setDepositRefundDate(Date depositRefundDate) {
        this.depositRefundDate = depositRefundDate;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public BigDecimal getReturnedDepositAmt() {
        return returnedDepositAmt;
    }

    public void setReturnedDepositAmt(BigDecimal returnedDepositAmt) {
        this.returnedDepositAmt = returnedDepositAmt;
    }

    public BigDecimal getRefundedAmt() {
        return refundedAmt;
    }

    public void setRefundedAmt(BigDecimal refundedAmt) {
        this.refundedAmt = refundedAmt;
    }

    public BigDecimal getDishonoredAmt() {
        return dishonoredAmt;
    }

    public void setDishonoredAmt(BigDecimal dishonoredAmt) {
        this.dishonoredAmt = dishonoredAmt;
    }

    public Date getEstAccTime() {
        return estAccTime;
    }

    public void setEstAccTime(Date estAccTime) {
        this.estAccTime = estAccTime;
    }

    public String getAccMode() {
        return accMode;
    }

    public void setAccMode(String accMode) {
        this.accMode = accMode;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBlnMode() {
        return blnMode;
    }

    public void setBlnMode(String blnMode) {
        this.blnMode = blnMode;
    }

    public String getBlnStatus() {
        return blnStatus;
    }

    public void setBlnStatus(String blnStatus) {
        this.blnStatus = blnStatus;
    }

    public Date getCanBlnDate() {
        return canBlnDate;
    }

    public void setCanBlnDate(Date canBlnDate) {
        this.canBlnDate = canBlnDate;
    }

    public Date getBlnDate() {
        return blnDate;
    }

    public void setBlnDate(Date blnDate) {
        this.blnDate = blnDate;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getOriAppTranNo() {
        return oriAppTranNo;
    }

    public void setOriAppTranNo(String oriAppTranNo) {
        this.oriAppTranNo = oriAppTranNo;
    }

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark;
    }

    public String getResv1() {
        return resv1;
    }

    public void setResv1(String resv1) {
        this.resv1 = resv1;
    }

    public String getResv2() {
        return resv2;
    }

    public void setResv2(String resv2) {
        this.resv2 = resv2;
    }

    public String getResv3() {
        return resv3;
    }

    public void setResv3(String resv3) {
        this.resv3 = resv3;
    }

    public String getResv4() {
        return resv4;
    }

    public void setResv4(String resv4) {
        this.resv4 = resv4;
    }

    public String getResv5() {
        return resv5;
    }

    public void setResv5(String resv5) {
        this.resv5 = resv5;
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