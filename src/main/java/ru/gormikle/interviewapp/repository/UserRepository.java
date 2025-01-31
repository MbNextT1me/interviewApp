package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
