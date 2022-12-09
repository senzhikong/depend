package com.senzhikong.db.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Shu.zhou
 * @date 2018年9月24日下午4:01:24
 */
@NoRepositoryBean
public interface BaseJpaRepository<T extends Serializable, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    /**
     * 通过example进行查询
     *
     * @param example example
     * @return 实体类
     */
    default T findOneByExample(Example<T> example) {
        Optional<T> o = this.findOne(example);
        return o.orElse(null);
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 查询结果
     */
    default T findOneById(ID id) {
        Optional<T> o = this.findById(id);
        return o.orElse(null);
    }

    /**
     * 通过主键数组查询
     *
     * @param ids 主键数组
     * @return 结果列表
     */
    List<T> findByIdIn(ID[] ids);

    /**
     * 通过主键集合查询
     *
     * @param ids 主键集合
     * @return 结果列表
     */
    List<T> findByIdIn(List<ID> ids);

    /**
     * 通过主键查询并锁定
     *
     * @param id 主键
     * @return 实体类
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    T findAndLockById(ID id);

    /**
     * 通过主键和状态查询
     *
     * @param id     主键
     * @param status 状态
     * @return 实体类
     */
    T findByIdAndStatus(ID id, String status);

    /**
     * 通过逐渐删除
     *
     * @param id 主键
     */
    void deleteById(ID id);

    /**
     * 通过逐渐删除
     *
     * @param ids 主键数组
     */
    void deleteByIdIn(ID[] ids);

    /**
     * 通过逐渐删除
     *
     * @param ids 主键数组
     */
    void deleteByIdIn(List<ID> ids);
}
