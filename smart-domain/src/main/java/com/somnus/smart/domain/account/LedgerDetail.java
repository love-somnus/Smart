package com.somnus.smart.domain.account;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;

import com.somnus.smart.base.dao.AccMappingDao;
import com.somnus.smart.base.dao.CusAccountedStatusDao;
import com.somnus.smart.base.dao.TrnLedgerDetailDao;
import com.somnus.smart.base.domain.CusAccountedStatus;
import com.somnus.smart.base.domain.TrnLedgerDetail;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.domain.DomainModel;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;
import com.somnus.smart.support.util.ValidateUtil;

/** 台账 */
public class LedgerDetail extends TrnLedgerDetail implements DomainModel<LedgerDetail, TrnLedgerDetail> {
	private static final long serialVersionUID = 1L;
	
	/** TrnLedgerDetailDao */
	private static TrnLedgerDetailDao trnLedgerDetailDaoDao;
	
	/** CusAccountedStatusDao */
	private static CusAccountedStatusDao cusAccountedStatusDao;
	
	/** MessageSourceAccessor */
	private static MessageSourceAccessor messageSourceAccessor;
	
	/** AccMappingDao */
	private static AccMappingDao accMappingDao;
	
	/** Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(LedgerDetail.class);

	private LedgerDetail() { }

	public static LedgerDetail getInstance() {

		return (LedgerDetail) DomainHelper
				.getDomainInstance(LedgerDetail.class);
	}

	public static void init(ApplicationContext context) {
		trnLedgerDetailDaoDao = context.getBean(TrnLedgerDetailDao.class);
		cusAccountedStatusDao = context.getBean(CusAccountedStatusDao.class);
		messageSourceAccessor = context.getBean(MessageSourceAccessor.class);
		accMappingDao = context.getBean(AccMappingDao.class);
	}

	/**
	 * 根据TrnLedgerDetail返回Ledgerdetail
	 * 
	 * @param model
	 * @return
	 */
	private static LedgerDetail getLedgerdetail(TrnLedgerDetail model) {

		LedgerDetail ledgerdetail = getInstance();
		if (model != null) {
			DomainHelper.setDomainData(ledgerdetail, model);
		}
		return ledgerdetail;
	}

	/**
	 * 根据交易流水创建台账
	 * 
	 * @param transaction
	 * @return
	 */
	public List<LedgerDetail> createLedgerdetail(Transaction transaction,
			String entryKey, Date accDate) {

		if (entryKey == null) {
			return null;
		}
		List<AccEntryCfg> accEntryCfgList = AccEntryCfg.getInstance()
				.getAllAccEntryList(entryKey);
		if (accEntryCfgList == null || accEntryCfgList.size() == 0) {
			return null;
		}
		List<LedgerDetail> ledgerDetailList = getLedgerDetailList(transaction,
				accEntryCfgList, accDate);
		return ledgerDetailList;
	}

	/**
	 * 落地
	 * 
	 * @return
	 */
	public boolean save() {

		int ret = trnLedgerDetailDaoDao.insert(this);
		if (ret > 0) {
			return true;
		} else {
			return false;
		}
	}

	public LedgerDetail selectByPrimaryKey(String ledTranNo) {
		TrnLedgerDetail trnLedgerDetail = trnLedgerDetailDaoDao
				.selectByPrimaryKey(ledTranNo);
		if (trnLedgerDetail == null) {
			return null;
		}
		return getLedgerdetail(trnLedgerDetail);
	}

	/**
	 * 根据会计分录、前置流水、记账日期生成台帐 如果存在入账分录，并且入账状态为不可入账，就不记保证金分录和如果收款方日扣手续费分录
	 * 
	 * @param transaction
	 * @param cfgAccEntryList
	 * @param accDate
	 * @return
	 */
	private List<LedgerDetail> getLedgerDetailList(Transaction transaction,
			List<AccEntryCfg> cfgAccEntryList, Date accDate) {

		List<LedgerDetail> ledgerdetailList = new ArrayList<LedgerDetail>(
				cfgAccEntryList.size());
		LedgerDetail ledgerdetail = null;

		// 是否能入账
		boolean canInAcc = true;
		for (AccEntryCfg accEntryCfg : cfgAccEntryList) {
			ledgerdetail = LedgerDetail.getInstance();
			// 如果是入账分录
			if (BasConstants.ACC_ENTRY_TYPE_ENTER.equals(accEntryCfg
					.getAccEntryType())) {
				// 查询收款方账户入账状态
				CusAccountedStatus cusAccountedStatus = cusAccountedStatusDao
						.selectByPrimaryKey(transaction.getPayeeAccCode());
				// 如果收款方的入账状态为不可入账
				if (null != cusAccountedStatus
						&& BasConstants.IN_ACC_STATUS_CANNOT
								.equals(cusAccountedStatus.getInAccStatus())) {
					canInAcc = false;
					continue;
				}
				transaction.setBlnStatus(BasConstants.BLN_STATUS_ENTERED);
				transaction.setBlnDate(accDate);
				transaction.setFinnishTime(new Date());
				if (BasConstants.IS_DEPOSIT_TRUE.equals(transaction
						.getIsDeposit())
						&& BasConstants.DEPOSIT_STATUS_NOTGATHER
								.equals(transaction.getDepositStatus())) {
					transaction
							.setDepositStatus(BasConstants.DEPOSIT_STATUS_GATHERED);
				}
			}
			// 如果商户入账状态不可入账，不记保证金分录和收款方日扣手续费分录
			if (!canInAcc) {
				if (BasConstants.ACC_ENTRY_CODE_HANDCHARGE.equals(accEntryCfg
						.getAccEntryCode())) {
					continue;
				}
				if (BasConstants.ACC_ENTRY_CODE_GATHERDEPOSIT
						.equals(accEntryCfg.getAccEntryCode())) {
					continue;
				}
			}
			// 普通处理
			dealCommon(transaction, accEntryCfg, accDate, ledgerdetail);
			// 第一个借贷处理
			dealFirstDC(transaction, accEntryCfg, ledgerdetail);
			// 第二个借贷处理
			dealSecondDC(transaction, accEntryCfg, ledgerdetail);
			// 第三个借贷处理
			dealThirdDC(transaction, accEntryCfg, ledgerdetail);
			// 第四个借贷处理
			dealFourthDC(transaction, accEntryCfg, ledgerdetail);
			// 台账试算平衡校验
			checkBalance(ledgerdetail);
			if (!ValidateUtil.isEmpty(ledgerdetail.getFirSubCode1st())) {
				ledgerdetailList.add(ledgerdetail);
			}
		}
		return ledgerdetailList;
	}

	/**
	 * 普通处理
	 * 
	 * @param transaction
	 * @param accEntryCfg
	 * @param accDate
	 * @param ledgerDetail
	 * @throws ParseException
	 */
	public void dealCommon(Transaction transaction, AccEntryCfg accEntryCfg,
			Date accDate, LedgerDetail ledgerDetail) {

		ledgerDetail.setAccEntryCode(accEntryCfg.getAccEntryCode());
		ledgerDetail.setAccTranNo(transaction.getAccTranNo());
		ledgerDetail.setAccEntryType(accEntryCfg.getAccEntryType());
		ledgerDetail.setCcyCode(transaction.getCcyCode());
		ledgerDetail.setAppTranDate(transaction.getAppTranDate());
		ledgerDetail.setSubmitAccDate(accDate);
		ledgerDetail.setAccStatus(BasConstants.ACC_STATUS_SUCCESS);
		ledgerDetail.setAccDate(accDate);
		ledgerDetail.setIpsBillNo(transaction.getIpsBillNo());
		ledgerDetail.setAppTranNo(transaction.getAppTranNo());
		ledgerDetail.setTranType(transaction.getTranType());
		ledgerDetail.setSysCode(transaction.getSysCode());
		ledgerDetail.setCusTranNo(transaction.getCusTranNo());
		ledgerDetail.setTranRemark(transaction.getTranRemark());
		ledgerDetail.setCreateTime(DateUtil.getCurrentTimeStamp());
		ledgerDetail.setCreateBy(transaction.getModifyBy());
		ledgerDetail.setModifyTime(ledgerDetail.getCreateTime());
		ledgerDetail.setModifyBy(ledgerDetail.getCreateBy());
	}

	/**
	 * 第四个借贷处理
	 * 
	 * @param transaction
	 * @param accEntryCfg
	 * @param ledgerDetail
	 */
	private void dealFourthDC(Transaction transaction, AccEntryCfg accEntryCfg,
			LedgerDetail ledgerDetail) {

		if (!ValidateUtil.isEmpty(accEntryCfg.getSubDir4th())) {
			// 设置金额
			ledgerDetail.setTranAmt4th(getRightMoney(transaction,
					accEntryCfg.getSubAmtType4th()));
			// 如果金额为零或者为空则不进行后续操作
			if (null == ledgerDetail.getTranAmt4th()
					|| ledgerDetail.getTranAmt4th().compareTo(
							new BigDecimal("0")) == 0) {
				ledgerDetail.setTranAmt4th(null);
				return;
			}
			if (BasConstants.IS_RED_TRUE.equals(accEntryCfg.getSubAmtSign4th())) {
				ledgerDetail.setTranAmt4th(ledgerDetail.getTranAmt4th()
						.multiply(new BigDecimal("-1")));
			}
			ledgerDetail.setSubCode4th(accEntryCfg.getSubCode4th());
			ledgerDetail.setRelSubCode4th(accEntryCfg.getRelSubCode4th());
			ledgerDetail.setSubIndex4th(accEntryCfg.getSubIndex4th());
			ledgerDetail.setSeqNo4th(accEntryCfg.getSeqNo4th());
			ledgerDetail.setSubType4th(accEntryCfg.getSubType4th());
			ledgerDetail.setCdFlag4th(accEntryCfg.getCdFlag4th());
			// 设置账户号
			String accCode4th = getRightAccCode(accEntryCfg.getSubDir4th(),
					transaction, accEntryCfg.getSubAmtType4th(),
					accEntryCfg.getSubType4th());
			ledgerDetail.setAccCode4th(accCode4th);
			// 设置实际账户号和实际科目号
			String relSubCode4th = getRelSubCode(ledgerDetail.getSubType4th(),
					accEntryCfg.getSubCode4th(), transaction,
					ledgerDetail.getAccCode4th());
			ledgerDetail.setRelSubCode4th(relSubCode4th);
			SubAccInfo subAccInfo = SubAccInfo.getInstance().getSubAccInfo(
					ledgerDetail.getSubType4th(),
					ledgerDetail.getRelSubCode4th(),
					ledgerDetail.getAccCode4th(), transaction.getCcyCode());
			ledgerDetail.setSubAccCode4th(subAccInfo.getSubAccCode());
		}
	}

	/**
	 * 第三个借贷处理
	 * 
	 * @param transaction
	 * @param accEntryCfg
	 * @param ledgerdetail
	 */
	private void dealThirdDC(Transaction transaction, AccEntryCfg accEntryCfg,
			LedgerDetail ledgerdetail) {

		if (!ValidateUtil.isEmpty(accEntryCfg.getSubDir3rd())) {
			// 设置金额
			ledgerdetail.setTranAmt3rd(getRightMoney(transaction,
					accEntryCfg.getSubAmtType3rd()));
			// 如果金额为零或者为空则不进行后续操作
			if (null == ledgerdetail.getTranAmt3rd()
					|| ledgerdetail.getTranAmt3rd().compareTo(
							new BigDecimal("0")) == 0) {
				ledgerdetail.setTranAmt3rd(null);
				return;
			}
			if (BasConstants.IS_RED_TRUE.equals(accEntryCfg.getSubAmtSign3rd())) {
				ledgerdetail.setTranAmt3rd(ledgerdetail.getTranAmt3rd()
						.multiply(new BigDecimal("-1")));
			}
			ledgerdetail.setSubCode3rd(accEntryCfg.getSubCode3rd());
			ledgerdetail.setSubIndex3rd(accEntryCfg.getSubIndex3rd());
			ledgerdetail.setSeqNo3rd(accEntryCfg.getSeqNo3rd());
			ledgerdetail.setSubType3rd(accEntryCfg.getSubType3rd());
			ledgerdetail.setCdFlag3rd(accEntryCfg.getCdFlag3rd());
			// 设置账户号
			String accCode3rd = getRightAccCode(accEntryCfg.getSubDir3rd(),
					transaction, accEntryCfg.getSubAmtType3rd(),
					accEntryCfg.getSubType3rd());
			ledgerdetail.setAccCode3rd(accCode3rd);
			// 设置实际账户号和实际科目号
			String relSubCode3rd = getRelSubCode(ledgerdetail.getSubType3rd(),
					accEntryCfg.getSubCode3rd(), transaction,
					ledgerdetail.getAccCode3rd());
			ledgerdetail.setRelSubCode3rd(relSubCode3rd);
			SubAccInfo subAccInfo = SubAccInfo.getInstance().getSubAccInfo(
					ledgerdetail.getSubType3rd(),
					ledgerdetail.getRelSubCode3rd(),
					ledgerdetail.getAccCode3rd(), transaction.getCcyCode());
			ledgerdetail.setSubAccCode3rd(subAccInfo.getSubAccCode());
		}
	}

	/**
	 * 第二个借贷处理
	 * 
	 * @param transaction
	 * @param accEntryCfg
	 * @param ledgerdetail
	 */
	private void dealSecondDC(Transaction transaction, AccEntryCfg accEntryCfg,
			LedgerDetail ledgerdetail) {

		if (!ValidateUtil.isEmpty(accEntryCfg.getSubDir2nd())) {

			// 设置金额
			ledgerdetail.setTranAmt2nd(getRightMoney(transaction,
					accEntryCfg.getSubAmtType2nd()));
			// 如果金额为零或者为空则不进行后续操作
			if (null == ledgerdetail.getTranAmt2nd()
					|| ledgerdetail.getTranAmt2nd().compareTo(
							new BigDecimal("0")) == 0) {
				ledgerdetail.setTranAmt2nd(null);
				return;
			}
			if (BasConstants.IS_RED_TRUE.equals(accEntryCfg.getSubAmtSign2nd())) {
				ledgerdetail.setTranAmt2nd(ledgerdetail.getTranAmt2nd()
						.multiply(new BigDecimal("-1")));
			}
			ledgerdetail.setSubCode2nd(accEntryCfg.getSubCode2nd());
			ledgerdetail.setSubIndex2nd(accEntryCfg.getSubIndex2nd());
			ledgerdetail.setSeqNo2nd(accEntryCfg.getSeqNo2nd());
			ledgerdetail.setSubType2nd(accEntryCfg.getSubType2nd());
			ledgerdetail.setCdFlag2nd(accEntryCfg.getCdFlag2nd());
			// 设置账户号
			String accCode2nd = getRightAccCode(accEntryCfg.getSubDir2nd(),
					transaction, accEntryCfg.getSubAmtType2nd(),
					accEntryCfg.getSubType2nd());
			ledgerdetail.setAccCode2nd(accCode2nd);
			// 设置实际账户号和实际科目号
			String relSubCode2nd = getRelSubCode(ledgerdetail.getSubType2nd(),
					accEntryCfg.getSubCode2nd(), transaction,
					ledgerdetail.getAccCode2nd());
			ledgerdetail.setRelSubCode2nd(relSubCode2nd);
			SubAccInfo subAccInfo = SubAccInfo.getInstance().getSubAccInfo(
					ledgerdetail.getSubType2nd(),
					ledgerdetail.getRelSubCode2nd(),
					ledgerdetail.getAccCode2nd(), transaction.getCcyCode());
			ledgerdetail.setSubAccCode2nd(subAccInfo.getSubAccCode());
		}
	}

	/**
	 * 第一个借贷处理
	 * 
	 * @param transaction
	 * @param accEntryCfg
	 * @param ledgerDetail
	 */
	private void dealFirstDC(Transaction transaction, AccEntryCfg accEntryCfg,
			LedgerDetail ledgerDetail) {

		if (!ValidateUtil.isEmpty(accEntryCfg.getSubDir1st())) {
			// 设置金额
			ledgerDetail.setTranAmt1st(getRightMoney(transaction,
					accEntryCfg.getSubAmtType1st()));
			// 如果金额为零或者为空则不进行后续操作
			if (null == ledgerDetail.getTranAmt1st()
					|| ledgerDetail.getTranAmt1st().compareTo(
							new BigDecimal("0")) == 0) {
				ledgerDetail.setTranAmt1st(null);
				return;
			}
			if (BasConstants.IS_RED_TRUE.equals(accEntryCfg.getSubAmtSign1st())) {// 如果科目发送额正负号是负号
				ledgerDetail.setTranAmt1st(ledgerDetail.getTranAmt1st()
						.multiply(new BigDecimal("-1")));
			}
			// 设置一级科目号
			ledgerDetail.setFirSubCode1st(accEntryCfg.getSubCode1st());
			// 设计一级科目序号
			ledgerDetail.setSubIndex1st(accEntryCfg.getSubIndex1st());
			// 设置一级科目科目附属编号1_非客户账使用 必填
			ledgerDetail.setSeqNo1st(accEntryCfg.getSeqNo1st());
			// 设置科目类型1_0：内部 1：客户
			ledgerDetail.setSubType1st(accEntryCfg.getSubType1st());
			// 设置一级科目借贷方向
			ledgerDetail.setCdFlag1st(accEntryCfg.getCdFlag1st());
			// 设置交易账户号
			String accCode1st = getRightAccCode(accEntryCfg.getSubDir1st(),
					transaction, accEntryCfg.getSubAmtType1st(),
					accEntryCfg.getSubType1st());
			ledgerDetail.setAccCode1st(accCode1st);
			// 设置实际账户号和实际科目号
			String relSubCode1st = getRelSubCode(ledgerDetail.getSubType1st(),
					accEntryCfg.getSubCode1st(), transaction,
					ledgerDetail.getAccCode1st());
			ledgerDetail.setRelSubCode1st(relSubCode1st);
			SubAccInfo subAccInfo = SubAccInfo.getInstance().getSubAccInfo(
					ledgerDetail.getSubType1st(),
					ledgerDetail.getRelSubCode1st(),
					ledgerDetail.getAccCode1st(), transaction.getCcyCode());
			if (subAccInfo != null)
				// 设置功能账户号
				ledgerDetail.setSubAccCode1st(subAccInfo.getSubAccCode());
		}
	}

	/**
	 * 根据科目发生金额类型获取金额
	 * 
	 * @param transaction
	 * @param subAmtType
	 * @return
	 */
	private BigDecimal getRightMoney(Transaction transaction, String subAmtType) {

		if (BasConstants.SUB_AMT_TYPE_TRAN.equals(subAmtType)) {// 如果是交易金额
			return transaction.getTranAmt();
		} else if (BasConstants.SUB_AMT_TYPE_ORDER.equals(subAmtType)) {// 如果是订单金额
			return transaction.getOrdAmt();
		} else if (BasConstants.SUB_AMT_TYPE_FEE.equals(subAmtType)) {// 如果是手续费
			return transaction.getFeeAmt();
		} else if (BasConstants.SUB_AMT_TYPE_BAIL.equals(subAmtType)) {// 如果是保证金
			return transaction.getSecurityDeposit();
		} else if (BasConstants.SUB_AMT_TYPE_COST.equals(subAmtType)) {// 如果是成本
			return transaction.getBankCost();
		} else if (BasConstants.SUB_AMT_TYPE_SETTLE.equals(subAmtType)) {// 如果是结算金额
			return transaction.getTranAmt().subtract(transaction.getFeeAmt());
		}
		return null;
	}

	/**
	 * 获取实际的科目号
	 * 
	 * @param subType
	 * @param subCode
	 * @param transaction
	 * @param accCode
	 * @return
	 */
	private String getRelSubCode(String subType, String subCode,
			Transaction transaction, String accCode) {

		String relSubCode = null;
		// 客户科目
		if (BasConstants.SUB_TYPE_CUS.equals(subType)) {
			if (ValidateUtil.isEmpty(accCode)) {
				throw new BizException(messageSourceAccessor.getMessage(
						MsgCodeList.ERROR_302001, new Object[] { accCode
								+ "商户账户号" }));
			}
			relSubCode = subCode + accCode;
		} else {
			if ("31".equals(subCode)) {// 应收银行
				if (ValidateUtil.isEmpty(transaction.getSupplierCode())) {
					throw new BizException(messageSourceAccessor.getMessage(
							MsgCodeList.ERROR_302001,
							new Object[] { transaction.getSupplierCode()
									+ "供应商号" }));
				}
				if (ValidateUtil.isEmpty(transaction.getBankAccCode())) {
					throw new BizException(messageSourceAccessor.getMessage(
							MsgCodeList.ERROR_302001,
							new Object[] { transaction.getBankAccCode()
									+ "银行帐号" }));
				}
				// 实际科目号=科目号+供应商编号+通道对应银行收款账号
				relSubCode = subCode + transaction.getSupplierCode()
						+ transaction.getBankAccCode();
			}
			// 12 其他费用收入，11 手续费收入，21 银行成本，22 非银行成本
			else if ("12".equals(subCode) || "11".equals(subCode)
					|| "21".equals(subCode) || "22".equals(subCode)) {
				if (ValidateUtil.isEmpty(transaction.getSysCode())) {
					throw new BizException(
							messageSourceAccessor.getMessage(
									MsgCodeList.ERROR_302001,
									new Object[] { transaction.getSysCode()
											+ "来源系统编号" }));
				}
				if (ValidateUtil.isEmpty(transaction.getPrdCode())) {
					throw new BizException(messageSourceAccessor.getMessage(
							MsgCodeList.ERROR_302001,
							new Object[] { transaction.getPrdCode() + "产品号" }));
				}
				// 实际科目号=科目号+SysCode+PrdCode
				relSubCode = subCode + transaction.getSysCode()
						+ transaction.getPrdCode();
			} else {
				relSubCode = subCode;
			}
		}
		return relSubCode;
	}

	/**
	 * 根据科目方向、记账流水、科目发生额类型、科目类型获取交易账户
	 * 
	 * @param subDir
	 * @param transaction
	 * @param subAmtType
	 * @param subType
	 * @return
	 */
	private String getRightAccCode(String subDir, Transaction transaction,
			String subAmtType, String subType) {

		String accCode = null;
		// 如果是客户科目
		if (BasConstants.SUB_TYPE_CUS.equals(subType)) {
			// 如果科目方向是收款方
			if (BasConstants.SUB_DIR_REC.equals(subDir)) {
				accCode = transaction.getPayeeAccCode();
			}
			// 如果科目方向是付款方
			else if (BasConstants.SUB_DIR_PAY.equals(subDir)) {
				accCode = transaction.getPayerAccCode();
			}
			// 如果科目方向是第三方
			else if (BasConstants.SUB_DIR_THD.equals(subDir)) {
				accCode = transaction.getThirdAccCode();
			}
			// 如果科目方向是付款方（标）
			else if (BasConstants.SUB_DIR_PAY_STD.equals(subDir)) {
				accCode = accMappingDao
						.selectStdAccCodeBySpeAccCode(transaction
								.getPayerAccCode());
			}
			// 如果科目方向是收款方（标）
			else if (BasConstants.SUB_DIR_REC_STD.equals(subDir)) {
				accCode = accMappingDao
						.selectStdAccCodeBySpeAccCode(transaction
								.getPayeeAccCode());
			}

			// 如果科目发生额类型是手续费
			if (BasConstants.SUB_AMT_TYPE_FEE.equals(subAmtType)) {
				// 如果是收款方承担手续费
				if (BasConstants.SUB_DIR_REC.equals(transaction.getFeeFlag())) {
					accCode = transaction.getPayeeAccCode();
				}
				// 如果是付款方承担手续费
				else if (BasConstants.SUB_DIR_PAY.equals(transaction
						.getFeeFlag())) {
					accCode = transaction.getPayerAccCode();
				}
				// 如果是第三方承担手续费
				else if (BasConstants.SUB_DIR_THD.equals(transaction
						.getFeeFlag())) {
					accCode = transaction.getThirdAccCode();
				}
			}
		}
		return accCode;
	}

	/**
	 * 台账试算平衡校验
	 * 
	 * @param ledgerdetail
	 */
	public void checkBalance(LedgerDetail ledgerdetail) {

		BigDecimal dAmt = new BigDecimal("0");
		BigDecimal cAmt = new BigDecimal("0");
		if (BasConstants.CD_FLAG_D.equals(ledgerdetail.getCdFlag1st())) {
			dAmt = dAmt.add(ledgerdetail.getTranAmt1st());
		} else if (BasConstants.CD_FLAG_C.equals(ledgerdetail.getCdFlag1st())) {
			cAmt = cAmt.add(ledgerdetail.getTranAmt1st());
		}
		if (BasConstants.CD_FLAG_D.equals(ledgerdetail.getCdFlag2nd())) {
			dAmt = dAmt.add(ledgerdetail.getTranAmt2nd());
		} else if (BasConstants.CD_FLAG_C.equals(ledgerdetail.getCdFlag2nd())) {
			cAmt = cAmt.add(ledgerdetail.getTranAmt2nd());
		}
		if (BasConstants.CD_FLAG_D.equals(ledgerdetail.getCdFlag3rd())) {
			dAmt = dAmt.add(ledgerdetail.getTranAmt3rd());
		} else if (BasConstants.CD_FLAG_C.equals(ledgerdetail.getCdFlag3rd())) {
			cAmt = cAmt.add(ledgerdetail.getTranAmt3rd());
		}
		if (BasConstants.CD_FLAG_D.equals(ledgerdetail.getCdFlag4th())) {
			dAmt = dAmt.add(ledgerdetail.getTranAmt4th());
		} else if (BasConstants.CD_FLAG_C.equals(ledgerdetail.getCdFlag4th())) {
			cAmt = cAmt.add(ledgerdetail.getTranAmt4th());
		}
		if (dAmt.compareTo(cAmt) != 0) {
			LOGGER.error("台帐试算不平衡：dAmt：{},cAmt{}",
					new Object[] { dAmt.toString(), cAmt.toString() });
			throw new BizException(
					messageSourceAccessor.getMessage(MsgCodeList.ERROR_505015));
		}
	}

}
