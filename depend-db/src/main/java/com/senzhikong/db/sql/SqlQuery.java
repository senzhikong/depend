package com.senzhikong.db.sql;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public interface SqlQuery<T> extends Specification<T> {
    default Predicate toPredicate(@Nullable Root<T> root, @Nullable CriteriaQuery<?> criteriaQuery,
            @Nullable CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> likeOr = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        build(root, criteriaQuery, criteriaBuilder, predicates, likeOr, orders);
        assert criteriaQuery != null;
        if (likeOr.size() > 0) {
            assert criteriaBuilder != null;
            predicates.add(criteriaBuilder.or(likeOr.toArray(new Predicate[0])));
        }
        if (orders.size() > 0) {
            criteriaQuery.orderBy(orders);
        }
        if (predicates.size() > 0) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        return criteriaQuery.getRestriction();
    }

    void build(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates,
            List<Predicate> likeOr, List<Order> orders);
}
