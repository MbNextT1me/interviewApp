package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.EmailDataEntity;

public interface EmailDataRepository extends JpaRepository<EmailDataEntity, Long> {
}
