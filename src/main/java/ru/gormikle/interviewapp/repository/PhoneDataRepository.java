package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;

public interface PhoneDataRepository extends JpaRepository<PhoneDataEntity, Long> {
}
