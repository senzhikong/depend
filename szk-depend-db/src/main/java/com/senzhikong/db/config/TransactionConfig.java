package com.senzhikong.db.config;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author shu
 */
public class TransactionConfig {
    /**
     *
     */
    private String expression = "execution(* com.demo..*.service.impl..*.*(..))";

    public TransactionConfig(String expression) {
        if (StringUtils.isBlank(expression)) {
            throw new RuntimeException("数据库事务表达式不能为空：例：" + this.expression);
        }
        this.expression = expression;
    }

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
        Map<String, TransactionAttribute> map = new HashMap<>(8);

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
        pointcut.setExpression(expression);
        advisor.setPointcut(pointcut);
        return advisor;
    }
}
