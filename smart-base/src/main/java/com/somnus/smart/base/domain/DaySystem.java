package com.somnus.smart.base.domain;

public class DaySystem extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String dayId;

    private String cutCode;

    private String coreDate;

    private String lCoreDate;

    private String blCoreDate;

    private String nCoreDate;

    private String yearEndDate;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getCutCode() {
        return cutCode;
    }

    public void setCutCode(String cutCode) {
        this.cutCode = cutCode;
    }

    public String getCoreDate() {
        return coreDate;
    }

    public void setCoreDate(String coreDate) {
        this.coreDate = coreDate;
    }

    public String getlCoreDate() {
        return lCoreDate;
    }

    public void setlCoreDate(String lCoreDate) {
        this.lCoreDate = lCoreDate;
    }

    public String getBlCoreDate() {
        return blCoreDate;
    }

    public void setBlCoreDate(String blCoreDate) {
        this.blCoreDate = blCoreDate;
    }

    public String getnCoreDate() {
        return nCoreDate;
    }

    public void setnCoreDate(String nCoreDate) {
        this.nCoreDate = nCoreDate;
    }

    public String getYearEndDate() {
        return yearEndDate;
    }

    public void setYearEndDate(String yearEndDate) {
        this.yearEndDate = yearEndDate;
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