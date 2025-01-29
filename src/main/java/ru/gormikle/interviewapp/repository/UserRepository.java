package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
