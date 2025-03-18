package com.senzhikong.excel;

import lombok.Data;

/**
 * @author shu.zhou
 */
@Data
public class MergeData {

    private String name;
    private int startRow;
    private int endRow;
    private int startCol;
    private int endCol;
}
