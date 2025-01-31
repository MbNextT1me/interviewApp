package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.UserEntity;

import java.time.LocalDate;

@DataJpaTest
public class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Проверка корректного создания пользователя")
    void shouldSaveAndReturnUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Misha");
        userEntity.setDateOfBirth(LocalDate.of(2002,4,18));
        userEntity.setEncryptedPassword("password!");

        UserEntity savedUserEntity = userRepository.save(userEntity);
        Assertions.assertThat(savedUserEntity.getId()).isNotNull();

        UserEntity findUserEntity = userRepository.findById(savedUserEntity.getId()).orElse(null);
        Assertions.assertThat(findUserEntity).isNotNull();
        Assertions.assertThat(findUserEntity.getName()).isEqualTo(savedUserEntity.getName());
        Assertions.assertThat(findUserEntity.getDateOfBirth()).isEqualTo(savedUserEntity.getDateOfBirth());
    }
}
