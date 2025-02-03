package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailDataEntityListEmail(String email);

    Optional<UserEntity> findByPhoneDataEntityListPhone(String phone);
}
