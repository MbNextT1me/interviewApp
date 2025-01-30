package ru.gormikle.interviewapp.repository;

import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.PhoneData;
import ru.gormikle.interviewapp.entity.User;

import java.time.LocalDate;

@DataJpaTest
public class PhoneDataRepositoryTest {

    @Autowired
    private PhoneDataRepository phoneDataRepository;


    private final UserRepository userRepository;

    @Autowired
    public PhoneDataRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Проверка корректного создания информации о телефоне")
    void shouldSavePhoneData() {
        User user = new User();
        user.setName("Misha");
        user.setDateOfBirth(LocalDate.of(2002,4,18));
        user.setEncryptedPassword("password!");

        User savedUser = userRepository.save(user);

        PhoneData phoneData = new PhoneData();
        phoneData.setUser(savedUser);
        phoneData.setPhone("79207865432");
        PhoneData savedPhoneData = phoneDataRepository.save(phoneData);

        PhoneData findPhoneData = phoneDataRepository.findById(savedPhoneData.getId()).orElse(null);

        Assertions.assertThat(findPhoneData).isNotNull();
        Assertions.assertThat(findPhoneData.getId()).isNotNull();
        Assertions.assertThat(findPhoneData.getPhone()).isEqualTo(savedPhoneData.getPhone());
        Assertions.assertThat(findPhoneData.getUser()).isEqualTo(savedUser);
    }
}
