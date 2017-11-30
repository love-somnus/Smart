package com.somnus.smart.domain;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.somnus.smart.domain.account.AccDetail;
import com.somnus.smart.domain.account.AccEntryCfg;
import com.somnus.smart.domain.account.Account;
import com.somnus.smart.domain.account.DrawTransaction;
import com.somnus.smart.domain.account.FreezeTransaction;
import com.somnus.smart.domain.account.LedgerDetail;
import com.somnus.smart.domain.account.RefundTransaction;
import com.somnus.smart.domain.account.SubAccInfo;
import com.somnus.smart.domain.account.SubAccInfoLock;
import com.somnus.smart.domain.account.TranDraw;
import com.somnus.smart.domain.account.TranFee;
import com.somnus.smart.domain.account.TranIssued;
import com.somnus.smart.domain.account.TranRefund;
import com.somnus.smart.domain.account.TranRefuse;
import com.somnus.smart.domain.account.TranReverse;
import com.somnus.smart.domain.account.TranVoucher;
import com.somnus.smart.domain.account.Transaction;
import com.somnus.smart.domain.account.TransferTransaction;


/**
 * 领域对象初始化
 */
public class DomainInitializer implements InitializingBean, ApplicationContextAware {

    private ApplicationContext  applicationContext;

    /**
     * 这个方法将在所有的属性被初始化后调用。 但是会在init前调用。 但是主要的是如果是延迟加载的话，则马上执行。 所以可以在类上加上注解： import
     * org.springframework.context.annotation.Lazy;
     * 
     * @Lazy(false) 这样spring容器初始化的时候afterPropertiesSet就会被调用。 只需要实现InitializingBean接口就行。
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Account.init(applicationContext);
        Transaction.init(applicationContext);
        LedgerDetail.init(applicationContext);
        AccDetail.init(applicationContext);
        SubAccInfo.init(applicationContext);
        AccEntryCfg.init(applicationContext);
        SubAccInfoLock.init(applicationContext);
        TranReverse.init(applicationContext);
        TranDraw.init(applicationContext);
        DrawTransaction.init(applicationContext);
        FreezeTransaction.init(applicationContext);
        RefundTransaction.init(applicationContext);
        TranFee.init(applicationContext);
        TranRefuse.init(applicationContext);
        TranRefund.init(applicationContext);
        TranVoucher.init(applicationContext);
        TranIssued.init(applicationContext);
        TransferTransaction.init(applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
