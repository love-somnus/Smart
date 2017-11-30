package com.somnus.smart.domain.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.somnus.smart.base.dao.TrnAccDetailDao;
import com.somnus.smart.base.domain.TrnAccDetail;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;
import com.somnus.smart.support.util.DateUtil;

/** 分账户明细  */
public class AccDetail extends TrnAccDetail implements DomainModel<AccDetail, TrnAccDetail> {

	private static final long serialVersionUID = 1L;

	private static TrnAccDetailDao dao;

    private final static String    BIZ_SUB_ACC_CODE__PREFIX = "1";

    private final static String    INT_SUB_ACC_CODE__PREFIX = "2";

    private final static String    IDV_SUB_ACC_CODE__PREFIX = "3";

    private AccDetail() { }

    public static AccDetail getInstance() {
        return (AccDetail) DomainHelper.getDomainInstance(AccDetail.class);
    }

    public static void init(ApplicationContext ctx) {
        dao = ctx.getBean(TrnAccDetailDao.class);
    }

    /**
     * 根据TrnAccDetail返回AccDetail
     * 
     * @param model
     * @return
     */
    /*private static AccDetail getAccDetail(TrnAccDetail model) {
        AccDetail accDetail = getInstance();
        if (model != null) {
            DomainHelper.setDomainData(accDetail, model);
        }
        return accDetail;
    }*/

    /**
     * 落地
     * 
     * @return
     */
    public boolean save() {

        if (getAccDetailNo() == null) {
            //企业账户收支明细
            if (this.getSubAccCode().startsWith(BIZ_SUB_ACC_CODE__PREFIX)) {
                dao.insertBizAccDetail(this);
            }
            //內部账户收支明细
            else if (this.getSubAccCode().startsWith(INT_SUB_ACC_CODE__PREFIX)) {
                dao.insertIntAccDetail(this);
            }//內部账户收支明细
            else if (this.getSubAccCode().startsWith(IDV_SUB_ACC_CODE__PREFIX)) {
                dao.insertIdvAccDetail(this);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据台账创建分账户明细
     * 
     * @param ledgerdetail
     * @return
     */
    public List<AccDetail> createAccDetails(LedgerDetail ledgerDetail) {
        List<AccDetail> accDetailList = new ArrayList<AccDetail>();
        AccDetail firstAccDetail = getFirstAccDetail(ledgerDetail);
        AccDetail secondAccDetail = getSecondAccDetail(ledgerDetail);
        AccDetail thirdAccDetail = getThirdAccDetail(ledgerDetail);
        AccDetail fourthAccDetail = getFourthAccDetail(ledgerDetail);
        if (firstAccDetail != null) {
            accDetailList.add(firstAccDetail);
        }
        if (secondAccDetail != null) {
            accDetailList.add(secondAccDetail);
        }
        if (thirdAccDetail != null) {
            accDetailList.add(thirdAccDetail);
        }
        if (fourthAccDetail != null) {
            accDetailList.add(fourthAccDetail);
        }
        return accDetailList;
    }

    /**
     * 验证是否已经记账
     * 
     * @param accDetail
     * @return
     */
    public boolean checkExistAccDetail(TrnAccDetail accDetail) {
        List<TrnAccDetail> trnAccDetails = null;
        //企业账户收支明细
        if (accDetail.getSubAccCode().startsWith(BIZ_SUB_ACC_CODE__PREFIX)) {
            trnAccDetails = dao.getBizAccDetailToDo(accDetail);
        }
        //內部账户收支明细
        else if (accDetail.getSubAccCode().startsWith(INT_SUB_ACC_CODE__PREFIX)) {
            trnAccDetails = dao.getIntAccDetailToDo(accDetail);
        }//內部账户收支明细
        else if (accDetail.getSubAccCode().startsWith(IDV_SUB_ACC_CODE__PREFIX)) {
            trnAccDetails = dao.getIdvAccDetailToDo(accDetail);
        }
        if (trnAccDetails != null && trnAccDetails.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 台帐第一个借贷赋值明细账
     * 
     * @param ledgerDetail
     */
    private AccDetail getFirstAccDetail(LedgerDetail ledgerDetail) {
        if (ledgerDetail.getFirSubCode1st() == null) {
            return null;
        }
        AccDetail firstAccDetail = AccDetail.getInstance();
        firstAccDetail.setSubIndex(ledgerDetail.getSubIndex1st());
        firstAccDetail.setSubCode(ledgerDetail.getFirSubCode1st());
        firstAccDetail.setRelSubCode(ledgerDetail.getRelSubCode1st());
        firstAccDetail.setCdFlag(ledgerDetail.getCdFlag1st());
        firstAccDetail.setAccCode(ledgerDetail.getAccCode1st());
        firstAccDetail.setSubAccCode(ledgerDetail.getSubAccCode1st());
        firstAccDetail.setTranAmt(ledgerDetail.getTranAmt1st());
        commonSetAccDetail(ledgerDetail, firstAccDetail);
        return firstAccDetail;
    }

    /**
     * 台帐第二个借贷赋值明细账
     * 
     * @param ledgerDetail
     */
    private AccDetail getSecondAccDetail(LedgerDetail ledgerDetail) {
        if (ledgerDetail.getSubCode2nd() == null) {
            return null;
        }
        AccDetail accDetail = new AccDetail();
        accDetail.setSubIndex(ledgerDetail.getSubIndex2nd());
        accDetail.setSubCode(ledgerDetail.getSubCode2nd());
        accDetail.setRelSubCode(ledgerDetail.getRelSubCode2nd());
        accDetail.setCdFlag(ledgerDetail.getCdFlag2nd());
        accDetail.setAccCode(ledgerDetail.getAccCode2nd());
        accDetail.setSubAccCode(ledgerDetail.getSubAccCode2nd());
        accDetail.setTranAmt(ledgerDetail.getTranAmt2nd());
        commonSetAccDetail(ledgerDetail, accDetail);
        return accDetail;
    }

    /**
     * 台帐第三个借贷赋值明细账
     * 
     * @param ledgerDetail
     */
    private AccDetail getThirdAccDetail(LedgerDetail ledgerDetail) {
        if (ledgerDetail.getSubCode3rd() == null) {
            return null;
        }
        AccDetail accDetail = new AccDetail();
        accDetail.setSubIndex(ledgerDetail.getSubIndex3rd());
        accDetail.setSubCode(ledgerDetail.getSubCode3rd());
        accDetail.setRelSubCode(ledgerDetail.getRelSubCode3rd());
        accDetail.setCdFlag(ledgerDetail.getCdFlag3rd());
        accDetail.setAccCode(ledgerDetail.getAccCode3rd());
        accDetail.setSubAccCode(ledgerDetail.getSubAccCode3rd());
        accDetail.setTranAmt(ledgerDetail.getTranAmt3rd());
        commonSetAccDetail(ledgerDetail, accDetail);
        return accDetail;
    }

    /**
     * 台帐第四个借贷赋值明细账
     * 
     * @param ledgerDetail
     */
    private AccDetail getFourthAccDetail(LedgerDetail ledgerDetail) {
        if (ledgerDetail.getSubCode4th() == null) {
            return null;
        }
        AccDetail accDetail = new AccDetail();
        accDetail.setSubIndex(ledgerDetail.getSubIndex4th());
        accDetail.setSubCode(ledgerDetail.getSubCode4th());
        accDetail.setRelSubCode(ledgerDetail.getRelSubCode4th());
        accDetail.setCdFlag(ledgerDetail.getCdFlag4th());
        accDetail.setAccCode(ledgerDetail.getAccCode4th());
        accDetail.setSubAccCode(ledgerDetail.getSubAccCode4th());
        accDetail.setTranAmt(ledgerDetail.getTranAmt4th());
        commonSetAccDetail(ledgerDetail, accDetail);
        return accDetail;
    }

    /**
     * 台帐转分户账明细普通赋值
     * 
     * @param ledgerDetail
     * @param accDetail
     */
    private void commonSetAccDetail(LedgerDetail ledgerDetail, AccDetail accDetail) {
        accDetail.setAccTranNo(ledgerDetail.getAccTranNo());
        accDetail.setAppTranNo(ledgerDetail.getAppTranNo());
        accDetail.setLedTranNo(ledgerDetail.getLedTranNo());
        accDetail.setAppTranDate(ledgerDetail.getAppTranDate());
        accDetail.setAccDate(ledgerDetail.getAccDate());
        accDetail.setSysCode(ledgerDetail.getSysCode());
        accDetail.setTranType(ledgerDetail.getTranType());
        accDetail.setCusTranNo(ledgerDetail.getCusTranNo());
        accDetail.setCcyCode(ledgerDetail.getCcyCode());
        accDetail.setIpsBillNo(ledgerDetail.getIpsBillNo());
        accDetail.setAppTranNo(ledgerDetail.getAppTranNo());
        accDetail.setTranRemark(ledgerDetail.getTranRemark());
        accDetail.setCreateTime(DateUtil.getCurrentTimeStamp());
        accDetail.setCreateBy(ledgerDetail.getModifyBy());
        accDetail.setModifyTime(accDetail.getCreateTime());
        accDetail.setModifyBy(accDetail.getCreateBy());
    }
}
