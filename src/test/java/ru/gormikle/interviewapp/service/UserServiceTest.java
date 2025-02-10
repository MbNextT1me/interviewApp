package ru.gormikle.interviewapp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.gormikle.interviewapp.AbstractTestContainer;
import ru.gormikle.interviewapp.entity.EmailDataEntity;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;
import ru.gormikle.interviewapp.entity.UserEntity;
import ru.gormikle.interviewapp.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest extends AbstractTestContainer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        seedDatabase();
    }

    private void seedDatabase() {
        UserEntity user1 = new UserEntity();
        user1.setName("Alice");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 10));
        user1.setPassword("password1");

        UserEntity user2 = new UserEntity();
        user2.setName("Bob");
        user2.setDateOfBirth(LocalDate.of(2002, 7, 20));
        user2.setPassword("password2");

        UserEntity user3 = new UserEntity();
        user3.setName("Charlie");
        user3.setDateOfBirth(LocalDate.of(1990, 3, 15));
        user3.setPassword("password3");

        PhoneDataEntity phone1 = new PhoneDataEntity();
        phone1.setPhone("79207865432");
        phone1.setUserEntity(user1);
        user1.getPhoneDataEntityList().add(phone1);

        PhoneDataEntity phone2 = new PhoneDataEntity();
        phone2.setPhone("79123456789");
        phone2.setUserEntity(user2);
        user2.getPhoneDataEntityList().add(phone2);

        EmailDataEntity email1 = new EmailDataEntity();
        email1.setEmail("alice@example.com");
        email1.setUserEntity(user1);
        user1.getEmailDataEntityList().add(email1);

        EmailDataEntity email2 = new EmailDataEntity();
        email2.setEmail("bob@example.com");
        email2.setUserEntity(user2);
        user2.getEmailDataEntityList().add(email2);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Test
    void testSearchByDateOfBirth() {
        Page<UserEntity> users = userService.searchUsers(
                Optional.of(LocalDate.of(2000, 1, 1)),
                Optional.empty(), Optional.empty(), Optional.empty(),
                PageRequest.of(0, 10)
        );

        assertThat(users).hasSize(1);
        assertThat(users.getContent().get(0).getName()).isEqualTo("Bob");
    }

    @Test
    void testSearchByPhone() {
        Page<UserEntity> users = userService.searchUsers(
                Optional.empty(), Optional.of("79207865432"),
                Optional.empty(), Optional.empty(),
                PageRequest.of(0, 10)
        );

        assertThat(users).hasSize(1);
        assertThat(users.getContent().get(0).getName()).isEqualTo("Alice");
    }

    @Test
    void testSearchByEmail() {
        Page<UserEntity> users = userService.searchUsers(
                Optional.empty(), Optional.empty(),
                Optional.of("alice@example.com"), Optional.empty(),
                PageRequest.of(0, 10)
        );

        assertThat(users).hasSize(1);
        assertThat(users.getContent().get(0).getName()).isEqualTo("Alice");
    }

    @Test
    void testSearchByName() {
        Page<UserEntity> users = userService.searchUsers(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of("A"),
                PageRequest.of(0, 10)
        );

        assertThat(users).hasSize(1);
        assertThat(users.getContent().get(0).getName()).isEqualTo("Alice");
    }

    @Test
    void testPagination() {
        Page<UserEntity> usersPage1 = userService.searchUsers(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(),
                PageRequest.of(0, 2)
        );

        Page<UserEntity> usersPage2 = userService.searchUsers(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(),
                PageRequest.of(1, 2)
        );

        assertThat(usersPage1.getContent()).hasSize(2);
        assertThat(usersPage2.getContent()).hasSize(1);
    }
}
