package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shu.zhou
 * @date 2018年10月24日下午3:10:01
 */
@Getter
@Setter
public class QueryWrapper<T extends Serializable> extends ListWrapper<T> {

    List<SelectWrapper<? extends Serializable>> selects = new ArrayList<>();
    List<Wrapper<? extends Serializable>> groupBys = new ArrayList<>();
    List<OrderByWrapper<? extends Serializable>> orderBys = new ArrayList<>();
    private boolean distinct = false;

    List<JoinWrapper<? extends Serializable>> joinWrappers = new ArrayList<>();

    public <R extends Serializable> WrapperValue value(ObjectFunction<T, R> func) {
        WrapperValue wrapperValue = WrapperValue.fromFunction(func);
        wrapperValue.setFunctionClass(genericsClass);
        return wrapperValue;
    }

    public QueryWrapper(Class<T> clz) {
        super();
        this.genericsClass = clz;
        this.setType(WrapperType.AND);
    }

    protected QueryWrapper() {
        this.setType(WrapperType.AND);
    }


    public static <T extends Serializable> QueryWrapper<T> from(Class<T> clz) {
        return new QueryWrapper<>(clz);
    }

    public static <T extends Serializable> QueryWrapper<T> from() {
        return new QueryWrapper<>();
    }


    public QueryWrapper<T> select(String key) {
        return select(WrapperValue.fromColumn(key));
    }

    public <R extends Serializable> QueryWrapper<T> select(ObjectFunction<T, R> func) {
        return select(WrapperValue.fromFunction(func));
    }

    public QueryWrapper<T> select(WrapperValue wrapperValue) {
        SelectWrapper<T> selectWrapper = new SelectWrapper<>();
        selectWrapper.setSelectType(SelectType.COLUMN);
        selectWrapper.getValueList().add(wrapperValue);
        selects.add(selectWrapper);
        return this;
    }

    @SafeVarargs
    public final <R extends Serializable> QueryWrapper<T> selectFunc(Function function, ObjectFunction<T, R>... func) {
        List<WrapperValue> values = Arrays.stream(func).map(WrapperValue::fromFunction).collect(Collectors.toList());
        return selectFunc(function, null, values);
    }

    @SafeVarargs
    public final <R extends Serializable> QueryWrapper<T> selectFunc(String functionName,
            ObjectFunction<T, R>... func) {
        List<WrapperValue> values = Arrays.stream(func).map(WrapperValue::fromFunction).collect(Collectors.toList());
        return selectFunc(Function.CUSTOMIZE, functionName, values);
    }

    public final QueryWrapper<T> selectFunc(String functionName, WrapperValue... values) {
        return selectFunc(Function.CUSTOMIZE, functionName, Arrays.asList(values));
    }

    public final QueryWrapper<T> selectFunc(Function function, String functionName, List<WrapperValue> values) {
        SelectWrapper<T> selectWrapper = new SelectWrapper<>();
        selectWrapper.setSelectType(SelectType.FUNCTION);
        selectWrapper.setFunction(function);
        selectWrapper.setFunctionName(functionName);
        selectWrapper.getValueList().addAll(values);
        selects.add(selectWrapper);
        return this;
    }

    public QueryWrapper<T> distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable> JoinWrapper<S> leftJoin(
            Class<S> joinClass,
            ObjectFunction<T, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.LEFT, WrapperValue.fromFunction(func1), WrapperValue.fromFunction(func2));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable> JoinWrapper<S> rightJoin(
            Class<S> joinClass,
            ObjectFunction<T, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.RIGHT, WrapperValue.fromFunction(func1), WrapperValue.fromFunction(func2));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable> JoinWrapper<S> innerJoin(
            Class<S> joinClass,
            ObjectFunction<T, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.INNER, WrapperValue.fromFunction(func1), WrapperValue.fromFunction(func2));
    }

    public <S extends Serializable, R extends Serializable, K extends Serializable> JoinWrapper<S> outerJoin(
            Class<S> joinClass,
            ObjectFunction<T, R> func1,
            ObjectFunction<S, K> func2) {
        return join(joinClass, JoinType.OUTER, WrapperValue.fromFunction(func1), WrapperValue.fromFunction(func2));
    }


    public <S extends Serializable> JoinWrapper<S> join(Class<S> joinClass, JoinType type,
            WrapperValue wrapperValue1, WrapperValue wrapperValue2) {
        JoinWrapper<S> joinWrapper = new JoinWrapper<>(joinClass);
        joinWrapper.setJoinType(type);
        joinWrapper.on(wrapperValue1, wrapperValue2);
        joinWrappers.add(joinWrapper);
        return joinWrapper;
    }

    public <S extends Serializable, R extends Serializable> QueryWrapper<T> groupBy(ObjectFunction<S, R> func) {
        return groupBy(WrapperValue.fromFunction(func));
    }

    public QueryWrapper<T> groupBy(WrapperValue wrapperValue) {
        OrderByWrapper<T> wrapper = new OrderByWrapper<>();
        wrapper.getValueList().add(wrapperValue);
        groupBys.add(wrapper);
        return this;
    }

    public <S extends Serializable, R extends Serializable> QueryWrapper<T> desc(ObjectFunction<S, R> func) {
        return orderBy(WrapperValue.fromFunction(func), OrderByType.DESC);
    }

    public <S extends Serializable, R extends Serializable> QueryWrapper<T> asc(ObjectFunction<S, R> func) {
        return orderBy(WrapperValue.fromFunction(func), OrderByType.ASC);
    }

    public QueryWrapper<T> orderBy(WrapperValue wrapperValue, OrderByType type) {
        OrderByWrapper<T> wrapper = new OrderByWrapper<>();
        wrapper.setOrderByType(type);
        wrapper.getValueList().add(wrapperValue);
        orderBys.add(wrapper);
        return this;
    }

    public QueryWrapper<T> addOr() {
        QueryWrapper<T> or = new QueryWrapper<>();
        or.setType(WrapperType.OR);
        this.wrapperList.add(or);
        return or;
    }

    public QueryWrapper<T> addAnd() {
        QueryWrapper<T> and = new QueryWrapper<>();
        and.setType(WrapperType.AND);
        this.wrapperList.add(and);
        return and;
    }
}
