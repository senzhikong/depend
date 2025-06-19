package com.senzhikong.excel;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author shu.zhou
 */
@Data
public class SheetExport {
    private String sheetName;
    private List<?> pojoList;
    private LinkedHashMap<String, String> alias;
    private SheetHeadData sheetHeadData;
    private List<MergeData> mergeDataList;
}
