package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnTransaction  extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String accTranNo;

    private String sysCode;

    private String machineNo;

    private String channelNo;

    private String terminalNo;

    private String tranCode;

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

    private String frozenFlag;

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
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo == null ? null : machineNo.trim();
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo == null ? null : channelNo.trim();
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo == null ? null : terminalNo.trim();
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getFee() {
        return isFee;
    }

    public void setFee(String fee) {
        isFee = fee;
    }

    public String getDeposit() {
        return isDeposit;
    }

    public void setDeposit(String deposit) {
        isDeposit = deposit;
    }

    public String getIpsBillNo() {
        return ipsBillNo;
    }

    public void setIpsBillNo(String ipsBillNo) {
        this.ipsBillNo = ipsBillNo == null ? null : ipsBillNo.trim();
    }

    public String getAppTranNo() {
        return appTranNo;
    }

    public void setAppTranNo(String appTranNo) {
        this.appTranNo = appTranNo == null ? null : appTranNo.trim();
    }

    public String getAppBatchNo() {
        return appBatchNo;
    }

    public void setAppBatchNo(String appBatchNo) {
        this.appBatchNo = appBatchNo == null ? null : appBatchNo.trim();
    }

    public String getCusTranNo() {
        return cusTranNo;
    }

    public void setCusTranNo(String cusTranNo) {
        this.cusTranNo = cusTranNo == null ? null : cusTranNo.trim();
    }

    public String getChlTranNo() {
        return chlTranNo;
    }

    public void setChlTranNo(String chlTranNo) {
        this.chlTranNo = chlTranNo == null ? null : chlTranNo.trim();
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode == null ? null : prdCode.trim();
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType == null ? null : tranType.trim();
    }

    public String getInitiatorFlg() {
        return initiatorFlg;
    }

    public void setInitiatorFlg(String initiatorFlg) {
        this.initiatorFlg = initiatorFlg == null ? null : initiatorFlg.trim();
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

    public String getPayeeRelSubCode() {
        return payeeRelSubCode;
    }

    public void setPayeeRelSubCode(String payeeRelSubCode) {
        this.payeeRelSubCode = payeeRelSubCode == null ? null : payeeRelSubCode.trim();
    }

    public String getPayerCode() {
        return payerCode;
    }

    public void setPayerCode(String payerCode) {
        this.payerCode = payerCode == null ? null : payerCode.trim();
    }

    public String getPayerAccCode() {
        return payerAccCode;
    }

    public void setPayerAccCode(String payerAccCode) {
        this.payerAccCode = payerAccCode == null ? null : payerAccCode.trim();
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType == null ? null : payerType.trim();
    }

    public String getPayerRelSubCode() {
        return payerRelSubCode;
    }

    public void setPayerRelSubCode(String payerRelSubCode) {
        this.payerRelSubCode = payerRelSubCode == null ? null : payerRelSubCode.trim();
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode == null ? null : ccyCode.trim();
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
        this.isFee = isFee == null ? null : isFee.trim();
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
        this.feeType = feeType == null ? null : feeType.trim();
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
        this.feeFlag = feeFlag == null ? null : feeFlag.trim();
    }

    public String getFeeStlMode() {
        return feeStlMode;
    }

    public void setFeeStlMode(String feeStlMode) {
        this.feeStlMode = feeStlMode == null ? null : feeStlMode.trim();
    }

    public String getThirdAccCode() {
        return thirdAccCode;
    }

    public void setThirdAccCode(String thirdAccCode) {
        this.thirdAccCode = thirdAccCode == null ? null : thirdAccCode.trim();
    }

    public String getRefundFeeBk() {
        return refundFeeBk;
    }

    public void setRefundFeeBk(String refundFeeBk) {
        this.refundFeeBk = refundFeeBk == null ? null : refundFeeBk.trim();
    }

    public String getBankAccCode() {
        return bankAccCode;
    }

    public void setBankAccCode(String bankAccCode) {
        this.bankAccCode = bankAccCode == null ? null : bankAccCode.trim();
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
        this.bankFeeType = bankFeeType == null ? null : bankFeeType.trim();
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
        this.isDeposit = isDeposit == null ? null : isDeposit.trim();
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
        this.depositStatus = depositStatus == null ? null : depositStatus.trim();
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
        this.accMode = accMode == null ? null : accMode.trim();
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus == null ? null : accStatus.trim();
    }

    public String getFrozenFlag() {
        return frozenFlag;
    }

    public void setFrozenFlag(String frozenFlag) {
        this.frozenFlag = frozenFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBlnMode() {
        return blnMode;
    }

    public void setBlnMode(String blnMode) {
        this.blnMode = blnMode == null ? null : blnMode.trim();
    }

    public String getBlnStatus() {
        return blnStatus;
    }

    public void setBlnStatus(String blnStatus) {
        this.blnStatus = blnStatus == null ? null : blnStatus.trim();
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
        this.errMsg = errMsg == null ? null : errMsg.trim();
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo == null ? null : voucherNo.trim();
    }

    public String getOriAppTranNo() {
        return oriAppTranNo;
    }

    public void setOriAppTranNo(String oriAppTranNo) {
        this.oriAppTranNo = oriAppTranNo == null ? null : oriAppTranNo.trim();
    }

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark == null ? null : tranRemark.trim();
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
}