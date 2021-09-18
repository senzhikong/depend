package com.senzhikong.db.sql.wrapper;

import com.senzhikong.db.sql.CacheColumn;
import com.senzhikong.db.sql.CacheTable;
import com.senzhikong.util.string.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

public class WrapperBuilder {
    public static final String SELECT_PREFIX = "select_";
    public static final String AND = " AND ";
    public static final String OR = " OR ";
    protected static Log logger = LogFactory.getLog(WrapperBuilder.class);

    public static String wrapperValueToText(WrapperValue wrapperValue, List<Object> params) {
        String valueText;
        ValueType type = wrapperValue.getType();
        switch (type) {
            case VALUE:
                Object value = wrapperValue.getValue();
                if (value instanceof Collection) {
                    value = ((Collection<?>) value).toArray();
                }
                if (value.getClass().isArray()) {
                    StringBuilder text = new StringBuilder();
                    text.append("(");
                    for (int i = 0; i < Array.getLength(value); i++) {
                        if (i > 0) {
                            text.append(",");
                        }
                        params.add(Array.get(value, i));
                        text.append("?").append(params.size());
                    }
                    text.append(")");
                    valueText = text.toString();
                    break;
                }
                params.add(wrapperValue.getValue());
                valueText = "?" + params.size();
                break;
            case COLUMN:
                valueText = wrapperValue.getColumn();
                break;
            case FUNCTION:
                CacheColumn cacheColumn = WrapperParser.getColumn(wrapperValue.getFunction());
                String tableName = cacheColumn.getTable().getAsName();
                if (wrapperValue.getFunctionClass() != null) {
                    tableName = wrapperValue.getFunctionClass().getSimpleName();
                }
                valueText = cacheColumn.getColumnName();
                if (StringUtil.isNotEmpty(tableName)) {
                    valueText = tableName + "." + valueText;
                }
                break;
            case WRAPPER:
                valueText = WrapperBuilder.getWrapper(wrapperValue.getWrapper(), params);
                break;
            default:
                throw new RuntimeException("不支持的类型:" + type);
        }
        return valueText;
    }


    public static String getWrappers(List<Wrapper<? extends Serializable>> list, String concatStr,
            List<Object> params) {
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sql.append("\n");
                sql.append(concatStr);
                sql.append(" ");
            }
            Wrapper<? extends Serializable> wrapper = list.get(i);
            sql.append(getWrapper(wrapper, params));
        }
        return sql.toString();
    }

    public static String getWrapper(Wrapper<? extends Serializable> wrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        WrapperType wrapperType = wrapper.getType();
        switch (wrapperType) {
            case EQ:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" = ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params)).append(" ");
                break;
            case NOT_EQ:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" != ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params)).append(" ");
                break;
            case GT:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" > ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case LT:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" < ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case GE:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" >= ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case LE:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" <= ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case IS_NULL:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" is null");
                break;
            case NOT_NULL:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params))
                   .append(" is not null");
                break;
            case START:
            case END:
            case REGEX:
            case LIKE:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" like ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case IN:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" in ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params))
                   .append(" ");
                break;
            case NOT_IN:
                sql.append(" ").append(wrapperValueToText(wrapper.getValueList().get(0), params)).append(" not in ")
                   .append(wrapperValueToText(wrapper.getValueList().get(1), params)).append(" ");
                break;
            case FUNCTION:
                sql.append(getFunctionWrapper(wrapper, params));
                break;
            case OR:
                if (wrapper instanceof ListWrapper) {
                    String or = getWrappers(((ListWrapper<? extends Serializable>) wrapper).getWrapperList(), OR,
                            params);
                    sql.append("(").append(or).append(")");
                }
                break;
            case AND:
                String and = getWrappers(((ListWrapper<? extends Serializable>) wrapper).getWrapperList(), AND, params);
                sql.append("(").append(and).append(")");
                break;
            default:
                break;
        }
        return sql.toString();
    }

    public static String getFunctionWrapper(Wrapper<? extends Serializable> wrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        Function function = wrapper.getFunction();
        String functionParams = StringUtil.join(
                wrapper.getValueList().stream().map(v -> wrapperValueToText(v, params)).toArray(), ",");
        switch (function) {
            case COUNT_DISTINCT:
                sql.append("COUNT( DISTINCT ").append(functionParams).append(")");
                break;
            case CUSTOMIZE:
                sql.append(wrapper.getFunctionName()).append("(").append(functionParams).append(")");
                break;
            default:
                sql.append(function).append("(").append(functionParams).append(")");
                break;
        }
        return sql.toString();
    }

    public static void buildSelect(StringBuilder sql, CacheTable cacheTable,
            List<SelectWrapper<? extends Serializable>> selects, List<Object> params) {
        sql.append("SELECT ");
        if (selects == null || selects.isEmpty()) {
            sql.append(cacheTable.getAsName()).append(".* ");
            return;
        }
        for (int i = 0; i < selects.size(); i++) {
            SelectWrapper<? extends Serializable> wrapper = selects.get(i);
            SelectType wrapperType = wrapper.getSelectType();
            if (i > 0) {
                sql.append(",");
            }
            switch (wrapperType) {
                case COLUMN:
                    sql.append(wrapperValueToText(wrapper.getValueList().get(0), params));
                    break;
                case FUNCTION:
                    sql.append(getFunctionWrapper(wrapper, params));
                    break;
                default:
                    break;
            }
            sql.append(" AS ");
            if (StringUtil.isNotEmpty(wrapper.getAsName())) {
                sql.append(wrapper.getAsName());
            } else {
                sql.append(SELECT_PREFIX);
                sql.append(i);
            }
            sql.append(" ");
        }
    }

    public static void buildFrom(StringBuilder sql, QueryWrapper<? extends Serializable> queryWrapper) {
        CacheTable cacheTable = WrapperParser.getTable(queryWrapper.getGenericsClass());
        sql.append(" FROM ");
        sql.append(cacheTable.getFullNameAs());
        sql.append(" ");

    }

    public static void buildJoin(StringBuilder sql, List<JoinWrapper<? extends Serializable>> list,
            List<Object> params) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (JoinWrapper<?> join : list) {
            JoinType type = join.getJoinType();
            String joinType = "INNER JOIN";
            switch (type) {
                case RIGHT:
                    joinType = "RIGHT JOIN";
                    break;
                case LEFT:
                    joinType = "LEFT JOIN";
                    break;
                case OUTER:
                    joinType = "OUTER JOIN";
                    break;
                default:
                    break;
            }
            CacheTable cacheTable = WrapperParser.getTable(join.getGenericsClass());
            sql.append("\n");
            sql.append(joinType);
            sql.append(" ");
            sql.append(cacheTable.getFullNameAs());
            sql.append(" ON ");
            sql.append(getWrappers(join.getOn(), AND, params));
            sql.append(" ");
        }
    }


    public static void buildWhere(StringBuilder sql, List<Wrapper<? extends Serializable>> wheres,
            List<Object> params) {
        if (wheres == null || wheres.isEmpty()) {
            return;
        }
        sql.append("\nWHERE ");
        sql.append(getWrappers(wheres, AND, params));
    }


    public static void buildGroupBuy(StringBuilder sql, List<Wrapper<? extends Serializable>> wrapperList,
            List<Object> params) {
        if (wrapperList == null || wrapperList.isEmpty()) {
            return;
        }
        sql.append("\nGROUP BY ");
        for (int i = 0; i < wrapperList.size(); i++) {
            Wrapper<? extends Serializable> wrapper = wrapperList.get(i);
            if (i > 0) {
                sql.append(" , ");
            }
            sql.append(wrapperValueToText(wrapper.getValueList().get(0), params));
        }
    }

    public static void buildOrder(StringBuilder sql, List<OrderByWrapper<? extends Serializable>> wrapperList,
            List<Object> params) {
        if (wrapperList == null || wrapperList.isEmpty()) {
            return;
        }
        sql.append("\nORDER BY ");
        for (int i = 0; i < wrapperList.size(); i++) {
            OrderByWrapper<? extends Serializable> wrapper = wrapperList.get(i);
            OrderByType wrapperType = wrapper.getOrderByType();
            if (i > 0) {
                sql.append(" , ");
            }
            sql.append(wrapperValueToText(wrapper.getValueList().get(0), params));
            switch (wrapperType) {
                case ASC:
                    sql.append(" ASC ");
                    break;
                case DESC:
                    sql.append(" DESC ");
                    break;
                default:
                    throw new RuntimeException("不支持的排序方式：" + wrapperType);
            }
        }
    }

    public static void buildLimit(StringBuilder sql, PagerQueryWrapper<? extends Serializable> pagerQueryWrapper,
            List<Object> params) {
        sql.append("\nLIMIT ");
        params.add((pagerQueryWrapper.getPageNumber() - 1) * pagerQueryWrapper.getPageSize());
        sql.append("?").append(params.size());
        params.add(pagerQueryWrapper.getPageSize());
        sql.append(",?").append(params.size()).append(" ");
    }

    public static String countByWrapper(QueryWrapper<?> queryWrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        buildFrom(sql, queryWrapper);
        buildJoin(sql, queryWrapper.getJoinWrappers(), params);
        buildWhere(sql, queryWrapper.getWrapperList(), params);
        buildGroupBuy(sql, queryWrapper.getGroupBys(), params);
        return sql.toString();
    }

    public static String selectByWrapper(QueryWrapper<? extends Serializable> queryWrapper, List<Object> params) {
        StringBuilder sql = new StringBuilder();
        CacheTable cacheTable = WrapperParser.getTable(queryWrapper.getGenericsClass());
        buildSelect(sql, cacheTable, queryWrapper.getSelects(), params);
        buildFrom(sql, queryWrapper);
        buildJoin(sql, queryWrapper.getJoinWrappers(), params);
        buildWhere(sql, queryWrapper.getWrapperList(), params);
        buildGroupBuy(sql, queryWrapper.getGroupBys(), params);
        buildOrder(sql, queryWrapper.getOrderBys(), params);
        if (queryWrapper instanceof PagerQueryWrapper) {
            buildLimit(sql, (PagerQueryWrapper<? extends Serializable>) queryWrapper, params);
        }
        return sql.toString();
    }

}
