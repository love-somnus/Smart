package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CusSubaccinfo extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	/** 功能账户号_标识位（1）+ 币种（3）+科目（2）+ 序号（10）*/
    private String subAccCode;
    
    /** 功能账户名*/
    private String subAccName;
    
    /** 账务日期*/
    private Date accDate;
    
    /** 交易账户*/
    private String merAccCode;
    
    /** 一级科目_所属一级科目*/
    private String subCode;
    
    /** 科目号_实际科目*/
    private String relSubCode;
    
    /** 余额方向_D-借方 C-贷方*/
    private String balDir;
    
    /** 币种代码_156 人民币*/
    private String ccyCode;
    
    /** 当前余额*/
    private BigDecimal curBal;
    
    /** 可用余额*/
    private BigDecimal availBal;
    
    /** 业务冻结金额_业务冻结 【退款冻结、风控冻结】*/
    private BigDecimal bizFreezeBal;
    
    /** 监管冻结金额_监管冻结*/
    private BigDecimal magFreezeBal;
    
    /** 应付冻结金额_应付冻结*/
    private BigDecimal payableFreezeBal;
    
    /** 期初余额*/
    private BigDecimal initialBal;
    
    /** 上日期初余额_上日期初*/
    private BigDecimal lastInitialBal;
    
    /** 借方发生额*/
    private BigDecimal debitAmt;
    
    /** 贷方发生额*/
    private BigDecimal creditAmt;
    
    /** 账户类型_记收 ，流动资金 。。。。。 参见 账户类型定义*/
    private String accType;
    
    /** 是否可透支_0：不可透支 1：可透支*/
    private String overFlag;
    
    /** 帐户状态_0：正常 1：销户 2:冻结*/
    private String accStatus;
    
    /** 开户日期*/
    private Date openDate;
    
    /** 开户人*/
    private String openBy;
    
    /** 销户日期*/
    private Date closeDate;
    
    /** 销户人*/
    private String closeBy;
    
    /** 创建时间_系统时间 yyyy-MM-dd HH24:MI:SS*/
    private String createTime;
    
    /** 创建人_系统： CORE  外围 报文中的 operator 字段*/
    private String createBy;
    
    /** 更新时间_系统时间 yyyy-MM-dd HH24:MI:SS*/
    private String modifyTime;
    
    /** 更新人_系统： CORE  外围 报文中的 operator 字段*/
    private String modifyBy;

    public String getSubAccCode() {
        return subAccCode;
    }

    public void setSubAccCode(String subAccCode) {
        this.subAccCode = subAccCode;
    }

    public String getSubAccName() {
        return subAccName;
    }

    public void setSubAccName(String subAccName) {
        this.subAccName = subAccName;
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public String getMerAccCode() {
        return merAccCode;
    }

    public void setMerAccCode(String merAccCode) {
        this.merAccCode = merAccCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getRelSubCode() {
        return relSubCode;
    }

    public void setRelSubCode(String relSubCode) {
        this.relSubCode = relSubCode;
    }

    public String getBalDir() {
        return balDir;
    }

    public void setBalDir(String balDir) {
        this.balDir = balDir;
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode;
    }

    public BigDecimal getCurBal() {
        return curBal;
    }

    public void setCurBal(BigDecimal curBal) {
        this.curBal = curBal;
    }

    public BigDecimal getAvailBal() {
        return availBal;
    }

    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }

    public BigDecimal getBizFreezeBal() {
        return bizFreezeBal;
    }

    public void setBizFreezeBal(BigDecimal bizFreezeBal) {
        this.bizFreezeBal = bizFreezeBal;
    }

    public BigDecimal getMagFreezeBal() {
        return magFreezeBal;
    }

    public void setMagFreezeBal(BigDecimal magFreezeBal) {
        this.magFreezeBal = magFreezeBal;
    }

    public BigDecimal getPayableFreezeBal() {
        return payableFreezeBal;
    }

    public void setPayableFreezeBal(BigDecimal payableFreezeBal) {
        this.payableFreezeBal = payableFreezeBal;
    }

    public BigDecimal getInitialBal() {
        return initialBal;
    }

    public void setInitialBal(BigDecimal initialBal) {
        this.initialBal = initialBal;
    }

    public BigDecimal getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(BigDecimal debitAmt) {
        this.debitAmt = debitAmt;
    }

    public BigDecimal getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(BigDecimal creditAmt) {
        this.creditAmt = creditAmt;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getOverFlag() {
        return overFlag;
    }

    public void setOverFlag(String overFlag) {
        this.overFlag = overFlag;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getOpenBy() {
        return openBy;
    }

    public void setOpenBy(String openBy) {
        this.openBy = openBy;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getCloseBy() {
        return closeBy;
    }

    public void setCloseBy(String closeBy) {
        this.closeBy = closeBy;
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

	public BigDecimal getLastInitialBal() {
		return lastInitialBal;
	}

	public void setLastInitialBal(BigDecimal lastInitialBal) {
		this.lastInitialBal = lastInitialBal;
	}
}