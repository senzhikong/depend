package com.senzhikong.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.PagerParam;
import com.senzhikong.basic.dto.PagerResp;
import com.senzhikong.core.entity.BaseEntityPO;

import java.util.List;

/**
 * @author shu.zhou
 */
public interface IBaseService<PO extends BaseEntityPO, VO extends BaseEntityVO> {
    /**
     * 生成基础查询queryWrapper
     *
     * @param query      查询参数
     * @param searchWord 搜索关键字
     * @return wrapper
     */
    QueryWrapper<PO> generateWrapper(VO query, String searchWord);

    /**
     * 通ID主键查询
     *
     * @param id 主键
     * @return 查询结果
     */
    VO findById(String id);

    /**
     * 查询全部
     *
     * @return 查询结果
     */
    List<VO> findAll();

    /**
     * 根据条件查询
     *
     * @param wrapper 查询条件
     * @return 查询结果
     */
    VO findOne(QueryWrapper<PO> wrapper);

    /**
     * 通过条件查询
     *
     * @param vo 查询参数
     * @return 查询结果
     */
    VO findOne(VO vo);

    /**
     * 通过条件查询
     *
     * @param vo      查询参数
     * @param keyword 关键字
     * @return 查询结果
     */
    VO findOne(VO vo, String keyword);

    /**
     * 通过字段查询
     *
     * @param key   查询参数
     * @param value 关键字
     * @return 查询结果
     */
    VO findOneByKey(String key, Object value);

    /**
     * 根据条件查询列表
     *
     * @param wrapper 查询条件
     * @return 查询结果
     */
    List<VO> findList(QueryWrapper<PO> wrapper);

    /**
     * 通过逐渐批量查询
     *
     * @param ids 主键列表
     * @return 查询结果数组
     */
    List<VO> findList(List<String> ids);

    /**
     * 通过逐渐批量查询
     *
     * @param ids 主键数组
     * @return 查询结果数组
     */
    List<VO> findList(String[] ids);

    /**
     * 通过条件批量查询
     *
     * @param vo 查询参数
     * @return 查询结果数组
     */
    List<VO> findList(VO vo);

    /**
     * 通过条件批量查询
     *
     * @param vo      查询参数
     * @param keyword 关键字
     * @return 查询结果数组
     */
    List<VO> findList(VO vo, String keyword);

    /**
     * 通过字段查询
     *
     * @param key   查询参数
     * @param value 关键字
     * @return 查询结果
     */
    List<VO> findListByKey(String key, Object value);

    /**
     * 新增
     *
     * @param vo       新增数据
     * @param createBy 创建人
     * @return 返回数据
     */
    VO save(VO vo, String createBy);

    /**
     * 新增列表
     *
     * @param list     新增数据列表
     * @param createBy 创建人
     * @return 返回数据
     */
    List<VO> saveList(List<VO> list, String createBy);

    /**
     * 更新
     *
     * @param vo       更新数据
     * @param updateBy 更新人
     * @return 返回数据
     */
    VO update(VO vo, String updateBy);

    /**
     * 跳过空值更新
     *
     * @param vo       更新数据
     * @param updateBy 更新人
     * @return 返回数据
     */
    VO updateIgnoreNull(VO vo, String updateBy);

    /**
     * 更新列表
     *
     * @param list     更新数据列表
     * @param updateBy 更新人
     */
    void updateList(List<VO> list, String updateBy);

    /**
     * 跳过空值更新列表
     *
     * @param list     更新数据列表
     * @param updateBy 更新人
     */
    void updateListIgnoreNull(List<VO> list, String updateBy);

    /**
     * 通过ID修改为删除状态
     *
     * @param ids      主键数数组
     * @param updateBy 更新人
     */
    void deleteByStatus(String[] ids, String updateBy);

    /**
     * 通过ID修改为删除状态
     *
     * @param ids      主键数数组
     * @param updateBy 更新人
     */
    void deleteByStatus(List<String> ids, String updateBy);

    /**
     * 通过ID修改为删除状态
     *
     * @param id       主键
     * @param updateBy 更新人
     */
    void deleteByStatus(String id, String updateBy);

    /**
     * 通过ID修改状态
     *
     * @param id       主键
     * @param status   状态
     * @param updateBy 更新人
     */
    void updateStatus(String id, String status, String updateBy);


    /**
     * 通过ID修改状态
     *
     * @param ids      主键
     * @param status   状态s
     * @param updateBy 更新人
     */
    void updateStatus(List<String> ids, String status, String updateBy);


    /**
     * 通过ID修改状态
     *
     * @param ids      主键
     * @param status   状态s
     * @param updateBy 更新人
     */
    void updateStatus(String[] ids, String status, String updateBy);

    /**
     * 分页查询
     *
     * @param pager 分页条件
     * @return 查询结果
     */
    PagerResp<VO> findByPage(PagerParam pager);

    /**
     * 分页查询
     *
     * @param pager 分页条件
     * @param vo    查询条件
     * @return 查询结果
     */
    PagerResp<VO> findByPage(PagerParam pager, VO vo);

    /**
     * 根据条件分页查询
     *
     * @param pager   分页参数
     * @param wrapper 查询条件
     * @return 查询结果
     */
    PagerResp<VO> findByPage(PagerParam pager, QueryWrapper<PO> wrapper);
}
