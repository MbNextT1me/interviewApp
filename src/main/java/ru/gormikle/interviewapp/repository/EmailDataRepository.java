package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.EmailData;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
}
