package com.senzhikong.basic.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.senzhikong.basic.dto.PagerParam;
import com.senzhikong.basic.dto.PagerResp;
import com.senzhikong.basic.entity.BaseEntityPO;
import com.senzhikong.basic.vo.BaseEntityVO;

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
     * @return warpper
     */
    QueryWrapper<PO> generateWrapper(VO query, String searchWord);

    /**
     * 通ID主键查询
     *
     * @param id 主键
     * @return 查询结果
     */
    VO findById(Long id);

    /**
     * 查询全部
     *
     * @return 查询结果
     */
    List<VO> findAll();

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
     * @return 查询结果数字
     */
    List<VO> findList(List<Long> ids);

    /**
     * List
     * 通过逐渐批量查询
     *
     * @param ids 主键数组
     * @return 查询结果数字
     */
    List<VO> findList(Long[] ids);

    /**
     * 新增
     *
     * @param vo       新增数据
     * @param createBy 创建人
     * @return 返回数据
     */
    VO save(VO vo, Long createBy);

    /**
     * 新增列表
     *
     * @param list     新增数据列表
     * @param createBy 创建人
     * @return 返回数据
     */
    List<VO> saveList(List<VO> list, Long createBy);

    /**
     * 更新
     *
     * @param vo       更新数据
     * @param updateBy 更新人
     * @return 返回数据
     */
    VO update(VO vo, Long updateBy);

    /**
     * 更新列表
     *
     * @param list     更新数据列表
     * @param updateBy 更新人
     */
    void updateList(List<VO> list, Long updateBy);

    /**
     * 通过ID修改为删除状态
     *
     * @param ids      主键数数组
     * @param updateBy 更新人
     */
    void deleteByStatus(Long[] ids, Long updateBy);

    /**
     * 通过ID修改为删除状态
     *
     * @param ids      主键数数组
     * @param updateBy 更新人
     */
    void deleteByStatus(List<Long> ids, Long updateBy);

    /**
     * 通过ID修改为删除状态
     *
     * @param id       主键
     * @param updateBy 更新人
     */
    void deleteByStatus(Long id, Long updateBy);

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
