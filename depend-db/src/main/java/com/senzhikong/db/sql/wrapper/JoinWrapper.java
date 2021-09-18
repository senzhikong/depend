package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JoinWrapper<T extends Serializable> extends QueryWrapper<T> {
    private JoinType joinType;
    List<Wrapper<? extends Serializable>> on = new ArrayList<>();

    public JoinWrapper(Class<T> joinClass) {
        super(joinClass);
        this.genericsClass = joinClass;
    }

    public JoinWrapper<T> on(WrapperValue wrapperValue1, WrapperValue wrapperValue2) {
        on.add(new Wrapper<>(WrapperType.EQ, wrapperValue1, wrapperValue2));
        return this;
    }
}
