package com.senzhikong.util.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Shu.zhou
 * @date 2018年10月24日下午5:10:57
 */
public class BeanUtil {


    public static <T> List<String> getStringList(List<T> list, String column) {
        List<String> result = new ArrayList<>();
        for (T obj : list) {
            try {
                String methodName = "get" + column.substring(0, 1)
                                                  .toUpperCase() + column.substring(1);
                Method method = obj.getClass()
                                   .getMethod(methodName);
                String id = (String) method.invoke(obj);
                result.add(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static <T> List<Long> getLongList(List<T> list, String column) {
        List<Long> result = new ArrayList<>();
        for (T obj : list) {
            try {
                String methodName = "get" + column.substring(0, 1)
                                                  .toUpperCase() + column.substring(1);
                Method method = obj.getClass()
                                   .getMethod(methodName);
                Long id = (Long) method.invoke(obj);
                result.add(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static <T, K> List<K> getListByName(List<T> list, String column) {
        List<K> result = new ArrayList<K>();
        for (T obj : list) {
            try {
                String methodName = "get" + column.substring(0, 1)
                                                  .toUpperCase() + column.substring(1);
                Method method = obj.getClass()
                                   .getMethod(methodName);
                @SuppressWarnings("unchecked") K val = (K) method.invoke(obj);
                result.add(val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static <T> Long[] getLongArray(List<T> list, String column) {
        List<Long> result = getLongList(list, column);
        Long[] rrr = new Long[result.size()];
        return result.toArray(rrr);
    }

    public static JSONArray toJSONArray(Object o) {
        return JSONArray.parseArray(JSON.toJSONString(o));
    }

    public static JSONObject toJSONObject(Object o) {
        return JSONObject.parseObject(JSON.toJSONString(o));
    }

    public static <T> T build(Class<T> cls) {
        T obj = null;
        try {
            obj = cls.getDeclaredConstructor()
                     .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <K, T> Map<K, T> listToMap(List<T> list, Class<K> cls, String column) {
        return listToMap(list, column);
    }

    public static <K, T> Map<K, T> listToMap(List<T> list, String column) {
        Map<K, T> map = new HashMap<>();
        for (T obj : list) {
            try {
                String methodName = "get" + column.substring(0, 1)
                                                  .toUpperCase() + column.substring(1);
                Method method = obj.getClass()
                                   .getMethod(methodName);
                @SuppressWarnings("unchecked") K id = (K) method.invoke(obj);
                map.put(id, obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    public static <T> List<T> arrayToList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static <T> T[] listToArray(List<T> list) {
        @SuppressWarnings("unchecked") T[] array = (T[]) list.toArray();
        return array;
    }

    public static <T, K> void setListPropFromList(List<T> list1, String prop, String key1, List<K> list2, String key2) {
        try {
            Map<Object, K> map = BeanUtil.listToMap(list2, key2);
            for (T item : list1) {
                String methodNameGet = "get" + key1.substring(0, 1)
                                                   .toUpperCase() + key1.substring(1);
                Method methodGet = item.getClass()
                                       .getMethod(methodNameGet);
                Object keyValue = methodGet.invoke(item);
                K val = map.get(keyValue);
                if (val == null) {
                    continue;
                }
                String methodNameSet = "set" + prop.substring(0, 1)
                                                   .toUpperCase() + prop.substring(1);
                Method methodSet = item.getClass()
                                       .getMethod(methodNameSet, val.getClass());
                methodSet.invoke(item, val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, K, H> void setListPropFromList(List<T> target, String prop1, String key1, List<K> source,
            String prop2, String key2) {
        try {
            Map<Object, K> map = BeanUtil.listToMap(source, key2);
            for (T item : target) {
                String methodNameGet = getMethodName(key1);
                Method methodGet = item.getClass().getMethod(methodNameGet);
                Object keyValue = methodGet.invoke(item);
                K val = map.get(keyValue);
                if (val == null) {
                    continue;
                }
                String methodNameGet2 = getMethodName(prop2);
                Method methodGet2 = val.getClass().getMethod(methodNameGet2);
                Object propVal = methodGet2.invoke(val);

                String methodNameSet = setMethodName(prop1);
                Method methodSet = item.getClass().getMethod(methodNameSet, propVal.getClass());
                methodSet.invoke(item, propVal);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convert(Object obj, Class<T> clz) {
        return JSONObject.parseObject(JSON.toJSONString(obj), clz);
    }

    public static String getMethodName(String prop) {
        return "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
    }

    public static String setMethodName(String prop) {
        return "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
    }
}
