package com.somnus.smart.biz.custom.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.smart.base.domain.DiffTraninfo;
import com.somnus.smart.biz.custom.service.AddAccountService;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.LedgerDetail;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.service.BasBizService;
import com.somnus.smart.service.common.BasConstants;
import com.somnus.smart.support.common.MsgCodeList;
import com.somnus.smart.support.exceptions.BizException;
import com.somnus.smart.support.util.DateUtil;

@Service
public class AddAccountServiceImpl implements AddAccountService {

    private transient Logger		log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BasBizService 			basBizService;
    
    @Autowired
    private MessageSourceAccessor 	msa;

    @Override
    @Transactional
    public void addAccount(String errNo) throws Exception {
        if (StringUtils.isBlank(errNo)) {
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302026, new Object[] {}));
        }
        DiffTraninfo diffTraninfo = basBizService.selectDiffTraninfoByPrimaryKey(errNo);
        
        if(diffTraninfo == null){
            throw new BizException(msa.getMessage(MsgCodeList.ERROR_302027, new Object[] {}));
        }
        if(BasConstants.DIFF_TRANINFO_STATUS_YES.equals(diffTraninfo.getStatus())){
        	log.warn("账务核对差错表处理状态 1:已处理>>>> 错误流水: {}", new Object[] { diffTraninfo.getErrTranNo() });
            return;
        }
        if (BasConstants.DIFF_TRANINFO_ERR_KIND_TRAN.equals(diffTraninfo.getErrKind())) {
            Transaction transaction = Transaction.selectByAccTranNo(diffTraninfo.getErrTranNo());
            if (transaction != null) {
                Account account = Account.getInstance();
                String 	entryKey = BasConstants.ENTRY_KEY_INCOME_PRE + transaction.getBlnMode()
                        + transaction.getFeeFlag() + transaction.getFeeStlMode();
                account.addAccount(transaction, entryKey, transaction.getAccDate(), false, null);
            } else {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302028, new Object[] {diffTraninfo.getErrTranNo()}));
            }
        } else if (BasConstants.DIFF_TRANINFO_ERR_KIND_LEDGER.equals(diffTraninfo.getErrKind())) {
            LedgerDetail ledgerDetail = LedgerDetail.getInstance();
            ledgerDetail = ledgerDetail.selectByPrimaryKey(diffTraninfo.getErrTranNo());
            if (ledgerDetail != null) {
                ledgerDetail.setAccDate(basBizService.getSystemAccDate());
                Account account = Account.getInstance();
                account.addAccount(false, ledgerDetail);
            } else {
                throw new BizException(msa.getMessage(MsgCodeList.ERROR_302029, new Object[] {diffTraninfo.getErrTranNo()}));
            }
        }
        diffTraninfo.setStatus(BasConstants.DIFF_TRANINFO_STATUS_YES);
        diffTraninfo.setModifyTime(DateUtil.getCurrentTimeStamp());
        basBizService.updateDiffTraninfoByPrimaryKey(diffTraninfo);
    }
}
