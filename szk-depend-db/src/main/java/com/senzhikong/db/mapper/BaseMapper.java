package com.senzhikong.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.List;

/**
 * @author shu.zhou
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    /**
     * 通过单个字段值查询
     *
     * @param key   字段名
     * @param value 值
     * @return 查询结果
     */
    default T selectOne(String key, Object value) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq(key, value);
        return this.selectOne(wrapper);
    }

    /**
     * 通过单个字段值查询
     *
     * @param key   字段名
     * @param value 值
     * @return 查询结果
     */
    default T selectOne(SFunction<T, ?> key, Object value) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(key, value);
        return this.selectOne(wrapper);
    }


    /**
     * 通过单个字段值查询列表
     *
     * @param key   字段名
     * @param value 值
     * @return 查询结果
     */
    default List<T> selectList(String key, Object value) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq(key, value);
        return this.selectList(wrapper);
    }

    /**
     * 通过单个字段值查询
     *
     * @param key   字段名
     * @param value 值
     * @return 查询结果
     */
    default List<T> selectList(SFunction<T, ?> key, Object value) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(key, value);
        return this.selectList(wrapper);
    }

    /**
     * 批量插入
     *
     * @param list 插入数据列表
     */
    default void insertBatch(List<T> list) {
        for (T t : list) {
            this.insert(t);
        }
    }

    /**
     * 批量更新
     *
     * @param list 更新数据列表
     */
    default void updateBatch(List<T> list) {
        for (T t : list) {
            this.updateById(t);
        }
    }
}
