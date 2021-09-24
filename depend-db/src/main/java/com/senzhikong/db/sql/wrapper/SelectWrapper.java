package com.senzhikong.db.sql.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SelectWrapper extends Wrapper  {
    private String asName;
    private SelectType selectType;
}
