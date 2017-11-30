package com.somnus.smart.base.domain;

import java.math.BigDecimal;

public class TrnTranVoucher extends Entity {
	
	private static final long serialVersionUID = 1L;

	private String vouTranNo;

    private String accTranNo;

    private String subCode1st;

    private Short subIndex1st;

    private String cdFlag1st;

    private BigDecimal tranAmt1st;

    private String subCode2nd;

    private Short subIndex2nd;

    private String cdFlag2nd;

    private BigDecimal tranAmt2nd;

    private String subCode3rd;

    private Short subIndex3rd;

    private String cdFlag3rd;

    private BigDecimal tranAmt3rd;

    private String subCode4th;

    private Short subIndex4th;

    private String cdFlag4th;

    private BigDecimal tranAmt4th;

    private String createTime;

    private String createBy;

    private String modifyTime;

    private String modifyBy;

    public String getVouTranNo() {
        return vouTranNo;
    }

    public void setVouTranNo(String vouTranNo) {
        this.vouTranNo = vouTranNo == null ? null : vouTranNo.trim();
    }

    public String getAccTranNo() {
        return accTranNo;
    }

    public void setAccTranNo(String accTranNo) {
        this.accTranNo = accTranNo == null ? null : accTranNo.trim();
    }

    public String getSubCode1st() {
        return subCode1st;
    }

    public void setSubCode1st(String subCode1st) {
        this.subCode1st = subCode1st == null ? null : subCode1st.trim();
    }

    public Short getSubIndex1st() {
        return subIndex1st;
    }

    public void setSubIndex1st(Short subIndex1st) {
        this.subIndex1st = subIndex1st;
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

    public String getSubCode2nd() {
        return subCode2nd;
    }

    public void setSubCode2nd(String subCode2nd) {
        this.subCode2nd = subCode2nd == null ? null : subCode2nd.trim();
    }

    public Short getSubIndex2nd() {
        return subIndex2nd;
    }

    public void setSubIndex2nd(Short subIndex2nd) {
        this.subIndex2nd = subIndex2nd;
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

    public String getSubCode3rd() {
        return subCode3rd;
    }

    public void setSubCode3rd(String subCode3rd) {
        this.subCode3rd = subCode3rd == null ? null : subCode3rd.trim();
    }

    public Short getSubIndex3rd() {
        return subIndex3rd;
    }

    public void setSubIndex3rd(Short subIndex3rd) {
        this.subIndex3rd = subIndex3rd;
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

    public String getSubCode4th() {
        return subCode4th;
    }

    public void setSubCode4th(String subCode4th) {
        this.subCode4th = subCode4th == null ? null : subCode4th.trim();
    }

    public Short getSubIndex4th() {
        return subIndex4th;
    }

    public void setSubIndex4th(Short subIndex4th) {
        this.subIndex4th = subIndex4th;
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