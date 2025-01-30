package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.EmailData;
import ru.gormikle.interviewapp.entity.User;

import java.time.LocalDate;

@DataJpaTest
public class EmailDataRepositoryTest {

    @Autowired
    private EmailDataRepository emailDataRepository;


    private final UserRepository userRepository;

    @Autowired
    public EmailDataRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Проверка корректного создания информации о почте")
    void shouldSaveEmailData() {
        User user = new User();
        user.setName("Misha");
        user.setDateOfBirth(LocalDate.of(2002,4,18));
        user.setEncryptedPassword("password!");

        User savedUser = userRepository.save(user);

        EmailData emailData = new EmailData();
        emailData.setUser(savedUser);
        emailData.setEmail("test@mail.ru");
        EmailData savedEmailData = emailDataRepository.save(emailData);

        EmailData findEmailData = emailDataRepository.findById(savedEmailData.getId()).orElse(null);

        Assertions.assertThat(findEmailData).isNotNull();
        Assertions.assertThat(findEmailData.getId()).isNotNull();
        Assertions.assertThat(findEmailData.getEmail()).isEqualTo(savedEmailData.getEmail());
        Assertions.assertThat(findEmailData.getUser()).isEqualTo(savedUser);
    }
}
