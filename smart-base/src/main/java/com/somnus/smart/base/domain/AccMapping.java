package com.somnus.smart.base.domain;

public class AccMapping extends Entity {

	private static final long serialVersionUID = 1L;
	
	/**专用账户号 */
	private String speAcctCode;
	
	/**标准账户号 */
	private String stdAcctCode;
	
	/**创建时间 */
	private String createTime;
	
	/**创建人 */
	private String createBy;
	
	/**更新时间 */
	private String modifyTime;
	
	/**更新人 */
	private String modifyBy;
	public String getSpeAcctCode() {
		return speAcctCode;
	}
	public void setSpeAcctCode(String speAcctCode) {
		this.speAcctCode = speAcctCode;
	}
	public String getStdAcctCode() {
		return stdAcctCode;
	}
	public void setStdAcctCode(String stdAcctCode) {
		this.stdAcctCode = stdAcctCode;
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
