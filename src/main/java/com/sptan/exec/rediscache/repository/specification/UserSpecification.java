package com.sptan.exec.rediscache.repository.specification;

import com.google.common.collect.Lists;
import com.sptan.exec.rediscache.domain.User;
import com.sptan.exec.rediscache.service.dto.UserQueryCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * The interface Role specification.
 *
 * @author liupeng
 * @version 1.0
 */
public interface UserSpecification {

    /**
     * Query specification.
     *
     * @param criteria the criteria
     * @return the specification
     */
    static Specification<User> query(UserQueryCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> condition = Lists.newArrayList();

            // 用户名
            final String username = criteria.getUsername();
            if (!StringUtils.isEmpty(username)) {
                condition.add(
                    criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }

            return criteriaQuery.where(condition.toArray(new Predicate[condition.size()])).getRestriction();
        };
    }
}
