package com.somnus.smart.base.domain;

public class CfgSubdict extends Entity {

	private static final long serialVersionUID = 1L;
	
    private String subCode;

    private String subName;

    private String subCode1st;

    private String subName1st;

    private String status;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubCode1st() {
        return subCode1st;
    }

    public void setSubCode1st(String subCode1st) {
        this.subCode1st = subCode1st;
    }

    public String getSubName1st() {
        return subName1st;
    }

    public void setSubName1st(String subName1st) {
        this.subName1st = subName1st;
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