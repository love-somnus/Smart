package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class MerMerchant extends Entity  {
	
	private static final long serialVersionUID = 1L;
	
	/** 商户ID*/
    private BigDecimal merId;
    
    /** 商户号_商户唯一标识*/
    private String merCode;
    
    /** POS商户号*/
    private String merCodePos;
    
    /** 商户类型_1：企业 2：个人 3：个体工商户*/
    private String merType;
    
    /** 商户名称*/
    private String merName;
    
    /** 商户简称*/
    private String shortName;
    
    /** 网关名称*/
    private String gwName;
    
    /** 办公地址*/
    private String officeAddr;
    
    /** 商户官方网站*/
    private String officialWebsite;
    
    /** 商户邮箱*/
    private String merMail;
    
    /** 一级行业分类*/
    private String industryL1;
    
    /** 二级行业分类*/
    private String industryL2;
    
    /** 三级行业分类*/
    private String industryL3;
    
    /** MCC码*/
    private String mcc;
    
    /** 商户等级*/
    private BigDecimal merLevel;
    
    /** 商户等级设置理由*/
    private String merLevelReason;
    
    /** 风险等级*/
    private String risk;
    
    /** 风险级别设置理由*/
    private String riskReason;
    
    /** 是否按月开发票*/
    private Short needInvoice;
    
    /** 商户来源方式*/
    private String merSource;
    
    /** 推荐详情*/
    private String recommendDetail;
    
    /** 所属分公司编号*/
    private String companyCode;
    
    /** 签约代表工号*/
    private String employeeNo;
    
    /** 签约代表手机号*/
    private String employeePhone;
    
    /** 代理商编号*/
    private String agencyCode;
    
    /** 所属国家*/
    private String merCountry;
    
    /** 所属省份*/
    private String merProvince;
    
    /** 所属城市*/
    private String merCity;
    
    /** 法人姓名*/
    private String legalPerson;
    
    /** 法人证件类型*/
    private String lpCerType;
    
    /** 法人证件号码*/
    private String lpCerNo;
    
    /** 法人证件有效日期*/
    private String lpCerVldTo;
    
    /** 营业执照号码*/
    private String licenseNo;
    
    /** 营业执照号码有效日期*/
    private String licenseVldTo;
    
    /** 主营业务*/
    private String mainBiz;
    
    /** 注册地址*/
    private String regAddr;
    
    /** 注册资本*/
    private Long regCapital;
    
    /** 实收资本*/
    private Long realCapital;
    
    /** 税证号码*/
    private String taxNo;
    
    /** 税证有效日期*/
    private String taxVldTo;
    
    /** 组织机构代码*/
    private String orgCode;
    
    /** 组织机构代码证有效日期*/
    private String orgVldTo;
    
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

    public BigDecimal getMerId() {
        return merId;
    }

    public void setMerId(BigDecimal merId) {
        this.merId = merId;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode == null ? null : merCode.trim();
    }

    public String getMerCodePos() {
        return merCodePos;
    }

    public void setMerCodePos(String merCodePos) {
        this.merCodePos = merCodePos == null ? null : merCodePos.trim();
    }

    public String getMerType() {
        return merType;
    }

    public void setMerType(String merType) {
        this.merType = merType == null ? null : merType.trim();
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName == null ? null : merName.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getGwName() {
        return gwName;
    }

    public void setGwName(String gwName) {
        this.gwName = gwName == null ? null : gwName.trim();
    }

    public String getOfficeAddr() {
        return officeAddr;
    }

    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr == null ? null : officeAddr.trim();
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite == null ? null : officialWebsite.trim();
    }

    public String getMerMail() {
        return merMail;
    }

    public void setMerMail(String merMail) {
        this.merMail = merMail == null ? null : merMail.trim();
    }

    public String getIndustryL1() {
        return industryL1;
    }

    public void setIndustryL1(String industryL1) {
        this.industryL1 = industryL1 == null ? null : industryL1.trim();
    }

    public String getIndustryL2() {
        return industryL2;
    }

    public void setIndustryL2(String industryL2) {
        this.industryL2 = industryL2 == null ? null : industryL2.trim();
    }

    public String getIndustryL3() {
        return industryL3;
    }

    public void setIndustryL3(String industryL3) {
        this.industryL3 = industryL3 == null ? null : industryL3.trim();
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc == null ? null : mcc.trim();
    }

    public BigDecimal getMerLevel() {
        return merLevel;
    }

    public void setMerLevel(BigDecimal merLevel) {
        this.merLevel = merLevel;
    }

    public String getMerLevelReason() {
        return merLevelReason;
    }

    public void setMerLevelReason(String merLevelReason) {
        this.merLevelReason = merLevelReason == null ? null : merLevelReason.trim();
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk == null ? null : risk.trim();
    }

    public String getRiskReason() {
        return riskReason;
    }

    public void setRiskReason(String riskReason) {
        this.riskReason = riskReason == null ? null : riskReason.trim();
    }

    public Short getNeedInvoice() {
        return needInvoice;
    }

    public void setNeedInvoice(Short needInvoice) {
        this.needInvoice = needInvoice;
    }

    public String getMerSource() {
        return merSource;
    }

    public void setMerSource(String merSource) {
        this.merSource = merSource == null ? null : merSource.trim();
    }

    public String getRecommendDetail() {
        return recommendDetail;
    }

    public void setRecommendDetail(String recommendDetail) {
        this.recommendDetail = recommendDetail == null ? null : recommendDetail.trim();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo == null ? null : employeeNo.trim();
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone == null ? null : employeePhone.trim();
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode == null ? null : agencyCode.trim();
    }

    public String getMerCountry() {
        return merCountry;
    }

    public void setMerCountry(String merCountry) {
        this.merCountry = merCountry == null ? null : merCountry.trim();
    }

    public String getMerProvince() {
        return merProvince;
    }

    public void setMerProvince(String merProvince) {
        this.merProvince = merProvince == null ? null : merProvince.trim();
    }

    public String getMerCity() {
        return merCity;
    }

    public void setMerCity(String merCity) {
        this.merCity = merCity == null ? null : merCity.trim();
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getLpCerType() {
        return lpCerType;
    }

    public void setLpCerType(String lpCerType) {
        this.lpCerType = lpCerType == null ? null : lpCerType.trim();
    }

    public String getLpCerNo() {
        return lpCerNo;
    }

    public void setLpCerNo(String lpCerNo) {
        this.lpCerNo = lpCerNo == null ? null : lpCerNo.trim();
    }

    public String getLpCerVldTo() {
        return lpCerVldTo;
    }

    public void setLpCerVldTo(String lpCerVldTo) {
        this.lpCerVldTo = lpCerVldTo == null ? null : lpCerVldTo.trim();
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo == null ? null : licenseNo.trim();
    }

    public String getLicenseVldTo() {
        return licenseVldTo;
    }

    public void setLicenseVldTo(String licenseVldTo) {
        this.licenseVldTo = licenseVldTo == null ? null : licenseVldTo.trim();
    }

    public String getMainBiz() {
        return mainBiz;
    }

    public void setMainBiz(String mainBiz) {
        this.mainBiz = mainBiz == null ? null : mainBiz.trim();
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr == null ? null : regAddr.trim();
    }

    public Long getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(Long regCapital) {
        this.regCapital = regCapital;
    }

    public Long getRealCapital() {
        return realCapital;
    }

    public void setRealCapital(Long realCapital) {
        this.realCapital = realCapital;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo == null ? null : taxNo.trim();
    }

    public String getTaxVldTo() {
        return taxVldTo;
    }

    public void setTaxVldTo(String taxVldTo) {
        this.taxVldTo = taxVldTo == null ? null : taxVldTo.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getOrgVldTo() {
        return orgVldTo;
    }

    public void setOrgVldTo(String orgVldTo) {
        this.orgVldTo = orgVldTo == null ? null : orgVldTo.trim();
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