package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
