package com.senzhikong.db.sql;

import com.senzhikong.db.entity.BaseEntity;
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
public interface BaseJpaRepository<T extends BaseEntity, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    default T findOneByExample(Example<T> example) {
        Optional<T> o = this.findOne(example);
        return o.orElse(null);
    }

    default T findOneById(ID id) {
        Optional<T> o = this.findById(id);
        return o.orElse(null);
    }

    default T findOne(ID id) {
        Optional<T> o = this.findById(id);
        return o.orElse(null);
    }

    List<T> findByIdIn(Long[] ids);

    List<T> findByIdIn(List<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    T findAndLockById(ID id);

    T findByIdAndStatus(ID id,String status);
}
