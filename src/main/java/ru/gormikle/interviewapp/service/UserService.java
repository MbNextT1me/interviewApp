package ru.gormikle.interviewapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gormikle.interviewapp.entity.EmailDataEntity;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;
import ru.gormikle.interviewapp.entity.UserEntity;
import ru.gormikle.interviewapp.repository.EmailDataRepository;
import ru.gormikle.interviewapp.repository.PhoneDataRepository;
import ru.gormikle.interviewapp.repository.UserRepository;
import ru.gormikle.interviewapp.specification.UserSpecification;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailRepository;
    private final PhoneDataRepository phoneRepository;

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

        if (emailEntity.getUserEntity().getId() != userId) {
            throw new RuntimeException("You can only update your own email.");
        }

        emailEntity.setEmail(newEmail);
        emailRepository.save(emailEntity);
    }

    @Transactional
    public void deleteEmail(long userId, String email) {
        EmailDataEntity emailEntity = emailRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (emailEntity.getUserEntity().getId() != userId) {
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

        if (phoneEntity.getUserEntity().getId() != userId) {
            throw new RuntimeException("You can only update your own phone.");
        }

        phoneEntity.setPhone(newPhone);
        phoneRepository.save(phoneEntity);
    }

    @Transactional
    public void deletePhone(long userId, String phone) {
        PhoneDataEntity phoneEntity = phoneRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Phone not found."));

        if (phoneEntity.getUserEntity().getId() != userId) {
            throw new RuntimeException("You can only delete your own phone.");
        }

        phoneRepository.delete(phoneEntity);
    }

    public Page<UserEntity> searchUsers(Optional<LocalDate> dateOfBirth, Optional<String> phone,
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

        return userRepository.findAll(spec, pageable);
    }
}
