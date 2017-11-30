package com.somnus.smart.base.domain;

import java.util.Date;

public class CusAccountedStatus extends Entity {

	private static final long serialVersionUID = 1L;

	private String merAccCode;

    private String inAccStatus;

    private String drawLever;

    private Date drawTime;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getMerAccCode() {
        return merAccCode;
    }

    public void setMerAccCode(String merAccCode) {
        this.merAccCode = merAccCode == null ? null : merAccCode.trim();
    }

    public String getInAccStatus() {
        return inAccStatus;
    }

    public void setInAccStatus(String inAccStatus) {
        this.inAccStatus = inAccStatus == null ? null : inAccStatus.trim();
    }

    public String getDrawLever() {
        return drawLever;
    }

    public void setDrawLever(String drawLever) {
        this.drawLever = drawLever == null ? null : drawLever.trim();
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
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