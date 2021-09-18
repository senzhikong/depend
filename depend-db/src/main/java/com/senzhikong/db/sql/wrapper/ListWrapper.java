package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class ListWrapper<T extends Serializable> extends Wrapper<T> {
    protected List<Wrapper<? extends Serializable>> wrapperList=new ArrayList<>();

    private ListWrapper<T> add(WrapperType type, Function function, String functionName,
            WrapperValue... values) {
        List<WrapperValue> valueList = Arrays.asList(values);
        Wrapper<T> wrapper = new Wrapper<>();
        wrapper.setType(type);
        wrapper.setFunction(function);
        wrapper.setFunctionName(functionName);
        wrapper.setValueList(valueList);
        this.wrapperList.add(wrapper);
        return this;
    }


    public <S extends Serializable,R extends Serializable> ListWrapper<T> eq(ObjectFunction<S, R> func1,
            Object value) {
        return add(WrapperType.EQ, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> ne(ObjectFunction<S, R> func1, Object value) {
        return add(WrapperType.NOT_EQ, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> gt(ObjectFunction<S, R> func1, Object value) {
        return add(WrapperType.GT, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> lt(ObjectFunction<S, R> func1, Object value) {
        return add(WrapperType.LT, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> ge(ObjectFunction<S, R> func1, Object value) {
        return add(WrapperType.GE, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> le(ObjectFunction<S, R> func1, Object value) {
        return add(WrapperType.LE, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> in(ObjectFunction<S, R> func1, Iterable<T> value) {
        return add(WrapperType.IN, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> in(ObjectFunction<S, R> func1, Object[] value) {
        return add(WrapperType.IN, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> not_in(ObjectFunction<S, R> func1, Collection<T> value) {
        return add(WrapperType.NOT_IN, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> not_in(ObjectFunction<S, R> func1, Object[] value) {
        return add(WrapperType.NOT_IN, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> start(ObjectFunction<S, R> func1, String value) {
        return add(WrapperType.LIKE, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue(value + "%"));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> end(ObjectFunction<S, R> func1, String value) {
        return add(WrapperType.LIKE, null, null, WrapperValue.fromFunction(func1), WrapperValue.fromValue("%" + value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> like(ObjectFunction<S, R> func1, String value) {
        return add(WrapperType.LIKE, null, null, WrapperValue.fromFunction(func1),
                WrapperValue.fromValue("%" + value + "%"));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> is_null(ObjectFunction<S, R> func1) {
        return add(WrapperType.IS_NULL, null, null, WrapperValue.fromFunction(func1));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> not_null(ObjectFunction<S, R> func1) {
        return add(WrapperType.NOT_NULL, null, null, WrapperValue.fromFunction(func1));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> or() {
        ListWrapper<T> or = new ListWrapper<>();
        or.setType(WrapperType.OR);
        this.wrapperList.add(or);
        return or;
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> jsonArrayContains(ObjectFunction<S, R> func1, Object value) {
        return add(WrapperType.IS_NULL, Function.CUSTOMIZE, "JSON_CONTAINS", WrapperValue.fromFunction(func1),
                WrapperValue.fromValue(value));
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> and() {
        ListWrapper<T> and = new ListWrapper<>();
        and.setType(WrapperType.AND);
        this.wrapperList.add(and);
        return and;
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> or(ListWrapper<T> listWrapper) {
        listWrapper.setType(WrapperType.OR);
        this.wrapperList.add(listWrapper);
        return this;
    }

    public <S extends Serializable,R extends Serializable>ListWrapper<T> and(ListWrapper<T> listWrapper) {
        listWrapper.setType(WrapperType.AND);
        this.wrapperList.add(listWrapper);
        return this;
    }


}
