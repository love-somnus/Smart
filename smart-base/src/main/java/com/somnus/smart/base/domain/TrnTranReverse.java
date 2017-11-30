package com.somnus.smart.base.domain;

public class TrnTranReverse extends Entity {
	
	private static final long serialVersionUID = 1L;

	/** 冲正附属流水号 */
    private String            reverseTranNo;
    
    /** 记账流水号 */
    private String            accTranNo;
    
    /** 状态 0：受理中 1 已冲正 */
    private String            status;
    
    /** 创建时间 */
    private String            createTime;
    
    /** 创建人 */
    private String            createBy;
    
    /** 更新时间 */
    private String            modifyTime;
    
    /** 更新人 */
    private String            modifyBy;

    public String getReverseTranNo() {
        return reverseTranNo;
    }

    public void setReverseTranNo(String reverseTranNo) {
        this.reverseTranNo = reverseTranNo;
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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