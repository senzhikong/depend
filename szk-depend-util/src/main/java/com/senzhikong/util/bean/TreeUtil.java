package com.senzhikong.util.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.util.string.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shu.zhou
 * @date 2018年10月22日下午2:39:43
 */
public class TreeUtil {

    public static <T> JSONArray buildTreeList(List<T> fromList, String id, String pid, Object firstLevel,
            String children) {
        if (StringUtil.isEmpty(children)) {
            children = "children";
        }
        JSONArray list = JSONArray.parseArray(JSON.toJSONString(fromList));
        JSONArray array1 = findChildren(list, pid, firstLevel);
        for (int i = 0; i < array1.size(); i++) {
            JSONObject obj1 = array1.getJSONObject(i);
            Object secondLevel = obj1.get(id);
            JSONArray array2 = findChildren(list, pid, secondLevel);
            obj1.put(children, array2);
            for (int j = 0; j < array2.size(); j++) {
                JSONObject obj2 = array2.getJSONObject(j);
                Object thirdLevel = obj2.get(id);
                JSONArray array3 = findChildren(list, pid, thirdLevel);
                obj2.put(children, array3);
            }
        }
        return array1;
    }

    public static <T> List<T> treeToList(List<T> list, String childName) {
        List<T> res = new ArrayList<>();
        for (T item : list) {
            res.add(item);
            try {
                final String getMethodName = getMethod(childName);
                List<T> children = (List<T>) item.getClass().getMethod(getMethodName).invoke(item);
                if (children != null && !children.isEmpty()) {
                    res.addAll(treeToList(children, childName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static JSONArray findChildren(JSONArray array, String pid, Object value) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < array.size(); ) {
            JSONObject obj = array.getJSONObject(i);
            Object pvalue = obj.get(pid);
            if (value == null && pvalue == null) {
                result.add(obj);
                array.remove(i);
            } else if (value != null && value.equals(pvalue)) {
                result.add(obj);
                array.remove(i);
            } else {
                i++;
            }
        }
        return result;
    }

    public static <T> List<T> buildTree(List<T> fromList, Object firstLevel) {
        return buildTree(fromList, "id", "parentId", "children", firstLevel);
    }

    public static <T> List<T> buildTree(List<T> fromList, String idKey, String pidKey, String childrenKey,
            Object firstLevel) {
        List<T> rootArray = findChildrenObj(fromList, pidKey, firstLevel);
        fromList.removeAll(rootArray);
        List<T> eachList;
        final String setMethodName = setMethod(childrenKey);
        final String getIdMethodName = getMethod(idKey);
        eachList = rootArray;
        while (fromList.size() > 0 && eachList.size() > 0) {
            List<T> tempList = new ArrayList<>();
            for (T item : eachList) {
                try {
                    Object idValue = item.getClass().getMethod(getIdMethodName).invoke(item);
                    List<T> childList = findChildrenObj(fromList, pidKey, idValue);
                    fromList.removeAll(childList);
                    tempList.addAll(childList);
                    if (childList.size() > 0) {
                        Method method = item.getClass().getMethod(setMethodName, List.class);
                        method.invoke(item, childList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            eachList = tempList;
        }
        return rootArray;
    }

    private static <T> List<T> findChildrenObj(List<T> fromList, String pidKey, Object value) {
        final String getMethodName = getMethod(pidKey);
        return fromList.stream().filter(item -> {
            try {
                Method method = item.getClass().getMethod(getMethodName);
                if (value == null) {
                    return null == method.invoke(item);
                }
                Object val = method.invoke(item);
                return value.equals(val);
            } catch (Exception ignore) {
                ignore.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
    }

    private static String getMethod(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static String setMethod(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
