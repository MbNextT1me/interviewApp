package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.AccountEntity;
import ru.gormikle.interviewapp.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
public class AccountEntityRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Проверка корректного создания аккаунта")
    void shouldSaveAccount() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Misha");
        userEntity.setDateOfBirth(LocalDate.of(2002,4,18));
        userEntity.setEncryptedPassword("password!");

        UserEntity savedUserEntity = userRepository.save(userEntity);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUserEntity(savedUserEntity);
        accountEntity.setBalance(new BigDecimal("123.45"));

        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
        AccountEntity findAccountEntity = accountRepository.findById(savedAccountEntity.getId()).orElse(null);

        Assertions.assertThat(findAccountEntity).isNotNull();
        Assertions.assertThat(findAccountEntity.getId()).isNotNull();
        Assertions.assertThat(findAccountEntity.getBalance()).isEqualTo(savedAccountEntity.getBalance());
        Assertions.assertThat(findAccountEntity.getUserEntity()).isEqualTo(savedUserEntity);
    }
}
