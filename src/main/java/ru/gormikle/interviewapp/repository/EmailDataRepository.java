package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.EmailDataEntity;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailDataEntity, Long> {

    boolean existsByEmail(String email);

    Optional<EmailDataEntity> findByEmail(String email);
}
