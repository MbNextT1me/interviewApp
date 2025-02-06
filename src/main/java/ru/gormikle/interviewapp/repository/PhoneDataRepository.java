package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneDataEntity, Long> {

    boolean existsByPhone(String phone);

    Optional<PhoneDataEntity> findByPhone(String phone);
}
