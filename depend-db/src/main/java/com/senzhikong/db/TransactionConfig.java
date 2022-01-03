package com.senzhikong.db;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

public class TransactionConfig {
    @Resource
    EntityManagerFactory entityManagerFactory;

    @Bean("transactionManager")
    public TransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean("jpaTxAdvice")
    public TransactionInterceptor transactionInterceptor(TransactionManager transactionManager) {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        //非只读事务
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //只读事务
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        Map<String, TransactionAttribute> map = new HashMap<>();

        map.put("add*", requiredTx);
        map.put("save*", requiredTx);
        map.put("insert*", requiredTx);
        map.put("update*", requiredTx);
        map.put("delete*", requiredTx);
        map.put("create*", requiredTx);
        map.put("remove*", requiredTx);

        map.put("select*", readOnlyTx);
        map.put("get*", readOnlyTx);
        map.put("find*", readOnlyTx);
        map.put("list*", readOnlyTx);
        source.setNameMap(map);
        return new TransactionInterceptor(transactionManager, source);
    }


    /**
     * 向容器中注入切入点
     */
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(TransactionInterceptor jpaTxAdvice) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(jpaTxAdvice);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.senzhikong..*.service.impl..*.*(..))");
        advisor.setPointcut(pointcut);
        return advisor;
    }
}
