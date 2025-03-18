package com.senzhikong.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author shu.zhou
 */
@Slf4j
public class ExcelUtil {

    /**
     * 将对象数组转换成excel<br/>
     *
     * @param pojoList  对象数组
     * @param out       输出流
     * @param alias     指定对象属性别名，生成列名和列顺序Map<"类属性名","列名">
     * @param utilExcel 表头对象
     * @throws Exception 异常
     */
    public static <T> void pojo2Excel(List<T> pojoList, OutputStream out, LinkedHashMap<String, String> alias, UtilExcel utilExcel) throws Exception {
        //创建一个工作簿
        if (utilExcel == null) {
            utilExcel = new UtilExcel();
        }
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            //创建一个表
            XSSFSheet sheet = wb.createSheet();
            // 需要表头
            if (utilExcel.getFieldRow() > utilExcel.getTableHeadRow()) {
                //创建第一行，作为表名
                // 这个方法感觉是直接跳到对应行的
                XSSFRow row = sheet.createRow(utilExcel.getTableHeadRow());
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(utilExcel.getTableHeadName());

                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, alias.size() - 1));
            }
            // 在第一行插入列名
            insertColumnName(utilExcel.getFieldRow(), sheet, alias);
            // 从第指定行开始插入数据
            insertColumnDate(utilExcel.getDataStarRow(), pojoList, sheet, alias);
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
     * @throws Exception 异常
     */
    public static void pojo2ExcelSheetList(List<SheetExport> exportList, OutputStream out) throws Exception {
        //创建一个工作簿

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            // 设置居中样式
            XSSFCellStyle xssStyle = wb.createCellStyle();
            xssStyle.setAlignment(HorizontalAlignment.CENTER);
            xssStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            for (SheetExport sheetData : exportList) {
                //创建一个表
                XSSFSheet sheet = wb.createSheet(sheetData.getSheetName());
                // 需要表头
                if (sheetData.getUtilExcel().getFieldRow() > sheetData.getUtilExcel().getTableHeadRow()) {
                    XSSFRow row = sheet.createRow(sheetData.getUtilExcel().getTableHeadRow());
                    // 这个方法感觉是直接跳到对应行的
                    XSSFCell cell = row.createCell(0);
                    cell.setCellValue(sheetData.getUtilExcel().getTableHeadName());
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, sheetData.getAlias().size() - 1));

                    // 设置居中样式
                    cell.setCellStyle(xssStyle);
                }
                if (sheetData.getMergeDataList() == null) {
                    if (sheetData.getUtilExcel().getFieldRow() < sheetData.getUtilExcel().getDataStarRow()) {
                        // 插入列名
                        insertColumnName(sheetData.getUtilExcel().getFieldRow(), sheet, sheetData.getAlias());
                        // 从第指定行开始插入数据
                        insertColumnDate(sheetData.getUtilExcel().getDataStarRow(), sheetData.getPojoList(), sheet, sheetData.getAlias());
                    } else {
                        insertColumnName(sheetData.getUtilExcel().getFieldRow(), sheet, sheetData.getAlias());
                        insertColumnDate(sheetData.getUtilExcel().getFieldRow() + 1, sheetData.getPojoList(), sheet, sheetData.getAlias());
                    }
                } else {
                    // 插入复杂表头(表的标题和字段名之间)
                    XSSFRow rowTable = sheet.createRow(sheetData.getUtilExcel().getTableHeadRow() + 1);
                    for (MergeData mergeData : sheetData.getMergeDataList()) {
                        sheet.addMergedRegion(new CellRangeAddress(mergeData.getStartRow(), mergeData.getEndRow(), mergeData.getStartCol(), mergeData.getEndCol()));
                        // 插入复杂表头的数据
                        XSSFCell tableCellValue = rowTable.createCell(mergeData.getStartCol());
                        tableCellValue.setCellValue(mergeData.getName());
                        // 这里可以对单元格做样式处理
                        // 设置居中样式
                        tableCellValue.setCellStyle(xssStyle);
                    }
                    // 如果插入数据的行小于指定的数据行，就默认在复杂表头的下方
                    int maxHeadRow = 0;
                    for (MergeData me : sheetData.getMergeDataList()) {
                        if (me.getEndRow() > maxHeadRow) {
                            maxHeadRow = me.getEndRow();
                        }
                    }
                    if (maxHeadRow < sheetData.getUtilExcel().getFieldRow() && sheetData.getUtilExcel().getFieldRow() < sheetData.getUtilExcel().getDataStarRow()) {
                        // 插入列名
                        insertColumnName(sheetData.getUtilExcel().getFieldRow(), sheet, sheetData.getAlias());
                        // 从第指定行开始插入数据
                        insertColumnDate(sheetData.getUtilExcel().getDataStarRow(), sheetData.getPojoList(), sheet, sheetData.getAlias());
                    } else {
                        insertColumnName(maxHeadRow + 1, sheet, sheetData.getAlias());
                        insertColumnDate(maxHeadRow + 2, sheetData.getPojoList(), sheet, sheetData.getAlias());
                    }
                }
            }
            // 输出表格文件
            wb.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将对象数组转换成excel，并增加复杂表头，除去表名和显示列的名称的那一行
     *
     * @param pojoList      对象数组
     * @param out           输出流
     * @param alias         指定对象属性别名，生成列名和列顺序Map<"类属性名","列名">
     * @param utilExcel     表头对象
     * @param mergeDataList 合并行中的所有数据，
     */
    public static <T> void pojo2Excel(List<T> pojoList, OutputStream out, LinkedHashMap<String, String> alias, UtilExcel utilExcel, List<MergeData> mergeDataList) throws Exception {
        if (utilExcel == null) {
            throw new Exception("UtilExcel 对象为空");
        }
        //创建一个工作簿
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFCellStyle xssStyle = wb.createCellStyle();
            xssStyle.setAlignment(HorizontalAlignment.CENTER);
            xssStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            //创建一个表
            XSSFSheet sheet = wb.createSheet();
            // 需要表头
            if (utilExcel.getFieldRow() > utilExcel.getTableHeadRow()) {
                //创建第一行，作为表名
                XSSFRow row = sheet.createRow(utilExcel.getTableHeadRow());
                // 这个方法感觉是直接跳到对应行的 后面不需要再次调用该方法，应该是使用该方法 可以独立设置该行的样式
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(utilExcel.getTableHeadName());
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, alias.size() - 1));

                // 设置居中样式
                cell.setCellStyle(xssStyle);
            }

            // 插入复杂表头
            XSSFRow rowTable = sheet.createRow(utilExcel.getTableHeadRow() + 1);
            for (MergeData mergeData : mergeDataList) {
                sheet.addMergedRegion(new CellRangeAddress(mergeData.getStartRow(), mergeData.getEndRow(), mergeData.getStartCol(), mergeData.getEndCol()));
                // 插入数据
                XSSFCell tableCellValue = rowTable.createCell(mergeData.getStartCol());
                tableCellValue.setCellValue(mergeData.getName());

                // 这里可以对单元格做样式处理
                // 设置居中样式
                tableCellValue.setCellStyle(xssStyle);
            }

            // 如果插入数据的行小于指定的数据行，就默认在复杂表头的下方
            int maxHeadRow = 0;
            for (MergeData me : mergeDataList) {
                if (me.getEndRow() > maxHeadRow) {
                    maxHeadRow = me.getEndRow();
                }
            }
            if (maxHeadRow < utilExcel.getFieldRow() && utilExcel.getFieldRow() < utilExcel.getDataStarRow()) {
                // 插入列名
                insertColumnName(utilExcel.getFieldRow(), sheet, alias);
                // 从第指定行开始插入数据
                insertColumnDate(utilExcel.getDataStarRow(), pojoList, sheet, alias);
            } else {
                insertColumnName(maxHeadRow + 1, sheet, alias);
                insertColumnDate(maxHeadRow + 2, pojoList, sheet, alias);
            }
            // 输出表格文件
            wb.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将excel表转换成指定类型的对象数组
     *
     * @param clazz 类型
     * @param alias 列别名,格式要求：Map<"列名","类属性名">
     * @param param 指定第几行行为字段名(数据在字段的下一行，默认)，第一行为0
     */
    public static <T> List<T> excel2Pojo(InputStream inputStream, Class<T> clazz, LinkedHashMap<String, String> alias, Integer param) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = wb.getSheetAt(0);
            //生成属性和列对应关系的map，Map<类属性名，对应一行的第几列>
            Map<String, Integer> propertyMap = generateColumnPropertyMap(sheet, alias, param);
            //根据指定的映射关系进行转换
            return generateList(sheet, propertyMap, clazz, param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> List<T> excel2PojoSheetList(List<SheetImport> list, InputStream inputStream) throws IOException {

        List<T> pojoList = new ArrayList<>();
        try (XSSFWorkbook wb = new XSSFWorkbook(inputStream)) {
            for (int i = 0; i < list.size(); i++) {
                XSSFSheet sheet = wb.getSheetAt(i);
                //生成属性和列对应关系的map，Map<类属性名，对应一行的第几列>
                Map<String, Integer> propertyMap = generateColumnPropertyMap(sheet, list.get(i).getAlias(), list.get(i).getParam());
                //根据指定的映射关系进行转换
                pojoList.add((T) generateList(sheet, propertyMap, list.get(i).getClazz(), list.get(i).getParam()));

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return pojoList;
    }

    /**
     * 将对象数组转换成excel
     *
     * @param pojoList 对象数组
     * @param out      输出流
     * @param alias    指定对象属性别名，生成列名和列顺序
     */
    public static <T> void pojo2Excel(List<T> pojoList, OutputStream out, LinkedHashMap<String, String> alias) throws Exception {
        //获取类名作为标题
        String headLine;
        if (!pojoList.isEmpty()) {
            Object pojo = pojoList.get(0);
            Class<?> clazz = pojo.getClass();
            headLine = clazz.getName();
            pojo2Excel(pojoList, out, alias, new UtilExcel(headLine, 1));
        }
    }

    /**
     * 将对象数组转换成excel,列名为对象属性名
     *
     * @param pojoList 对象数组
     * @param out      输出流
     * @param headLine 表标题
     */
    public static <T> void pojo2Excel(List<T> pojoList, OutputStream out, String headLine) throws Exception {
        //获取类的属性作为列名
        LinkedHashMap<String, String> alias = new LinkedHashMap<>();
        if (!pojoList.isEmpty()) {
            Object pojo = pojoList.get(0);
            Field[] fields = pojo.getClass().getDeclaredFields();
            String[] name = new String[fields.length];
            Field.setAccessible(fields, true);
            for (int i = 0; i < name.length; i++) {
                name[i] = fields[i].getName();
                alias.put(isNull(name[i]).toString(), isNull(name[i]).toString());
            }
            pojo2Excel(pojoList, out, alias, new UtilExcel(headLine, 1));
        }
    }

    /**
     * 将对象数组转换成excel，列名默认为对象属性名，标题为类名
     *
     * @param pojoList 对象数组
     * @param out      输出流
     */
    public static <T> void pojo2Excel(List<T> pojoList, OutputStream out) throws Exception {
        //获取类的属性作为列名
        LinkedHashMap<String, String> alias = new LinkedHashMap<>();
        //获取类名作为标题
        String headLine;
        if (!pojoList.isEmpty()) {
            Object pojo = pojoList.get(0);
            Class<?> clazz = pojo.getClass();
            headLine = clazz.getName();
            Field[] fields = clazz.getDeclaredFields();
            String[] name = new String[fields.length];
            Field.setAccessible(fields, true);
            for (int i = 0; i < name.length; i++) {
                name[i] = fields[i].getName();
                alias.put(isNull(name[i]).toString(), isNull(name[i]).toString());
            }
            pojo2Excel(pojoList, out, alias, new UtilExcel(headLine, 1));
        }
    }

    /**
     * 此方法作用是创建表头的列名
     *
     * @param alias  要创建的表的列名与实体类的属性名的映射集合
     * @param rowNum 指定行创建列名
     */
    private static void insertColumnName(int rowNum, XSSFSheet sheet, Map<String, String> alias) {
        XSSFRow row = sheet.createRow(rowNum);
        //列的数量
        int columnCount = 0;

        Set<Entry<String, String>> entrySet = alias.entrySet();

        for (Entry<String, String> entry : entrySet) {
            // 创建第一行的第columnCount个格子
            XSSFCell cell = row.createCell(columnCount++);
            // 将此格子的值设置为alias中的键名
            cell.setCellValue(isNull(entry.getValue()).toString());
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
    private static <T> void insertColumnDate(int beginRowNum, List<T> models, XSSFSheet sheet, Map<String, String> alias) throws Exception {
        for (T model : models) {
            // 创建新的一行
            XSSFRow rowTemp = sheet.createRow(beginRowNum++);
            // 获取列的迭代
            Set<Entry<String, String>> entrySet = alias.entrySet();

            // 从第0个格子开始创建
            int columnNum = 0;
            for (Entry<String, String> entry : entrySet) {
                // 获取属性值
                String property = BeanUtils.getProperty(model, entry.getKey());
                // 创建一个格子
                XSSFCell cell = rowTemp.createCell(columnNum++);
                // 得知string可以转化的类型
                if (isDouble(property)) {
                    cell.setCellValue(Double.parseDouble(property));
                } else if (isInt(property)) {
                    cell.setCellValue(Integer.parseInt(property));
                } else if (isDateAndTime(property)) {
                    // 只对日期加time的做转化
                    Date date = getDate(property);
                    cell.setCellValue(date);
                } else {
                    cell.setCellValue(property);
                }
            }
        }
    }

    private static Date getDate(String property) throws ParseException {
        SimpleDateFormat formatter;
        if (property.indexOf("-") >= 1) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (property.indexOf("/") >= 1) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (property.indexOf(".") >= 1) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        }
        return formatter.parse(property);
    }

    /**
     * 判断是否为空，若为空设为""
     */
    private static Object isNull(Object object) {
        return Objects.requireNonNullElse(object, "");
    }

    /**
     * 生成一个属性-列的对应关系的map
     *
     * @param sheet 表
     * @param alias 别名
     */
    private static Map<String, Integer> generateColumnPropertyMap(XSSFSheet sheet, LinkedHashMap<String, String> alias, Integer param) {
        Map<String, Integer> propertyMap = new HashMap<>(16);

        if (param == null || param < 0) {
            param = 1;
        }
        XSSFRow propertyRow = sheet.getRow(param);
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
    private static <T> List<T> generateList(XSSFSheet sheet, Map<String, Integer> propertyMap, Class<T> clazz, Integer param) throws Exception {
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
     * 将excel表转换成指定类型的对象数组，列名即作为对象属性
     *
     * @param clazz 类型
     * @throws Exception 异常
     */
    public static <T> List<T> excel2Pojo(InputStream inputStream, Class<T> clazz) throws Exception {
        LinkedHashMap<String, String> alias = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            alias.put(field.getName(), field.getName());
        }
        return excel2Pojo(inputStream, clazz, alias, 1);
    }


    /**
     * 是否为浮点数
     */
    private static boolean isDouble(String str) {
        return str.matches("^[-+]?[1-9][0-9]*\\.?[0-9]+$");
    }

    /**
     * 是否为整数
     */
    private static boolean isInt(String str) {
        return str.matches("^[-+]?[1-9]\\d*$");
    }

    private static boolean isDateAndTime(String str) {
        return str.matches("^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    }
}

