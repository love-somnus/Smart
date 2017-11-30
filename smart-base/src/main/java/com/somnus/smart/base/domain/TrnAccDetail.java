package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnAccDetail extends Entity {

	private static final long serialVersionUID = 1L;
	
    private String accDetailNo;

    private String accTranNo;

    private String ledTranNo;

    private Short subIndex;

    private String subCode;

    private String relSubCode;

    private String cdFlag;

    private String accCode;

    private String subAccCode;

    private BigDecimal tranAmt;

    private BigDecimal curBal;

    private Date appTranDate;

    private Date accDate;

    private String sysCode;

    private String tranType;

    private String cusTranNo;

    private String ccyCode;

    private String ipsBillNo;

    private String appTranNo;

    private String tranRemark;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;
    /**总收入*/
	private BigDecimal totalIncome;
	/**总支出*/
	private BigDecimal totalExpenditure;

    public String getAccDetailNo() {
        return accDetailNo;
    }

    public void setAccDetailNo(String accDetailNo) {
        this.accDetailNo = accDetailNo == null ? null : accDetailNo.trim();
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
    }

    public String getLedTranNo() {
        return ledTranNo;
    }

    public void setLedTranNo(String ledTranNo) {
        this.ledTranNo = ledTranNo == null ? null : ledTranNo.trim();
    }

    public Short getSubIndex() {
        return subIndex;
    }

    public void setSubIndex(Short subIndex) {
        this.subIndex = subIndex;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode == null ? null : subCode.trim();
    }

    public String getRelSubCode() {
        return relSubCode;
    }

    public void setRelSubCode(String relSubCode) {
        this.relSubCode = relSubCode == null ? null : relSubCode.trim();
    }

    public String getCdFlag() {
        return cdFlag;
    }

    public void setCdFlag(String cdFlag) {
        this.cdFlag = cdFlag == null ? null : cdFlag.trim();
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode == null ? null : accCode.trim();
    }

    public String getSubAccCode() {
        return subAccCode;
    }

    public void setSubAccCode(String subAccCode) {
        this.subAccCode = subAccCode == null ? null : subAccCode.trim();
    }

    public BigDecimal getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(BigDecimal tranAmt) {
        this.tranAmt = tranAmt;
    }

    public BigDecimal getCurBal() {
        return curBal;
    }

    public void setCurBal(BigDecimal curBal) {
        this.curBal = curBal;
    }

    public Date getAppTranDate() {
        return appTranDate;
    }

    public void setAppTranDate(Date appTranDate) {
        this.appTranDate = appTranDate;
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType == null ? null : tranType.trim();
    }

    public String getCusTranNo() {
        return cusTranNo;
    }

    public void setCusTranNo(String cusTranNo) {
        this.cusTranNo = cusTranNo == null ? null : cusTranNo.trim();
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode == null ? null : ccyCode.trim();
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

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark == null ? null : tranRemark.trim();
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

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(BigDecimal totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}
}