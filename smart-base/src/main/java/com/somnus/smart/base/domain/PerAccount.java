package com.somnus.smart.base.domain;

public class PerAccount extends Entity {

	private static final long serialVersionUID = 1L;
	
	/** 个人交易账户号 */
	private String acctCode;
	
	/** 个人交易账户名称 */
	private String acctName;
	
	/** 登录用户名 */
	private String userId;
	
	/** 绑定邮箱 */
	private String email;
	
	/** 绑定手机 */
	private String mobile;
	
	/** 开户银行 */
	private String bankCode;
	
	/** 开户银行名称 */
	private String bankName;
	
	/** 银行账号 */
	private String bankAcctNo;
	
	/** 银行账号名称 */
	private String bankAcctName;
	
	/** 货币码 */
	private String ccyCode;
	
	/** 会员等级 */
	private String level;
	
	/** 账户状态 */
	private String accStatus;
	
	/** 资金状态 */
	private String fundStatus;
	
	/** 开户日期 */
	private String openDate;
	
	/** 开户人 */
	private String openBy;
	
	/** 创建时间 */
	private String createTime;
	
	/** 创建人 */
	private String createBy;
	
	/** 更新时间 */
	private String modifyTime;
	
	/** 更新人 */
	private String modifyBy;
	public String getAcctCode() {
		return acctCode;
	}
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAcctNo() {
		return bankAcctNo;
	}
	public void setBankAcctNo(String bankAcctNo) {
		this.bankAcctNo = bankAcctNo;
	}
	public String getBankAcctName() {
		return bankAcctName;
	}
	public void setBankAcctName(String bankAcctName) {
		this.bankAcctName = bankAcctName;
	}
	public String getCcyCode() {
		return ccyCode;
	}
	public void setCcyCode(String ccyCode) {
		this.ccyCode = ccyCode;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
	}
	public String getFundStatus() {
		return fundStatus;
	}
	public void setFundStatus(String fundStatus) {
		this.fundStatus = fundStatus;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getOpenBy() {
		return openBy;
	}
	public void setOpenBy(String openBy) {
		this.openBy = openBy;
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