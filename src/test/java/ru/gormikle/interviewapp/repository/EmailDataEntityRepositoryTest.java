package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.EmailDataEntity;
import ru.gormikle.interviewapp.entity.UserEntity;

import java.time.LocalDate;

@DataJpaTest
public class EmailDataEntityRepositoryTest {

    @Autowired
    private EmailDataRepository emailDataRepository;


    private final UserRepository userRepository;

    @Autowired
    public EmailDataEntityRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Проверка корректного создания информации о почте")
    void shouldSaveEmailData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Misha");
        userEntity.setDateOfBirth(LocalDate.of(2002,4,18));
        userEntity.setEncryptedPassword("password!");

        UserEntity savedUserEntity = userRepository.save(userEntity);

        EmailDataEntity emailDataEntity = new EmailDataEntity();
        emailDataEntity.setUserEntity(savedUserEntity);
        emailDataEntity.setEmail("test@mail.ru");
        EmailDataEntity savedEmailDataEntity = emailDataRepository.save(emailDataEntity);

        EmailDataEntity findEmailDataEntity = emailDataRepository.findById(savedEmailDataEntity.getId()).orElse(null);

        Assertions.assertThat(findEmailDataEntity).isNotNull();
        Assertions.assertThat(findEmailDataEntity.getId()).isNotNull();
        Assertions.assertThat(findEmailDataEntity.getEmail()).isEqualTo(savedEmailDataEntity.getEmail());
        Assertions.assertThat(findEmailDataEntity.getUserEntity()).isEqualTo(savedUserEntity);
    }
}
