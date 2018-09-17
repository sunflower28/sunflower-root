package com.sunflower.framework.tx;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

@Configuration
public class TxConfiguration {
    private static final String AOP_POINTCUT_EXPRESSION = "(execution(public * com.sunflower.*.svcimpl.*Impl.*(..)))or(execution(public * com.sunflower.*.service.impl.*Impl.*(..)))";
    @Autowired
    private PlatformTransactionManager transactionManager;

    public TxConfiguration() {
    }

    @Bean
    public TransactionInterceptor txAdvice() {
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(1);
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(0, Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        NameMatchTransactionAttributeSource transactionAttributeSource = new NameMatchTransactionAttributeSource();
        transactionAttributeSource.addTransactionalMethod("insert*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("submit*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("audit*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("add*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("save*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("create*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("update*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("delete*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("edit*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("bind*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("lock*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("*", readOnlyTx);
        return new TransactionInterceptor(this.transactionManager, transactionAttributeSource);
    }

    @Bean
    public AspectJExpressionPointcutAdvisor txAdvisor() {
        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setExpression("(execution(public * com.sunflower.*.svcimpl.*Impl.*(..)))or(execution(public * com.sunflower.*.service.impl.*Impl.*(..)))");
        pointcutAdvisor.setAdvice(this.txAdvice());
        pointcutAdvisor.setOrder(2);
        return pointcutAdvisor;
    }

    @Bean
    public TxCheckInterceptor txCheckInterceptor() {
        TxCheckInterceptor txCheckInterceptor = new TxCheckInterceptor();
        txCheckInterceptor.setTransactionInterceptor(this.txAdvice());
        return txCheckInterceptor;
    }

    @Bean
    public AspectJExpressionPointcutAdvisor txCheckInterceptorAdvisor() {
        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setExpression("(execution(public * com.sunflower.*.svcimpl.*Impl.*(..)))or(execution(public * com.sunflower.*.service.impl.*Impl.*(..)))");
        pointcutAdvisor.setAdvice(this.txCheckInterceptor());
        pointcutAdvisor.setOrder(1);
        return pointcutAdvisor;
    }
}

