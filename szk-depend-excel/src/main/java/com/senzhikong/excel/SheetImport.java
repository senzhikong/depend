package com.senzhikong.excel;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author shu.zhou
 */
@Data
public class SheetImport {

    private Class<?> clazz;
    private LinkedHashMap<String, String> alias;
    private Integer param;
}
