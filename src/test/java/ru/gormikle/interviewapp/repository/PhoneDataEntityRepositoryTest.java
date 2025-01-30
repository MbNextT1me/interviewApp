package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;
import ru.gormikle.interviewapp.entity.UserEntity;

import java.time.LocalDate;

@DataJpaTest
public class PhoneDataEntityRepositoryTest {

    @Autowired
    private PhoneDataRepository phoneDataRepository;


    private final UserRepository userRepository;

    @Autowired
    public PhoneDataEntityRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Проверка корректного создания информации о телефоне")
    void shouldSavePhoneData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Misha");
        userEntity.setDateOfBirth(LocalDate.of(2002,4,18));
        userEntity.setEncryptedPassword("password!");

        UserEntity savedUserEntity = userRepository.save(userEntity);

        PhoneDataEntity phoneDataEntity = new PhoneDataEntity();
        phoneDataEntity.setUserEntity(savedUserEntity);
        phoneDataEntity.setPhone("79207865432");
        PhoneDataEntity savedPhoneDataEntity = phoneDataRepository.save(phoneDataEntity);

        PhoneDataEntity findPhoneDataEntity = phoneDataRepository.findById(savedPhoneDataEntity.getId()).orElse(null);

        Assertions.assertThat(findPhoneDataEntity).isNotNull();
        Assertions.assertThat(findPhoneDataEntity.getId()).isNotNull();
        Assertions.assertThat(findPhoneDataEntity.getPhone()).isEqualTo(savedPhoneDataEntity.getPhone());
        Assertions.assertThat(findPhoneDataEntity.getUserEntity()).isEqualTo(savedUserEntity);
    }
}
