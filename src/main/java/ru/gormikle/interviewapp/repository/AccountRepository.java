package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gormikle.interviewapp.entity.AccountEntity;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select e from AccountEntity e where e.id = :id")
    Optional<AccountEntity> findByIdForUpdate(@Param("id") Long id);
}
