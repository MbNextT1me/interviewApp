package ru.gormikle.interviewapp.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gormikle.interviewapp.dto.PagedResponseDto;
import ru.gormikle.interviewapp.dto.UserDto;
import ru.gormikle.interviewapp.entity.AccountEntity;
import ru.gormikle.interviewapp.entity.EmailDataEntity;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;
import ru.gormikle.interviewapp.entity.UserEntity;
import ru.gormikle.interviewapp.repository.AccountRepository;
import ru.gormikle.interviewapp.repository.EmailDataRepository;
import ru.gormikle.interviewapp.repository.PhoneDataRepository;
import ru.gormikle.interviewapp.repository.UserRepository;
import ru.gormikle.interviewapp.specification.UserSpecification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailRepository;
    private final PhoneDataRepository phoneRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @CacheEvict(value = {"users", "usersSearch"}, allEntries = true)
    public UserEntity createUser(String name, LocalDate dateOfBirth, String password, BigDecimal initialBalance, String email, String phone) {
        if (emailRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (phoneRepository.existsByPhone(phone)) {
            throw new RuntimeException("Phone already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setDateOfBirth(dateOfBirth);
        user.setEncryptedPassword(password);

        AccountEntity account = new AccountEntity();
        account.setUserEntity(user);
        account.setBalance(initialBalance);
        account.setInitialBalance(initialBalance);

        user.setAccountEntity(account);

        EmailDataEntity emailEntity = new EmailDataEntity();
        emailEntity.setEmail(email);
        emailEntity.setUserEntity(user);

        PhoneDataEntity phoneEntity = new PhoneDataEntity();
        phoneEntity.setPhone(phone);
        phoneEntity.setUserEntity(user);

        user.setEmailDataEntityList(List.of(emailEntity));
        user.setPhoneDataEntityList(List.of(phoneEntity));

        userRepository.save(user);
        accountRepository.save(account);
        emailRepository.save(emailEntity);
        phoneRepository.save(phoneEntity);

        return user;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        return UserDto.fromEntity(Objects.requireNonNull(user));
    }

    @Transactional
    public void addEmail(long userId, String email) {
        if (emailRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmailDataEntity newEmail = new EmailDataEntity();
        newEmail.setEmail(email);
        newEmail.setUserEntity(user);
        emailRepository.save(newEmail);
    }

    @Transactional
    public void updateEmail(long userId, String oldEmail, String newEmail) {
        if (emailRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("New email already exists");
        }

        EmailDataEntity emailEntity = emailRepository.findByEmail(oldEmail)
                .orElseThrow(() -> new RuntimeException("Old email not found"));

        if (!emailEntity.getUserEntity().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own email.");
        }

        emailEntity.setEmail(newEmail);
        emailRepository.save(emailEntity);
    }

    @Transactional
    public void deleteEmail(long userId, String email) {
        EmailDataEntity emailEntity = emailRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!emailEntity.getUserEntity().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own email.");
        }

        emailRepository.delete(emailEntity);
    }

    @Transactional
    public void addPhone(long userId, String phone) {
        if (phoneRepository.existsByPhone(phone)) {
            throw new RuntimeException("Phone already exists");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PhoneDataEntity newPhone = new PhoneDataEntity();
        newPhone.setUserEntity(user);
        newPhone.setPhone(phone);

        phoneRepository.save(newPhone);
    }

    @Transactional
    public void updatePhone(long userId, String oldPhone, String newPhone) {
        if (phoneRepository.existsByPhone(newPhone)) {
            throw new RuntimeException("New phone already exists");
        }

        PhoneDataEntity phoneEntity = phoneRepository.findByPhone(oldPhone)
                .orElseThrow(() -> new RuntimeException("Old phone not found."));

        if (!phoneEntity.getUserEntity().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own phone.");
        }

        phoneEntity.setPhone(newPhone);
        phoneRepository.save(phoneEntity);
    }

    @Transactional
    public void deletePhone(long userId, String phone) {
        PhoneDataEntity phoneEntity = phoneRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Phone not found."));

        if (!phoneEntity.getUserEntity().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own phone.");
        }

        phoneRepository.delete(phoneEntity);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<UserDto> searchUsers(Optional<LocalDate> dateOfBirth, Optional<String> phone,
                                                    Optional<String> email, Optional<String> name, Pageable pageable) {
        Specification<UserEntity> spec = Specification.where(null);

        if (dateOfBirth.isPresent()) {
            spec = spec.and(UserSpecification.filterByDateOfBirth(dateOfBirth.get()));
        }
        if (phone.isPresent()) {
            spec = spec.and(UserSpecification.filterByPhone(phone.get()));
        }
        if (email.isPresent()) {
            spec = spec.and(UserSpecification.filterByEmail(email.get()));
        }
        if (name.isPresent()) {
            spec = spec.and(UserSpecification.filterByName(name.get()));
        }
        Page<UserEntity> users = userRepository.findAll(spec, pageable);

        users.forEach(user -> {
            Hibernate.initialize(user.getPhoneDataEntityList());
            Hibernate.initialize(user.getEmailDataEntityList());
        });

        PagedResponseDto<UserDto> pagedResponseDto = new PagedResponseDto<>();
        pagedResponseDto.setContent(users.getContent().stream().map(UserDto::fromEntity).collect(Collectors.toList()));
        pagedResponseDto.setTotalElements(users.getTotalElements());
        pagedResponseDto.setTotalPages(users.getTotalPages());

        return pagedResponseDto;
    }
}
