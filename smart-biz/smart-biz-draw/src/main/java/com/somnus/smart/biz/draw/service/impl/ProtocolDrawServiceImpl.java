package com.somnus.smart.biz.draw.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.dao.CusAccountedStatusDao;
import com.somnus.smart.base.dao.CusSubAccInfoDao;
import com.somnus.smart.base.dao.MerAccountDao;
import com.somnus.smart.base.dao.TrnTransferDao;
import com.somnus.smart.base.domain.CusAccountedStatus;
import com.somnus.smart.base.domain.CusSubaccinfo;
import com.somnus.smart.base.domain.MerAccount;
import com.somnus.smart.base.domain.MerMerchant;
import com.somnus.smart.base.domain.TrnTransfer;
import com.somnus.smart.biz.draw.resource.impl.DrawResourceImpl;
import com.somnus.smart.biz.draw.service.ProtocolDrawService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.AccountCallBack;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.domain.account.TransferTransaction;
import com.somnus.smart.message.account.ProtocolDrawRequest;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.service.common.CusSubAccInfoUtil;
import com.somnus.smart.service.common.DrawConstants;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

/**
 * 协议出款实现
 */
@Service
public class ProtocolDrawServiceImpl implements ProtocolDrawService {

	protected static Logger log = LoggerFactory.getLogger(DrawResourceImpl.class);
	/** merAccountDao */
	@Autowired
	private MerAccountDao 			merAccountDao;

	/** cusSubAccInfoDao */
	@Autowired
	private CusSubAccInfoDao 		cusSubAccInfoDao;
	/** basBizService */
	@Resource
	private BasBizService 			basBizService;
	/** cusAccountedStatusDao */
	@Autowired
	private CusAccountedStatusDao 	cusAccountedStatusDao;
	/** trnTransferDao */
	@Autowired
	private TrnTransferDao 			trnTransferDao;
	
	@Autowired
    private MessageSourceAccessor 	msa;

	@Override
	@Transactional
	public void protocolDraw(TrnTransfer trnTransfer, Date accDate)
			throws Exception {
		CusSubaccinfo cusSubaccinfo = cusSubAccInfoDao.selectBycode(
				CusSubAccInfoUtil.BIZ_CUS_SUB_INFO_TBL,
				DrawConstants.REL_SUB_CODE_FREE + trnTransfer.getMerAccCode(),
				trnTransfer.getMerAccCode(), trnTransfer.getCcyCode());
		if (null == cusSubaccinfo) {
			log.error("relSubCode:" + DrawConstants.REL_SUB_CODE_FREE
					+ trnTransfer.getMerAccCode() + " CcyCode:"
					+ trnTransfer.getCcyCode());
			throw new BizException(msa.getMessage(MsgCodeList.ERROR_302041, new Object[] {}));
		}
		updateCusAccountedStatus(trnTransfer);

		BigDecimal bizFreezeBal = new BigDecimal(0).add(cusSubaccinfo
				.getBizFreezeBal());
		BigDecimal magFreezeBal = new BigDecimal(0).add(cusSubaccinfo
				.getMagFreezeBal());
		if (bizFreezeBal.intValue() < 0) {
			bizFreezeBal = new BigDecimal(0);
		}
		if (magFreezeBal.intValue() < 0) {
			magFreezeBal = new BigDecimal(0);
		}
		MerAccount merAccount = basBizService.getMerAccount(
				trnTransfer.getMerCode(), trnTransfer.getMerAccCode());
		BigDecimal drawAmt = BigDecimal.ZERO;
		drawAmt = cusSubaccinfo.getAvailBal();
		drawAmt = drawAmt.subtract(merAccount.getBalFloor());
		// 校验是否允许出款
		boolean isAllowOut = basBizService.isAccAllowOut(
				BasConstants.BIZ_KIND_CUS, trnTransfer.getMerCode(),
				trnTransfer.getMerAccCode());
		if (!isAllowOut) {
			drawAmt = BigDecimal.ZERO;
			log.warn("商户交易账户{}出款关闭，划款金额为零！", trnTransfer.getMerAccCode());
		}
		trnTransfer.setTranAmount(drawAmt);
		trnTransfer.setCurBal(cusSubaccinfo.getCurBal());
		trnTransfer.setReserveBal(merAccount.getBalFloor());
		trnTransfer.setFreezeBal(cusSubaccinfo.getCurBal().subtract(
				cusSubaccinfo.getAvailBal()));
		trnTransfer.setStatus(DrawConstants.TRANSFER_STATUS_SUCCESS);
		trnTransfer.setModifyTime(DateUtil.getCurrentTimeStamp());
		boolean continueFlag = true;
		if (drawAmt.compareTo(new BigDecimal(0)) <= 0) {
			log.info(cusSubaccinfo.getSubAccCode() + "出款金额：" + drawAmt
					+ "小于等于零");
			trnTransfer.setTranAmount(new BigDecimal(0));
			continueFlag = false;
		}
		if (drawAmt.compareTo(merAccount.getMinTransfer()) < 0) {
			log.info(cusSubaccinfo.getSubAccCode() + "出款金额：" + drawAmt
					+ "小于最低划款金额：" + merAccount.getMinTransfer());
			trnTransfer.setTranAmount(new BigDecimal(0));
			continueFlag = false;
		}
		int upCount = trnTransferDao.updateByPrimaryKeySelective(trnTransfer);
		if (upCount != 1) {
			throw new BizException(msa.getMessage(MsgCodeList.ERROR_302042, new Object[] {}));
		}
		if (!continueFlag) {
			return;
		}
		// 协议出款记账
		protocolDrawSynAccountDo(accDate, merAccount, drawAmt);
	}

	/**
	 * 协议划款记账
	 * 
	 * @param accDate
	 * @param merAccount
	 * @param drawAmt
	 * @throws Exception
	 */
	private void protocolDrawSynAccountDo(Date accDate, MerAccount merAccount,
			BigDecimal drawAmt) throws Exception {
		MerMerchant merMerchant = basBizService.getMerMerchant(merAccount
				.getMerCode());
		final DrawTransaction drawTransaction = createDrawTransaction(accDate,
				merAccount, drawAmt, merMerchant);
		Transaction transaction = createTransaction(drawTransaction);
		Account account = Account.getInstance();
		account.synAccountNoTransaction(transaction, DrawConstants.ENTRY_KEY_PROTOCOLDRAW_BLN_PRE, accDate, false,new AccountCallBack() {

			@Override
			public Object callBack() throws Exception {
				drawTransaction.save();
				return drawTransaction;
			}
		});
	}

	@Transactional
	private void protocolDraw(MerAccount merAccount, Date accDate)
			throws Exception {
		CusSubaccinfo cusSubaccinfo = cusSubAccInfoDao.selectBycode(
				CusSubAccInfoUtil.BIZ_CUS_SUB_INFO_TBL,
				DrawConstants.REL_SUB_CODE_FREE + merAccount.getAcctCode(),
				merAccount.getAcctCode(), merAccount.getCurrency());
		if (null == cusSubaccinfo) {
			log.error("relSubCode:" + DrawConstants.REL_SUB_CODE_FREE
					+ merAccount.getAcctCode() + " CcyCode:"
					+ merAccount.getCurrency());
			throw new BizException(msa.getMessage(MsgCodeList.ERROR_302041, new Object[] {}));
		}
		CusAccountedStatus cusAccountedStatus = new CusAccountedStatus();
		cusAccountedStatus.setMerAccCode(merAccount.getAcctCode());
		cusAccountedStatus.setInAccStatus(DrawConstants.IN_ACC_STATUS_CAN);
		cusAccountedStatus.setModifyTime(DateUtil.getCurrentTimeStamp());
		cusAccountedStatus.setModifyBy(DrawConstants.DEFAULT_OPERATOR);
		cusAccountedStatusDao.updateByPrimaryKeySelective(cusAccountedStatus);

		BigDecimal bizFreezeBal = new BigDecimal(0).add(cusSubaccinfo
				.getBizFreezeBal());
		BigDecimal magFreezeBal = new BigDecimal(0).add(cusSubaccinfo
				.getMagFreezeBal());
		if (bizFreezeBal.intValue() < 0) {
			bizFreezeBal = new BigDecimal(0);
		}
		if (magFreezeBal.intValue() < 0) {
			magFreezeBal = new BigDecimal(0);
		}
		BigDecimal drawAmt = cusSubaccinfo.getCurBal().subtract(bizFreezeBal)
				.subtract(magFreezeBal);
		drawAmt = drawAmt.subtract(merAccount.getBalFloor());
		if (drawAmt.compareTo(new BigDecimal(0)) <= 0) {
			log.info(cusSubaccinfo.getSubAccCode() + "出款金额：" + drawAmt
					+ "小于等于零");
			return;
		}
		if (drawAmt.compareTo(merAccount.getMinTransfer()) < 0) {
			log.info(cusSubaccinfo.getSubAccCode() + "出款金额：" + drawAmt
					+ "小于最低划款金额：" + merAccount.getMinTransfer());
			return;
		}
		// 协议出款记账
		protocolDrawSynAccountDo(accDate, merAccount, drawAmt);
	}

	@Override
	public List<MerAccount> selectAllProtocolDrawAccounts() {
		return merAccountDao.selectAllProtocolDrawAccounts();
	}

	@Override
	@Transactional
	public void protocolSpecificDraw(String merAccCode, Date accDate)
			throws Exception {
		List<MerAccount> merAccountList = merAccountDao
				.selectByAcctcode(merAccCode);
		for (MerAccount merAccount : merAccountList) {
			protocolDraw(merAccount, accDate);
		}
	}

	/**
	 * 创建交易流水
	 * 
	 * @param drawTransaction
	 * @return
	 */
	private Transaction createTransaction(DrawTransaction drawTransaction) {
		Transaction transaction = Transaction.getInstance();
		transaction.setAccTranNo(drawTransaction.getDrawId());
		transaction.setCcyCode(drawTransaction.getCcyCode());
		transaction.setAppTranDate(new Date());
		transaction.setIpsBillNo(null);
		transaction.setAppTranNo(drawTransaction.getAppTranNo());
		transaction.setTranType(drawTransaction.getTranType());
		transaction.setSysCode(drawTransaction.getSysCode());
		transaction.setCusTranNo(null);
		transaction.setTranRemark(drawTransaction.getRemark());
		transaction.setCreateTime(DateUtil.getCurrentTimeStamp());
		transaction.setCreateBy(drawTransaction.getModifyBy());
		transaction.setTranAmt(drawTransaction.getTranAmount());
		transaction.setOrdAmt(drawTransaction.getTranAmount());
		transaction.setFeeAmt(new BigDecimal(0));
		transaction.setSecurityDeposit(new BigDecimal(0));
		transaction.setBankCost(new BigDecimal(0));
		transaction.setPayerCode(drawTransaction.getMerCode());
		transaction.setPayerAccCode(drawTransaction.getMerAccCode());
		transaction.setPayerType(drawTransaction.getMerBizKind());
		transaction.setModifyBy(drawTransaction.getModifyBy());
		transaction.setModifyTime(transaction.getModifyBy());
		transaction.setBankAccCode(drawTransaction.getIpsAccount());
		transaction.setSupplierCode(drawTransaction.getPayerBankCode());
		transaction.setPrdCode(drawTransaction.getPrdCode());
		return transaction;
	}

	@Override
	@Transactional
	public String protocolTransfer(ProtocolDrawRequest request) {
		if (!DrawConstants.TRANSFER_TYPE_ALL.equals(request.getTransferType())) {
			return null;
		}
		List<MerAccount> merAccountList = selectAllProtocolDrawAccounts();
		String batchNo = trnTransferDao.getBatchNo();
		for (MerAccount merAccount : merAccountList) {
			TransferTransaction transferTransaction = TransferTransaction
					.getInstance();
			transferTransaction.setAccDate(new Date());
			transferTransaction.setMerCode(merAccount.getMerCode());
			transferTransaction.setBatchNo(batchNo);
			MerMerchant merMerchant = basBizService.getMerMerchant(merAccount
					.getMerCode());
			if (null != merMerchant) {
				transferTransaction.setMerName(merMerchant.getMerName());
			}
			transferTransaction.setMerAccCode(merAccount.getAcctCode());
			transferTransaction
					.setStatus(DrawConstants.TRANSFER_STATUS_DEALING);
			transferTransaction.setCcyCode(merAccount.getCurrency());
			transferTransaction.setCreateBy(DrawConstants.DEFAULT_OPERATOR);
			transferTransaction.setCreateTime(DateUtil.getCurrentTimeStamp());
			transferTransaction.setModifyBy(transferTransaction.getCreateBy());
			transferTransaction.setModifyTime(transferTransaction
					.getCreateTime());
			transferTransaction.save();
		}
		return batchNo;
	}

	@Override
	public List<TrnTransfer> selectProtocolTransfer(TrnTransfer trnTransferQuery) {
		return trnTransferDao.select(trnTransferQuery);
	}

	/***
	 * 更新入账状态
	 * 
	 * @param trnTransfer
	 */
	private void updateCusAccountedStatus(TrnTransfer trnTransfer) {
		CusAccountedStatus cusAccountedStatus = new CusAccountedStatus();
		cusAccountedStatus.setMerAccCode(trnTransfer.getMerAccCode());
		cusAccountedStatus.setInAccStatus(DrawConstants.IN_ACC_STATUS_CAN);
		cusAccountedStatus.setModifyTime(DateUtil.getCurrentTimeStamp());
		cusAccountedStatus.setModifyBy(DrawConstants.DEFAULT_OPERATOR);
		cusAccountedStatusDao.updateByPrimaryKeySelective(cusAccountedStatus);
	}

	/**
	 * 创建出款交易
	 * 
	 * @param accDate
	 * @param merAccount
	 * @param drawAmt
	 * @param merMerchant
	 * @return
	 */
	private DrawTransaction createDrawTransaction(Date accDate,
			MerAccount merAccount, BigDecimal drawAmt, MerMerchant merMerchant) {
		DrawTransaction drawTransaction = DrawTransaction.getInstance();
		drawTransaction.setAppBatchNo(null);
		drawTransaction.setAppTranNo(null);
		drawTransaction.setSupplierBillNo(null);
		drawTransaction.setPrdCode(DrawConstants.ISSUED_PROTOCOL_DRAW);
		drawTransaction.setMerCode(merAccount.getMerCode());
		if (null != merMerchant) {
			drawTransaction.setMerName(merMerchant.getMerName());
		}
		drawTransaction.setMerAccCode(merAccount.getAcctCode());
		drawTransaction.setMerAccName(merAccount.getAcctName());
		drawTransaction.setMerBizKind(DrawConstants.BIZ_KIND_CUS);
		drawTransaction.setCcyCode(merAccount.getCurrency());
		drawTransaction.setTranAmount(null);
		drawTransaction.setChannelCode(null);
		drawTransaction.setChannelName(null);
		drawTransaction.setPayeeBankCode(merAccount.getBankCode());
		drawTransaction.setPayeeBraBankCode(merAccount.getBranchCode());
		drawTransaction.setPayeeBraBankName(merAccount.getBranchName());
		drawTransaction.setPayerBankCode(null);
		drawTransaction.setPayerBraBankCode(null);
		drawTransaction.setPayerBraBankName(null);
		drawTransaction.setIpsAccount(null);
		drawTransaction.setPayeeBankAccCode(merAccount.getBankAcctNo());
		drawTransaction.setPayeeBankAccName(merAccount.getBankAcctName());
		drawTransaction.setIsOnline(DrawConstants.IS_ONLINE_TRUE);
		drawTransaction.setErrorMessage(null);
		// 1204 协议出款
		drawTransaction.setTranType(DrawConstants.TRANTYPE_PROTOCOL_DRAW);
		drawTransaction.setFileId(null);
		// 出款状态 00 待处理
		drawTransaction.setStatus("00");
		drawTransaction.setRemark(null);
		drawTransaction.setPayeeBankAccType(merAccount.getStlType());
		// 01协议出款
		drawTransaction.setBizType(DrawConstants.BIZTYPE_PROTOCOLDRAW);
		drawTransaction.setPriNo(String.valueOf(merAccount
				.getTransferPriority()));
		drawTransaction.setTranDate(accDate);
		drawTransaction.setTabTime(null);
		drawTransaction.setSysCode(DrawConstants.DEFAULT_SYSCODE);
		drawTransaction.setFinishFlag("0");
		drawTransaction.setFinishNo(null);
		drawTransaction.setFinishTime(null);
		drawTransaction.setCreateTime(DateUtil.getCurrentTimeStamp());
		drawTransaction.setCreateBy(DrawConstants.DEFAULT_OPERATOR);
		drawTransaction.setModifyTime(drawTransaction.getCreateTime());
		drawTransaction.setModifyBy(drawTransaction.getCreateBy());
		String drawId = drawTransaction.getDrawTransactionId();
		drawTransaction.setTranAmount(drawAmt);
		drawTransaction.setDrawId(drawId);
		drawTransaction.setAppTranNo(drawId);
		drawTransaction.setTranDate(new Date());
		if (drawTransaction.getPayeeBankAccType() != null) {
			if (drawTransaction.getPayeeBankAccType().equals(
					BasConstants.ACC_STL_TYPE_PUBLICK)) {
				drawTransaction
						.setPayeeBankAccType(BasConstants.STL_TYPE_PUBLICK);
			} else if (drawTransaction.getPayeeBankAccType().equals(
					BasConstants.ACC_STL_TYPE_PRIVATE)) {
				drawTransaction
						.setPayeeBankAccType(BasConstants.STL_TYPE_PRIVATE);
			}
		}
		return drawTransaction;
	}

	@Override
	@Transactional
	public void update(TrnTransfer trnTransfer) {
		trnTransfer.setModifyTime(DateUtil.getCurrentTimeStamp());
		trnTransferDao.updateByPrimaryKeySelective(trnTransfer);
	}

	@Override
	public String selectTransferBatchNo(TrnTransfer trnTransferQuery) {
		return trnTransferDao.selectBatchNo(trnTransferQuery);
	}
}