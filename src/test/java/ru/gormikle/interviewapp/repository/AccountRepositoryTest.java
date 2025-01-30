package ru.gormikle.interviewapp.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gormikle.interviewapp.entity.Account;
import ru.gormikle.interviewapp.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
public class AccountRepositoryTest {

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
        User user = new User();
        user.setName("Misha");
        user.setDateOfBirth(LocalDate.of(2002,4,18));
        user.setEncryptedPassword("password!");

        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(savedUser);
        account.setBalance(new BigDecimal("123.45"));

        Account savedAccount = accountRepository.save(account);
        Account findAccount = accountRepository.findById(savedAccount.getId()).orElse(null);

        Assertions.assertThat(findAccount).isNotNull();
        Assertions.assertThat(findAccount.getId()).isNotNull();
        Assertions.assertThat(findAccount.getBalance()).isEqualTo(savedAccount.getBalance());
        Assertions.assertThat(findAccount.getUser()).isEqualTo(savedUser);
    }
}
