package com.senzhikong.excel;

import lombok.Data;

/**
 * @author shu.zhou
 */
@Data
public class SheetHeadData {
    /**
     * 表头名称
     */
    private String tableHeadName;
    /**
     * 表头名称所在的行
     */
    private Integer tableHeadRow;
    /**
     * 字段所在的行
     */
    private Integer fieldRow;
    /**
     * 插入数据开始的row
     */
    private Integer dataStarRow;

    /**
     * 默认sheet的信息<br/>
     * tableHeadName = "export excel"<br/>
     * fieldRow = 0<br/>
     * dataStarRow = 1<br/>
     */
    SheetHeadData() {
        this.tableHeadName = "export excel";
        this.tableHeadRow = 0;
        this.fieldRow = 1;
        this.dataStarRow = 2;
    }

    /**
     * sheet的基本信息
     *
     * @param tableHeadName 表头名称
     * @param fieldRow      sheet表格对应实体字段所在的行
     */
    public SheetHeadData(String tableHeadName, int fieldRow) {
        this.tableHeadName = tableHeadName;
        this.tableHeadRow = 0;// 如果fieldRow = tableHeadRow，则没有表头
        this.fieldRow = fieldRow;
        this.dataStarRow = fieldRow + 1;
    }

    /**
     * sheet的基本信息
     *
     * @param tableHeadName 表头名称
     * @param fieldRow      sheet表格字段所在的行
     * @param dataStarRow   插入数据开始的行
     */
    public SheetHeadData(String tableHeadName, int fieldRow, int dataStarRow) {
        this.tableHeadName = tableHeadName;
        if (fieldRow > 0) {
            this.tableHeadRow = fieldRow - 1;// 如果fieldRow = tableHeadRow，则没有表头
        } else {
            this.tableHeadRow = 0;
        }

        this.fieldRow = fieldRow;
        this.dataStarRow = dataStarRow;
    }

    /**
     * sheet的基本信息
     *
     * @param tableHeadName 表头名称
     * @param tableHeadRow  表头名称所在的行
     * @param fieldRow      sheet表格字段所在的行
     * @param dataStarRow   插入数据开始的行
     */
    public SheetHeadData(String tableHeadName, int tableHeadRow, int fieldRow, int dataStarRow) {
        this.tableHeadName = tableHeadName;
        this.tableHeadRow = tableHeadRow;
        this.fieldRow = fieldRow;
        this.dataStarRow = dataStarRow;
    }
}
