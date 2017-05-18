package com.somnus.smart.domain.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.somnus.smart.base.domain.TrnLedgerDetail;
import com.somnus.smart.domain.AccountContext;
import com.somnus.smart.domain.DomainHelper;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

/**
 * 记账
 */
public class Account {
	/** Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(Account.class);

	/** 交易账户锁 */
	private SubAccInfoLock subAccInfoLock;

	/** 记账上下文 */
	private static AccountContext accountContext = AccountContext.getContext();

	/** spring 事务模板 */
	private static TransactionTemplate transactionTemplate;

	public Account() {
	}

	public static Account getInstance() {
		return (Account) DomainHelper.getDomainInstance(Account.class);
	}

	public static void init(ApplicationContext context) {
		transactionTemplate = context.getBean(TransactionTemplate.class);
	}

	/**
	 * 同步记账
	 * 
	 * @param transaction
	 * @param entryKey
	 * @param accDate
	 * @param checkRed
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void synAccount(Transaction transaction, String entryKey,
			Date accDate, boolean checkRed, AccountCallBack callBack)
			throws Exception {
		try {
			synAccountNoTransaction(transaction, entryKey, accDate, checkRed,
					callBack);
			// 落地流水
			transaction.setAccStatus(BasConstants.ACC_STATUS_SUCCESS);
			transaction.save();
		} finally {
			accountContext.remove();
		}
	}

	/**
	 * 同步记账
	 * 
	 * @param transaction
	 * @param entryKey
	 * @param accDate
	 * @param checkRed
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void synAccountNoTransaction(Transaction transaction,
			String entryKey, Date accDate, boolean checkRed,
			AccountCallBack callBack) throws Exception {
		try {
			// 设置批量记账
			accountContext.setBatchAccount(true);
			subAccInfoLock = SubAccInfoLock.getInstance();
			// 创建台账
			LedgerDetail ledgerdetail = LedgerDetail.getInstance();
			List<LedgerDetail> ledgerdetails = ledgerdetail.createLedgerdetail(
					transaction, entryKey, accDate);
			// 记账
			synMultiAccount(ledgerdetails, checkRed);
			// 记账回调
			if (callBack != null) {
				callBack.callBack();
			}
		} finally {
			accountContext.remove();
		}
	}

	/**
	 * 补记账
	 * 
	 * @param transaction
	 * @param entryKey
	 * @param accDate
	 * @param checkRed
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void addAccount(Transaction transaction, String entryKey,
			Date accDate, boolean checkRed, AccountCallBack callBack)
			throws Exception {
		try {
			// 设置批量记账
			accountContext.setBatchAccount(true);
			subAccInfoLock = SubAccInfoLock.getInstance();
			// 创建台账
			LedgerDetail ledgerdetail = LedgerDetail.getInstance();
			List<LedgerDetail> ledgerdetails = ledgerdetail.createLedgerdetail(
					transaction, entryKey, accDate);
			// 记账
			synMultiAccount(ledgerdetails, checkRed);
			// 更新流水入账状态
			for (TrnLedgerDetail trnLedgerDetail : ledgerdetails) {
				if (BasConstants.ACC_ENTRY_TYPE_ENTER.equals(trnLedgerDetail
						.getAccEntryType())) {
					transaction.setBlnStatus(BasConstants.BLN_STATUS_ENTERED);
					transaction.setBlnDate(accDate);
					transaction.setFinnishTime(new Date());
					if (BasConstants.DEPOSIT_STATUS_NOTGATHER
							.equals(transaction.getDepositStatus())) {
						transaction
								.setDepositStatus(BasConstants.DEPOSIT_STATUS_GATHERED);
					}
				}
			}
			transaction.setModifyTime(DateUtil.getCurrentTimeStamp());
			transaction.setAccStatus(BasConstants.ACC_STATUS_SUCCESS);
			transaction.update();
			// 记账回调
			callBack.callBack();
		} catch (Exception e) {
			throw e;
		} finally {
			accountContext.remove();
		}

	}

	/**
	 * isp账户收单记账
	 * 
	 * @param transaction
	 * @param entryKey
	 * @param accDate
	 * @param checkRed
	 * @return
	 * @throws Exception
	 */
	public void ipsPayAccount(final Transaction transaction,
			final String entryKey, final Date accDate, final boolean checkRed)
			throws Exception {
		// 创建台账
		LedgerDetail ledgerDetail = LedgerDetail.getInstance();
		final List<LedgerDetail> ledgerDetails = ledgerDetail
				.createLedgerdetail(transaction, entryKey, accDate);
		// 第一阶段：落地台账和交易流水
		TransactionCallback<Boolean> saveLedgerDetailAndTransaction = new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				// 落地台账
				for (LedgerDetail ledgerDetail2 : ledgerDetails) {
					ledgerDetail2.save();
				}
				transaction.setAccStatus(BasConstants.ACC_STATUS_SUCCESS);
				transaction.save();
				return Boolean.TRUE;
			}
		};

		transactionTemplate.execute(saveLedgerDetailAndTransaction);

		// 第二阶段：落地第一笔台账对应的分账户明细和更新对应的账户余额

		TransactionCallback<Boolean> saveFirstAccDetailAndUpdateBal = new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				// 设置RelSubCode和SubCode映射
				setRelAccSubCodeSubCodeMap(ledgerDetails.subList(0, 1));
				// RelSubCode排序
				subAccInfoLock = SubAccInfoLock.getInstance();
				subAccInfoLock.lockSort(accountContext);
				accountContext.setBatchAccount(false);
				try {
					doAccount(checkRed, ledgerDetails.get(0));
				} catch (Exception e) {
					try {
						throw e;
					} catch (Exception e1) {
						LOGGER.error("", e);
					}
				} finally {
					accountContext.remove();
				}
				return Boolean.TRUE;
			}
		};

		transactionTemplate.execute(saveFirstAccDetailAndUpdateBal);

		// 第三阶段：落地第一笔台账以后对应的分账户明细和更新对应的账户余额

		TransactionCallback<Boolean> saveSurplusAccDetailAndUpdateBal = new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				// 设置RelSubCode和SubCode映射
				List<LedgerDetail> ledgerdetails = new ArrayList<LedgerDetail>();
				int size = ledgerDetails.size();
				for (int i = 1; i < size; i++) {
					ledgerdetails.add(ledgerDetails.get(i));
				}
				setRelAccSubCodeSubCodeMap(ledgerdetails);
				// RelSubCode排序
				subAccInfoLock.lockSort(accountContext);
				accountContext.setBatchAccount(true);
				try {
					multiAccount(ledgerdetails, false);
					return Boolean.TRUE;
				} catch (Exception e) {
					try {
						throw e;
					} catch (Exception e1) {
						LOGGER.error("", e);
					}
				} finally {
					accountContext.remove();
				}
				return Boolean.FALSE;
			}
		};

		transactionTemplate.execute(saveSurplusAccDetailAndUpdateBal);

	}

	/**
	 * 多笔
	 * 
	 * @param ledgerdetails
	 * @param checkRed
	 * @throws Exception
	 */
	@Transactional
	public void multiAccount(List<LedgerDetail> ledgerdetails, boolean checkRed)
			throws Exception {
		// 设置RelSubCode和SubCode映射
		setRelAccSubCodeSubCodeMap(ledgerdetails);
		// RelSubCode排序
		subAccInfoLock.lockSort(accountContext);
		// 记账
		for (LedgerDetail ledagrdel : ledgerdetails) {
			doAccount(checkRed, ledagrdel);
		}
	}

	/**
	 * 多笔
	 * 
	 * @param ledgerdetails
	 * @param checkRed
	 * @throws Exception
	 */
	@Transactional
	public void multiAccount(Transaction transaction, String entryKey,
			boolean checkRed, Date accDate) throws Exception {
		try {
			// 创建台账
			LedgerDetail ledgerDetail = LedgerDetail.getInstance();
			final List<LedgerDetail> ledgerDetails = ledgerDetail
					.createLedgerdetail(transaction, entryKey, accDate);
			// 设置RelSubCode和SubCode映射
			setRelAccSubCodeSubCodeMap(ledgerDetails);
			// RelSubCode排序
			subAccInfoLock.lockSort(accountContext);
			// 记账
			for (LedgerDetail ledagrdel : ledgerDetails) {
				ledagrdel.save();
				doAccount(checkRed, ledagrdel);
			}
		} finally {
			accountContext.remove();
		}
	}

	/**
	 * 多笔
	 * 
	 * @param ledgerdetails
	 * @param checkRed
	 * @throws Exception
	 */
	@Transactional
	public void synMultiAccount(List<LedgerDetail> ledgerdetails,
			boolean checkRed) throws Exception {
		// 设置RelSubCode和SubCode映射
		setRelAccSubCodeSubCodeMap(ledgerdetails);
		// RelSubCode排序
		subAccInfoLock.lockSort(accountContext);
		// 记账
		for (LedgerDetail ledagrdel : ledgerdetails) {
			ledagrdel.save();
			doAccount(checkRed, ledagrdel);
		}
	}

	@Transactional
	public void saveLedgerDetails(Transaction transaction, String entryKey,
			Date accDate) {
		// 创建台账
		LedgerDetail ledgerdetail = LedgerDetail.getInstance();
		List<LedgerDetail> ledgerdetails = ledgerdetail.createLedgerdetail(
				transaction, entryKey, accDate);
		// 记账
		for (LedgerDetail ledagrdel : ledgerdetails) {
			ledagrdel.save();
		}
	}

	/**
	 * 落地台账、落地交易明细、更新余额
	 * 
	 * @param checkRed
	 * @param ledagrdel
	 * @param isBatch
	 */
	@Transactional
	public void doAccount(boolean checkRed, LedgerDetail ledagrdel)
			throws Exception {
		// 根据台账创建分户明细账
		AccDetail accDetail = AccDetail.getInstance();
		List<AccDetail> accDetails = accDetail.createAccDetails(ledagrdel);
		// 锁SubAccInfo
		SubAccInfo subAccInfo = null;
		for (AccDetail accDel : accDetails) {
			accountContext.setBatchAccount(true);
			subAccInfo = subAccInfoLock.lockSubAccInfo(accDel.getRelSubCode(),
					accountContext);
			if (subAccInfo == null) {
				throw new BizException("锁" + accDel.getRelSubCode() + "失败！");
			}
			// 更新余额
			subAccInfo.updateBal(accDel, checkRed);
			accDel.setCurBal(subAccInfo.getCurBal());
			// 落地分户明细账
			accDel.save();
		}
	}

	/**
	 * 补记账（落地分账户明细和更新余额）
	 * 
	 * @param checkRed
	 * @param ledagrdel
	 * @throws Exception
	 */
	@Transactional
	public void addAccount(boolean checkRed, LedgerDetail ledgerDetail)
			throws Exception {
		try {
			if (subAccInfoLock == null) {
				subAccInfoLock = SubAccInfoLock.getInstance();
			}
			List<LedgerDetail> ledgerdetails = new ArrayList<LedgerDetail>();
			ledgerdetails.add(ledgerDetail);
			setRelAccSubCodeSubCodeMap(ledgerdetails);
			doAccount(checkRed, ledgerDetail);
		} catch (Exception e) {
			throw e;
		} finally {
			accountContext.remove();
		}
	}

	private void setRelAccSubCodeSubCodeMap(List<LedgerDetail> ledgerdetails) {
		Map<String, String> relSubCodeAndSubCode = new HashMap<String, String>();
		for (LedgerDetail ledgerdetailTemp : ledgerdetails) {
			if (ledgerdetailTemp.getRelSubCode1st() != null) {
				relSubCodeAndSubCode.put(ledgerdetailTemp.getRelSubCode1st(),
						ledgerdetailTemp.getSubAccCode1st());
			}
			if (ledgerdetailTemp.getRelSubCode2nd() != null) {
				relSubCodeAndSubCode.put(ledgerdetailTemp.getRelSubCode2nd(),
						ledgerdetailTemp.getSubAccCode2nd());
			}
			if (ledgerdetailTemp.getRelSubCode3rd() != null) {
				relSubCodeAndSubCode.put(ledgerdetailTemp.getRelSubCode3rd(),
						ledgerdetailTemp.getSubAccCode3rd());
			}
			if (ledgerdetailTemp.getRelSubCode4th() != null) {
				relSubCodeAndSubCode.put(ledgerdetailTemp.getRelSubCode4th(),
						ledgerdetailTemp.getSubAccCode4th());
			}
		}
		accountContext.putRelSubCodeSubCodeMap(relSubCodeAndSubCode);
	}

	/**
	 * 收单异步记账
	 * 
	 * @param transaction
	 * @param entryKey
	 * @param accDate
	 * @param checkRed
	 * @throws Exception
	 */
	public void asynAccount(final Transaction transaction, String entryKey,
			Date accDate, final boolean checkRed) throws Exception {
		// 创建台账
		LedgerDetail ledgerDetail = LedgerDetail.getInstance();
		final List<LedgerDetail> ledgerDetails = ledgerDetail
				.createLedgerdetail(transaction, entryKey, accDate);
		// 第一阶段：落地台账和更新交易流水
		TransactionCallback<Boolean> saveLedgerDetail = new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				// 落地台账
				for (LedgerDetail ledgerDetail2 : ledgerDetails) {
					ledgerDetail2.save();
				}
				transaction.setAccStatus(BasConstants.ACC_STATUS_SUCCESS);
				transaction.update();
				return Boolean.TRUE;
			}
		};

		Boolean saveLedgerSuccess = transactionTemplate
				.execute(saveLedgerDetail);
		if (!saveLedgerSuccess) {
			throw new BizException("落地台账失败 AccTranNo:"
					+ transaction.getAccTranNo());
		}

		// 第二阶段：落地分账户明细和更新对应的账户余额

		if (subAccInfoLock == null) {
			subAccInfoLock = SubAccInfoLock.getInstance();
		}
		ArrayList<LedgerDetail> ledgerDetailList = null;
		try {
			for (final LedgerDetail ledgerDetail2 : ledgerDetails) {
				ledgerDetailList = new ArrayList<LedgerDetail>();
				ledgerDetailList.add(ledgerDetail2);
				// RelSubCode排序
				setRelAccSubCodeSubCodeMap(ledgerDetailList);
				subAccInfoLock.lockSort(accountContext);
				TransactionCallback<Object> updateBlnAndInsertAccDel = new TransactionCallback<Object>() {

					@Override
					public Object doInTransaction(TransactionStatus status) {
						try {
							doAccount(checkRed, ledgerDetail2);
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
						}
						return null;
					}
				};
				transactionTemplate.execute(updateBlnAndInsertAccDel);
			}
		} finally {
			accountContext.remove();
		}
	}
}
