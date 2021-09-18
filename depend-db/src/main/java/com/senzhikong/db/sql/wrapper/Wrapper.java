package com.senzhikong.db.sql.wrapper;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shu.zhou
 * @date 2018年9月24日下午5:00:37
 */
@Data
public class Wrapper<T extends Serializable> {
    protected Class<T> genericsClass;
    private WrapperType type;
    private List<WrapperValue> valueList=new ArrayList<>();
    private Function function;
    private String functionName;

    protected Wrapper(WrapperType type, WrapperValue wrapperValue1, WrapperValue wrapperValue2) {
        super();
        valueList.add(wrapperValue1);
        valueList.add(wrapperValue2);
        this.type = type;
    }

    protected Wrapper() {

    }


}
