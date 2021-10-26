package com.senzhikong.db.sql.wrapper;

import lombok.Data;

import java.io.Serializable;

@Data
public class WrapperValue {
    private Class<? extends Serializable> functionClass;
    private ValueType type;
    private ObjectFunction<? extends Serializable, ? extends Serializable> function;
    private Object value;
    private String column;
    private Wrapper wrapper;

    public static <S extends Serializable, R extends Serializable>WrapperValue from(ObjectFunction<S,R> function) {
        WrapperValue wrapperValue = new WrapperValue();
        wrapperValue.setType(ValueType.FUNCTION);
        wrapperValue.setFunction(function);
        return wrapperValue;
    }


    public static WrapperValue fromColumn(String column) {
        WrapperValue wrapperValue = new WrapperValue();
        wrapperValue.setType(ValueType.COLUMN);
        wrapperValue.setColumn(column);
        return wrapperValue;
    }

    public static WrapperValue fromValue(Object value) {
        WrapperValue wrapperValue = new WrapperValue();
        wrapperValue.setType(ValueType.VALUE);
        wrapperValue.setValue(value);
        return wrapperValue;
    }

    public static <T extends Serializable> WrapperValue from(Wrapper  wrapper) {
        WrapperValue wrapperValue = new WrapperValue();
        wrapperValue.setType(ValueType.WRAPPER);
        wrapperValue.setWrapper(wrapper);
        return wrapperValue;
    }
}
