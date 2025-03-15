package ru.gormikle.interviewapp.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gormikle.interviewapp.entity.UserEntity;
import ru.gormikle.interviewapp.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с id: " + userId + " не найден."));
        return org.springframework.security.core.userdetails.User
                .withUsername(String.valueOf(user.getId()))
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> userOpt;
        if (login.contains("@")) {
            userOpt = userRepository.findByEmailDataEntityListEmail(login);
        } else {
            userOpt = userRepository.findByPhoneDataEntityListPhone(login);
        }
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден с именем: " + login);
        }
        UserEntity user = userOpt.get();
        return User.withUsername(String.valueOf(user.getId()))
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}

