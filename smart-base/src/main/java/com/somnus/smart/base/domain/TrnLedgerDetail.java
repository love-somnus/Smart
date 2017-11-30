package com.somnus.smart.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class TrnLedgerDetail extends Entity {
	
	private static final long serialVersionUID = 1L;
	
    private String ledTranNo;

    private String accEntryCode;

    private String accTranNo;

    private String accEntryType;

    private String accCode1st;

    private String subAccCode1st;

    private String firSubCode1st;

    private String relSubCode1st;

    private Short subIndex1st;

    private Long seqNo1st;

    private String subType1st;

    private String cdFlag1st;

    private BigDecimal tranAmt1st;

    private String accCode2nd;

    private String subAccCode2nd;

    private String subCode2nd;

    private String relSubCode2nd;

    private Short subIndex2nd;

    private Long seqNo2nd;

    private String subType2nd;

    private String cdFlag2nd;

    private BigDecimal tranAmt2nd;

    private String accCode3rd;

    private String subAccCode3rd;

    private String subCode3rd;

    private String relSubCode3rd;

    private Short subIndex3rd;

    private Long seqNo3rd;

    private String subType3rd;

    private String cdFlag3rd;

    private BigDecimal tranAmt3rd;

    private String accCode4th;

    private String subAccCode4th;

    private String subCode4th;

    private String relSubCode4th;

    private Short subIndex4th;

    private Long seqNo4th;

    private String subType4th;

    private String cdFlag4th;

    private BigDecimal tranAmt4th;

    private String ccyCode;

    private Date appTranDate;

    private Date submitAccDate;

    private String accStatus;

    private Date accDate;

    private String ipsBillNo;

    private String appTranNo;

    private String tranType;

    private String sysCode;

    private String cusTranNo;

    private String tranRemark;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getLedTranNo() {
        return ledTranNo;
    }

    public void setLedTranNo(String ledTranNo) {
        this.ledTranNo = ledTranNo == null ? null : ledTranNo.trim();
    }

    public String getAccEntryCode() {
        return accEntryCode;
    }

    public void setAccEntryCode(String accEntryCode) {
        this.accEntryCode = accEntryCode == null ? null : accEntryCode.trim();
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
    }

    public String getAccEntryType() {
        return accEntryType;
    }

    public void setAccEntryType(String accEntryType) {
        this.accEntryType = accEntryType == null ? null : accEntryType.trim();
    }

    public String getAccCode1st() {
        return accCode1st;
    }

    public void setAccCode1st(String accCode1st) {
        this.accCode1st = accCode1st == null ? null : accCode1st.trim();
    }

    public String getSubAccCode1st() {
        return subAccCode1st;
    }

    public void setSubAccCode1st(String subAccCode1st) {
        this.subAccCode1st = subAccCode1st == null ? null : subAccCode1st.trim();
    }

    public String getFirSubCode1st() {
        return firSubCode1st;
    }

    public void setFirSubCode1st(String firSubCode1st) {
        this.firSubCode1st = firSubCode1st == null ? null : firSubCode1st.trim();
    }

    public String getRelSubCode1st() {
        return relSubCode1st;
    }

    public void setRelSubCode1st(String relSubCode1st) {
        this.relSubCode1st = relSubCode1st == null ? null : relSubCode1st.trim();
    }

    public Short getSubIndex1st() {
        return subIndex1st;
    }

    public void setSubIndex1st(Short subIndex1st) {
        this.subIndex1st = subIndex1st;
    }

    public Long getSeqNo1st() {
        return seqNo1st;
    }

    public void setSeqNo1st(Long seqNo1st) {
        this.seqNo1st = seqNo1st;
    }

    public String getSubType1st() {
        return subType1st;
    }

    public void setSubType1st(String subType1st) {
        this.subType1st = subType1st == null ? null : subType1st.trim();
    }

    public String getCdFlag1st() {
        return cdFlag1st;
    }

    public void setCdFlag1st(String cdFlag1st) {
        this.cdFlag1st = cdFlag1st == null ? null : cdFlag1st.trim();
    }

    public BigDecimal getTranAmt1st() {
        return tranAmt1st;
    }

    public void setTranAmt1st(BigDecimal tranAmt1st) {
        this.tranAmt1st = tranAmt1st;
    }

    public String getAccCode2nd() {
        return accCode2nd;
    }

    public void setAccCode2nd(String accCode2nd) {
        this.accCode2nd = accCode2nd == null ? null : accCode2nd.trim();
    }

    public String getSubAccCode2nd() {
        return subAccCode2nd;
    }

    public void setSubAccCode2nd(String subAccCode2nd) {
        this.subAccCode2nd = subAccCode2nd == null ? null : subAccCode2nd.trim();
    }

    public String getSubCode2nd() {
        return subCode2nd;
    }

    public void setSubCode2nd(String subCode2nd) {
        this.subCode2nd = subCode2nd == null ? null : subCode2nd.trim();
    }

    public String getRelSubCode2nd() {
        return relSubCode2nd;
    }

    public void setRelSubCode2nd(String relSubCode2nd) {
        this.relSubCode2nd = relSubCode2nd == null ? null : relSubCode2nd.trim();
    }

    public Short getSubIndex2nd() {
        return subIndex2nd;
    }

    public void setSubIndex2nd(Short subIndex2nd) {
        this.subIndex2nd = subIndex2nd;
    }

    public Long getSeqNo2nd() {
        return seqNo2nd;
    }

    public void setSeqNo2nd(Long seqNo2nd) {
        this.seqNo2nd = seqNo2nd;
    }

    public String getSubType2nd() {
        return subType2nd;
    }

    public void setSubType2nd(String subType2nd) {
        this.subType2nd = subType2nd == null ? null : subType2nd.trim();
    }

    public String getCdFlag2nd() {
        return cdFlag2nd;
    }

    public void setCdFlag2nd(String cdFlag2nd) {
        this.cdFlag2nd = cdFlag2nd == null ? null : cdFlag2nd.trim();
    }

    public BigDecimal getTranAmt2nd() {
        return tranAmt2nd;
    }

    public void setTranAmt2nd(BigDecimal tranAmt2nd) {
        this.tranAmt2nd = tranAmt2nd;
    }

    public String getAccCode3rd() {
        return accCode3rd;
    }

    public void setAccCode3rd(String accCode3rd) {
        this.accCode3rd = accCode3rd == null ? null : accCode3rd.trim();
    }

    public String getSubAccCode3rd() {
        return subAccCode3rd;
    }

    public void setSubAccCode3rd(String subAccCode3rd) {
        this.subAccCode3rd = subAccCode3rd == null ? null : subAccCode3rd.trim();
    }

    public String getSubCode3rd() {
        return subCode3rd;
    }

    public void setSubCode3rd(String subCode3rd) {
        this.subCode3rd = subCode3rd == null ? null : subCode3rd.trim();
    }

    public String getRelSubCode3rd() {
        return relSubCode3rd;
    }

    public void setRelSubCode3rd(String relSubCode3rd) {
        this.relSubCode3rd = relSubCode3rd == null ? null : relSubCode3rd.trim();
    }

    public Short getSubIndex3rd() {
        return subIndex3rd;
    }

    public void setSubIndex3rd(Short subIndex3rd) {
        this.subIndex3rd = subIndex3rd;
    }

    public Long getSeqNo3rd() {
        return seqNo3rd;
    }

    public void setSeqNo3rd(Long seqNo3rd) {
        this.seqNo3rd = seqNo3rd;
    }

    public String getSubType3rd() {
        return subType3rd;
    }

    public void setSubType3rd(String subType3rd) {
        this.subType3rd = subType3rd == null ? null : subType3rd.trim();
    }

    public String getCdFlag3rd() {
        return cdFlag3rd;
    }

    public void setCdFlag3rd(String cdFlag3rd) {
        this.cdFlag3rd = cdFlag3rd == null ? null : cdFlag3rd.trim();
    }

    public BigDecimal getTranAmt3rd() {
        return tranAmt3rd;
    }

    public void setTranAmt3rd(BigDecimal tranAmt3rd) {
        this.tranAmt3rd = tranAmt3rd;
    }

    public String getAccCode4th() {
        return accCode4th;
    }

    public void setAccCode4th(String accCode4th) {
        this.accCode4th = accCode4th == null ? null : accCode4th.trim();
    }

    public String getSubAccCode4th() {
        return subAccCode4th;
    }

    public void setSubAccCode4th(String subAccCode4th) {
        this.subAccCode4th = subAccCode4th == null ? null : subAccCode4th.trim();
    }

    public String getSubCode4th() {
        return subCode4th;
    }

    public void setSubCode4th(String subCode4th) {
        this.subCode4th = subCode4th == null ? null : subCode4th.trim();
    }

    public String getRelSubCode4th() {
        return relSubCode4th;
    }

    public void setRelSubCode4th(String relSubCode4th) {
        this.relSubCode4th = relSubCode4th == null ? null : relSubCode4th.trim();
    }

    public Short getSubIndex4th() {
        return subIndex4th;
    }

    public void setSubIndex4th(Short subIndex4th) {
        this.subIndex4th = subIndex4th;
    }

    public Long getSeqNo4th() {
        return seqNo4th;
    }

    public void setSeqNo4th(Long seqNo4th) {
        this.seqNo4th = seqNo4th;
    }

    public String getSubType4th() {
        return subType4th;
    }

    public void setSubType4th(String subType4th) {
        this.subType4th = subType4th == null ? null : subType4th.trim();
    }

    public String getCdFlag4th() {
        return cdFlag4th;
    }

    public void setCdFlag4th(String cdFlag4th) {
        this.cdFlag4th = cdFlag4th == null ? null : cdFlag4th.trim();
    }

    public BigDecimal getTranAmt4th() {
        return tranAmt4th;
    }

    public void setTranAmt4th(BigDecimal tranAmt4th) {
        this.tranAmt4th = tranAmt4th;
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode == null ? null : ccyCode.trim();
    }

    public Date getAppTranDate() {
        return appTranDate;
    }

    public void setAppTranDate(Date appTranDate) {
        this.appTranDate = appTranDate;
    }

    public Date getSubmitAccDate() {
        return submitAccDate;
    }

    public void setSubmitAccDate(Date submitAccDate) {
        this.submitAccDate = submitAccDate;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus == null ? null : accStatus.trim();
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
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

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType == null ? null : tranType.trim();
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public String getCusTranNo() {
        return cusTranNo;
    }

    public void setCusTranNo(String cusTranNo) {
        this.cusTranNo = cusTranNo == null ? null : cusTranNo.trim();
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
}