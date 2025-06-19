package com.senzhikong.excel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shu.zhou
 */
@Slf4j
public class ExcelUtil {

    private static final ThreadLocal<DateFormat> THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 将excel表转换成指定类型的对象数组，列名即作为对象属性
     *
     * @param clazz 类型
     */
    public static <T> List<T> excel2Pojo(Workbook wb, Class<T> clazz) {
        LinkedHashMap<String, String> alias = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            alias.put(field.getName(), field.getName());
        }
        return excel2Pojo(wb, clazz, alias, 1);
    }

    /**
     * 将excel表转换成指定类型的对象数组
     *
     * @param clazz 类型
     * @param alias 列别名,格式要求：Map<"列名","类属性名">
     * @param param 指定第几行行为字段名(数据在字段的下一行，默认)，第一行为0
     */
    public static <T> List<T> excel2Pojo(Workbook wb, Class<T> clazz, LinkedHashMap<String, String> alias, Integer param) {
        try {
            Sheet sheet = wb.getSheetAt(0);
            //生成属性和列对应关系的map，Map<类属性名，对应一行的第几列>
            Map<String, Integer> propertyMap = generateColumnPropertyMap(sheet, alias, param);
            //根据指定的映射关系进行转换
            return generateList(sheet, propertyMap, clazz, param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * 生成一个属性-列的对应关系的map
     *
     * @param sheet 表
     * @param alias 别名
     */
    private static Map<String, Integer> generateColumnPropertyMap(Sheet sheet, LinkedHashMap<String, String> alias, Integer param) {
        Map<String, Integer> propertyMap = new HashMap<>(16);

        if (param == null || param < 0) {
            param = 1;
        }
        Row propertyRow = sheet.getRow(param);
        short firstCellNum = propertyRow.getFirstCellNum();
        short lastCellNum = propertyRow.getLastCellNum();

        for (int i = firstCellNum; i < lastCellNum; i++) {
            Cell cell = propertyRow.getCell(i);
            if (cell == null) {
                continue;
            }
            // 列名
            String cellValue = cell.getStringCellValue();
            // 对应属性名
            String propertyName = alias.get(cellValue);
            propertyMap.put(propertyName, i);
        }
        return propertyMap;
    }

    /**
     * 根据指定关系将表数据转换成对象数组
     *
     * @param sheet       表
     * @param propertyMap 属性映射关系Map<"属性名",一行第几列>
     * @param clazz       类类型
     */
    private static <T> List<T> generateList(Sheet sheet, Map<String, Integer> propertyMap, Class<T> clazz, Integer param) throws Exception {
        if (param == null || param < 0) {
            param = 1;
        }
        // 对象数组
        List<T> pojoList = new ArrayList<>();
        int index = 0;
        for (Row row : sheet) {
            // 跳过标题和列名
            if (row.getRowNum() < param + 1) {
                continue;
            }
            T instance = clazz.getDeclaredConstructor().newInstance();
            Set<Entry<String, Integer>> entrySet = propertyMap.entrySet();
            for (Entry<String, Integer> entry : entrySet) {
                if (row.getCell(entry.getValue()) == null) {
                    continue;
                }
                /*
                 * CellTypeEnum        类型        值
                 * NUMERIC             数值型      0
                 * STRING              字符串型     1
                 * FORMULA             公式型      2
                 * BLANK               空值        3
                 * BOOLEAN             布尔型      4
                 * ERROR               错误        5
                 *
                 * 4.0以上将会移除 替换为getCellType
                 * */
                // 获取此行指定列的值,即为属性对应的值
                switch (row.getCell(entry.getValue()).getCellType()) {
                    case _NONE:
                        System.out.println("****************************不知道的类型*********************************");
                        throw new Exception("第" + index + "行【" + row.getCell(entry.getValue()) + "】导入数据异常");
                    case BLANK:
                        BeanUtils.setProperty(instance, entry.getKey(), null);
                        break;
                    case NUMERIC:
                        int numericType = row.getCell(entry.getValue()).getCellStyle().getDataFormat();
                        if (numericType == 0) {
                            // 数字类型
                            int pInt = (int) row.getCell(entry.getValue()).getNumericCellValue();
                            BeanUtils.setProperty(instance, entry.getKey(), pInt);
                        } else {
                            Date date = row.getCell(entry.getValue()).getDateCellValue();
                            BeanUtils.setProperty(instance, entry.getKey(), date);
                        }
                        break;
                    case STRING:
                        String pString = row.getCell(entry.getValue()).getStringCellValue();
                        BeanUtils.setProperty(instance, entry.getKey(), pString);
                        break;
                    case FORMULA:
                        System.out.println("**该类型【FORMULA】未做处理，因为没见过这种类型，于ExcelUtil2.generateList方法中修改！");
                        break;
                    case BOOLEAN:
                        boolean pBoolean = row.getCell(entry.getValue()).getBooleanCellValue();
                        BeanUtils.setProperty(instance, entry.getKey(), pBoolean);
                        break;
                    case ERROR:
                        System.out.println("****************************error*********************************");
                        throw new Exception("第" + index + "行【" + row.getCell(entry.getValue()) + "】导入数据异常");
                    default:
                }
            }
            pojoList.add(instance);
            index++;
        }
        return pojoList;
    }


    /**
     * 将对象数组转换成excel<br/>
     *
     * @param pojoList      对象数组
     * @param out           输出流
     * @param alias         指定对象属性别名，生成列名和列顺序Map<"类属性名","列名">
     * @param sheetHeadData 表头对象
     */
    public static <T> void pojo2Excel(List<T> pojoList, OutputStream out, LinkedHashMap<String, String> alias, SheetHeadData sheetHeadData) {
        //创建一个工作簿
        if (sheetHeadData == null) {
            sheetHeadData = new SheetHeadData();
        }
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFCellStyle centerStyle = wb.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //创建一个表
            XSSFSheet sheet = wb.createSheet();
            // 需要表头
            if (sheetHeadData.getFieldRow() > sheetHeadData.getTableHeadRow()) {
                //创建第一行，作为表名
                // 这个方法感觉是直接跳到对应行的
                XSSFRow row = sheet.createRow(sheetHeadData.getTableHeadRow());
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(sheetHeadData.getTableHeadName());
                cell.setCellStyle(centerStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, alias.size() - 1));
            }
            // 在第一行插入列名
            insertColumnName(sheetHeadData.getFieldRow(), sheet, alias, centerStyle);
            // 从第指定行开始插入数据
            insertColumnDate(sheetHeadData.getDataStarRow(), pojoList, sheet, alias);
            AtomicInteger columnIndex = new AtomicInteger();
            alias.forEach((key, value) -> sheet.setColumnWidth(columnIndex.getAndAdd(1), value.length() * 512));
            // 输出表格文件
            wb.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 多个sheet导出excel 复杂表头或者非复杂表头<br/>
     *
     * @param exportList sheet对象的list
     * @param out        输出流
     */
    public static void pojo2ExcelSheetList(List<SheetExport> exportList, OutputStream out) {
        //创建一个工作簿

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            // 设置居中样式
            XSSFCellStyle centerStyle = wb.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            for (SheetExport sheetData : exportList) {
                //创建一个表
                XSSFSheet sheet = wb.createSheet(sheetData.getSheetName());
                // 需要表头
                if (sheetData.getSheetHeadData().getFieldRow() > sheetData.getSheetHeadData().getTableHeadRow()) {
                    XSSFRow row = sheet.createRow(sheetData.getSheetHeadData().getTableHeadRow());
                    // 这个方法感觉是直接跳到对应行的
                    XSSFCell cell = row.createCell(0);
                    cell.setCellValue(sheetData.getSheetHeadData().getTableHeadName());
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, sheetData.getAlias().size() - 1));

                    // 设置居中样式
                    cell.setCellStyle(centerStyle);
                }
                if (sheetData.getMergeDataList() == null) {
                    if (sheetData.getSheetHeadData().getFieldRow() < sheetData.getSheetHeadData().getDataStarRow()) {
                        // 插入列名
                        insertColumnName(sheetData.getSheetHeadData().getFieldRow(), sheet, sheetData.getAlias(), centerStyle);
                        // 从第指定行开始插入数据
                        insertColumnDate(sheetData.getSheetHeadData().getDataStarRow(), sheetData.getPojoList(), sheet, sheetData.getAlias());
                    } else {
                        insertColumnName(sheetData.getSheetHeadData().getFieldRow(), sheet, sheetData.getAlias(), centerStyle);
                        insertColumnDate(sheetData.getSheetHeadData().getFieldRow() + 1, sheetData.getPojoList(), sheet, sheetData.getAlias());
                    }
                } else {
                    // 插入复杂表头(表的标题和字段名之间)
                    XSSFRow rowTable = sheet.createRow(sheetData.getSheetHeadData().getTableHeadRow() + 1);
                    for (MergeData mergeData : sheetData.getMergeDataList()) {
                        sheet.addMergedRegion(new CellRangeAddress(mergeData.getStartRow(), mergeData.getEndRow(), mergeData.getStartCol(), mergeData.getEndCol()));
                        // 插入复杂表头的数据
                        XSSFCell tableCellValue = rowTable.createCell(mergeData.getStartCol());
                        tableCellValue.setCellValue(mergeData.getName());
                        // 这里可以对单元格做样式处理
                        // 设置居中样式
                        tableCellValue.setCellStyle(centerStyle);
                    }
                    // 如果插入数据的行小于指定的数据行，就默认在复杂表头的下方
                    int maxHeadRow = 0;
                    for (MergeData me : sheetData.getMergeDataList()) {
                        if (me.getEndRow() > maxHeadRow) {
                            maxHeadRow = me.getEndRow();
                        }
                    }
                    if (maxHeadRow < sheetData.getSheetHeadData().getFieldRow() && sheetData.getSheetHeadData().getFieldRow() < sheetData.getSheetHeadData().getDataStarRow()) {
                        // 插入列名
                        insertColumnName(sheetData.getSheetHeadData().getFieldRow(), sheet, sheetData.getAlias(), centerStyle);
                        // 从第指定行开始插入数据
                        insertColumnDate(sheetData.getSheetHeadData().getDataStarRow(), sheetData.getPojoList(), sheet, sheetData.getAlias());
                    } else {
                        insertColumnName(maxHeadRow + 1, sheet, sheetData.getAlias(), centerStyle);
                        insertColumnDate(maxHeadRow + 2, sheetData.getPojoList(), sheet, sheetData.getAlias());
                    }
                }
                AtomicInteger columnIndex = new AtomicInteger();
                sheetData.getAlias().forEach((key, value) -> sheet.setColumnWidth(columnIndex.getAndAdd(1), value.length() * 512));
            }
            // 输出表格文件
            wb.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 此方法作用是创建表头的列名
     *
     * @param alias  要创建的表的列名与实体类的属性名的映射集合
     * @param rowNum 指定行创建列名
     */
    private static void insertColumnName(int rowNum, XSSFSheet sheet, Map<String, String> alias, XSSFCellStyle cellStyle) {
        XSSFRow row = sheet.createRow(rowNum);
        //列的数量
        int columnCount = 0;

        Set<Entry<String, String>> entrySet = alias.entrySet();

        for (Entry<String, String> entry : entrySet) {
            // 创建第一行的第columnCount个格子
            XSSFCell cell = row.createCell(columnCount++);
            // 将此格子的值设置为alias中的键名
            cell.setCellValue(isNull(entry.getValue()).toString().trim());
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
            }
        }
    }

    /**
     * 从指定行开始插入数据
     *
     * @param beginRowNum 开始行
     * @param models      对象数组
     * @param sheet       表
     * @param alias       列别名
     */
    private static <T> void insertColumnDate(int beginRowNum, List<T> models, XSSFSheet sheet, Map<String, String> alias) {
        for (T model : models) {
            // 创建新的一行
            XSSFRow rowTemp = sheet.createRow(beginRowNum++);
            // 获取列的迭代
            Set<Entry<String, String>> entrySet = alias.entrySet();
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(model, SerializerFeature.WriteDateUseDateFormat));
            // 从第0个格子开始创建
            int columnNum = 0;
            for (Entry<String, String> entry : entrySet) {
                // 获取属性值
                Object property = object.get(entry.getKey());
                if (property == null) {
                    continue;
                }
                // 创建一个格子
                XSSFCell cell = rowTemp.createCell(columnNum++);
                if (property instanceof Double) {
                    cell.setCellValue(object.getDoubleValue(entry.getKey()));
                } else if (property instanceof Integer) {
                    cell.setCellValue(object.getInteger(entry.getKey()));
                } else if (property instanceof Boolean) {
                    cell.setCellValue(object.getBoolean(entry.getKey()));
                } else if (property instanceof Date) {
                    cell.setCellType(CellType.STRING);
                    String date = THREAD_LOCAL.get().format(object.getDate(entry.getKey()));
                    cell.setCellValue(date.replace(" 00:00:00", ""));
                } else {
                    cell.setCellType(CellType.STRING);
                    String value = String.valueOf(property);
                    if (isDateAndTime(value)) {
                        value = value.replace(" 00:00:00", "");
                    }
                    cell.setCellValue(value);
                }
            }
        }
    }

    /**
     * 判断是否为空，若为空设为""
     */
    private static Object isNull(Object object) {
        return object == null ? "" : object;
    }

    private static boolean isDateAndTime(String str) {
        return str.matches("^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\131)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\229)\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    }
}

