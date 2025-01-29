package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
