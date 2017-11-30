package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class MerAccount extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	/** 账户ID*/
    private Long acctId;
    
    /** 交易账户号*/
    private String acctCode;
    
    /** 商户号*/
    private String merCode;
    
    /** 交易账户名*/
    private String acctName;
    
    /** 币种_156：人民币*/
    private String currency;
    
    /** 开户银行*/
    private String bankCode;
    
    /** 开户银行名称*/
    private String bankName;
    
    /** 银行联行号*/
    private String bankKey;
    
    /** 分行编号_开户行编号*/
    private String branchCode;
    
    /** 分行名称_开户行名称*/
    private String branchName;
    
    /** 商户银行账号*/
    private String bankAcctNo;
    
    /** 商户银行户名*/
    private String bankAcctName;
    
    /** 清算类型_1:对公 2：对私*/
    private String stlType;
    
    /** 余额下限*/
    private BigDecimal balFloor;
    
    /** 是否开通协议划款_0：否 1：是*/
    private Short autoTransfer;
    
    /** 最低划款金额*/
    private BigDecimal minTransfer;
    
    /** 划款周期_1:工作日划款 2:一周两次 3:一周一次*/
    private String transferPeriod;
    
    /** 是否自动出款_0：否 1：是*/
    private String isAutoStl;
    
    /** 开始划款日期*/
    private String beginTransfer;
    
    /** 出款优先级_1,2,3*/
    private Integer transferPriority;
    
    /** 是否允许收款_0：否 1：是*/
    private String allowIn;
    
    /** 是否允许出款_0：否 1：是*/
    private String allowOut;
    
    /** 出款标志_区分是风控的冻结还是公安的冻结 二进制“01”表示风控允许出款；二进制“10”表示公安允许出款；“11”表示都允许出款；“00”表示都不允许出款*/
    private String outFlag;
    
    /** 备注*/
    private String remark;
    
    /** 版本*/
    private Integer version;
    
    /** 版本状态_1:开启/开通/有效 2:关闭/无效*/
    private String status;
    
    /** 操作标志_0:最终有效 1:被删除 2:被新版本替换*/
    private String operFlag;
    
    /** 生效日期*/
    private String effectDate;
    
    /** 失效日期*/
    private String loseEffectDate;
    
    /** 修改人*/
    private String modifyBy;
    
    /** 修改时间*/
    private Date modifyTime;

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public String getAcctCode() {
        return acctCode;
    }

    public void setAcctCode(String acctCode) {
        this.acctCode = acctCode == null ? null : acctCode.trim();
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode == null ? null : merCode.trim();
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName == null ? null : acctName.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankKey() {
        return bankKey;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey == null ? null : bankKey.trim();
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode == null ? null : branchCode.trim();
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName == null ? null : branchName.trim();
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo == null ? null : bankAcctNo.trim();
    }

    public String getBankAcctName() {
        return bankAcctName;
    }

    public void setBankAcctName(String bankAcctName) {
        this.bankAcctName = bankAcctName == null ? null : bankAcctName.trim();
    }

    public String getStlType() {
        return stlType;
    }

    public void setStlType(String stlType) {
        this.stlType = stlType == null ? null : stlType.trim();
    }

    public BigDecimal getBalFloor() {
        return balFloor;
    }

    public void setBalFloor(BigDecimal balFloor) {
        this.balFloor = balFloor;
    }

    public Short getAutoTransfer() {
        return autoTransfer;
    }

    public void setAutoTransfer(Short autoTransfer) {
        this.autoTransfer = autoTransfer;
    }

    public BigDecimal getMinTransfer() {
        return minTransfer;
    }

    public void setMinTransfer(BigDecimal minTransfer) {
        this.minTransfer = minTransfer;
    }

    public String getTransferPeriod() {
        return transferPeriod;
    }

    public void setTransferPeriod(String transferPeriod) {
        this.transferPeriod = transferPeriod == null ? null : transferPeriod.trim();
    }

    public String getIsAutoStl() {
        return isAutoStl;
    }

    public void setIsAutoStl(String isAutoStl) {
        this.isAutoStl = isAutoStl == null ? null : isAutoStl.trim();
    }

    public String getBeginTransfer() {
        return beginTransfer;
    }

    public void setBeginTransfer(String beginTransfer) {
        this.beginTransfer = beginTransfer == null ? null : beginTransfer.trim();
    }

    public Integer getTransferPriority() {
        return transferPriority;
    }

    public void setTransferPriority(Integer transferPriority) {
        this.transferPriority = transferPriority;
    }

    public String getAllowIn() {
        return allowIn;
    }

    public void setAllowIn(String allowIn) {
        this.allowIn = allowIn == null ? null : allowIn.trim();
    }

    public String getAllowOut() {
        return allowOut;
    }

    public void setAllowOut(String allowOut) {
        this.allowOut = allowOut == null ? null : allowOut.trim();
    }

    public String getOutFlag() {
        return outFlag;
    }

    public void setOutFlag(String outFlag) {
        this.outFlag = outFlag == null ? null : outFlag.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOperFlag() {
        return operFlag;
    }

    public void setOperFlag(String operFlag) {
        this.operFlag = operFlag == null ? null : operFlag.trim();
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate == null ? null : effectDate.trim();
    }

    public String getLoseEffectDate() {
        return loseEffectDate;
    }

    public void setLoseEffectDate(String loseEffectDate) {
        this.loseEffectDate = loseEffectDate == null ? null : loseEffectDate.trim();
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}