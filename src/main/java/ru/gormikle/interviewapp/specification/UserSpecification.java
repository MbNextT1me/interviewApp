package ru.gormikle.interviewapp.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.gormikle.interviewapp.entity.UserEntity;

import javax.persistence.criteria.*;
import java.time.LocalDate;

public class UserSpecification {

    public static Specification<UserEntity> filterByDateOfBirth(LocalDate dateOfBirth) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth);
    }

    public static Specification<UserEntity> filterByPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> phoneJoin = root.join("phoneDataEntityList", JoinType.INNER);
            return criteriaBuilder.equal(phoneJoin.get("phone"), phone);
        };
    }

    public static Specification<UserEntity> filterByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> emailJoin = root.join("emailDataEntityList", JoinType.INNER);
            return criteriaBuilder.equal(emailJoin.get("email"), email);
        };
    }

    public static Specification<UserEntity> filterByName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), name + "%");
    }
}

