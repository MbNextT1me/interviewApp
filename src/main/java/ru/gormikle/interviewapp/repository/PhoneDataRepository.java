package ru.gormikle.interviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gormikle.interviewapp.entity.PhoneData;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
}
