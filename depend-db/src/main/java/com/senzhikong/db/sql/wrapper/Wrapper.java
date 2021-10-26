package com.senzhikong.db.sql.wrapper;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shu.zhou
 * @date 2018年9月24日下午5:00:37
 */
@Data
public class Wrapper {
    protected Class<? extends Serializable> genericsClass;
    private WrapperType type;
    private List<WrapperValue> valueList = new ArrayList<>();
    private Function function;
    private String functionName;

    public static Wrapper functionWrapper(Function function, String functionName, WrapperValue... values) {
        Wrapper wrapper = new Wrapper();
        wrapper.setType(WrapperType.FUNCTION);
        wrapper.setFunction(function);
        wrapper.setFunctionName(functionName);
        wrapper.setValueList(Arrays.asList(values));
        return wrapper;
    }
}
