package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.User;

import java.time.LocalDate;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Проверка корректного создания пользователя")
    void shouldSaveAndReturnUser(){
        User user = new User();
        user.setName("Misha");
        user.setDateOfBirth(LocalDate.of(2002,4,18));
        user.setEncryptedPassword("password!");

        User savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser.getId()).isNotNull();

        User findUser = userRepository.findById(savedUser.getId()).orElse(null);
        Assertions.assertThat(findUser).isNotNull();
        Assertions.assertThat(findUser.getName()).isEqualTo(savedUser.getName());
        Assertions.assertThat(findUser.getDateOfBirth()).isEqualTo(savedUser.getDateOfBirth());
    }
}
