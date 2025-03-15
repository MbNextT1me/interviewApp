package ru.gormikle.interviewapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gormikle.interviewapp.dto.CreateUserRequestDto;
import ru.gormikle.interviewapp.dto.UserDto;
import ru.gormikle.interviewapp.entity.UserEntity;
import ru.gormikle.interviewapp.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid CreateUserRequestDto request) {
        UserEntity user = userService.createUser(
                request.getName(),
                request.getDateOfBirth(),
                request.getPassword(),
                request.getInitialBalance(),
                request.getEmail(),
                request.getPhone()
        );
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/{userId}/email")
    public ResponseEntity<String> addEmail(@PathVariable long userId, @RequestParam String email) {
        userService.addEmail(userId, email);
        return ResponseEntity.ok("Successfully added email");
    }

    @PutMapping("/{userId}/email/update")
    public ResponseEntity<String> updateEmail(@PathVariable long userId,
                                              @RequestParam String oldEmail,
                                              @RequestParam String newEmail) {
        userService.updateEmail(userId, oldEmail, newEmail);
        return ResponseEntity.ok("Successfully updated email");
    }

    @DeleteMapping("/{userId}/email")
    public ResponseEntity<String> deleteEmail(@PathVariable long userId, @RequestParam String email) {
        userService.deleteEmail(userId, email);
        return ResponseEntity.ok("Successfully deleted email");
    }

    @PostMapping("/{userId}/phone")
    public ResponseEntity<String> addPhone(@PathVariable long userId, @RequestParam String phone) {
        userService.addPhone(userId, phone);
        return ResponseEntity.ok("Successfully added phone");
    }

    @PutMapping("/{userId}/phone/update")
    public ResponseEntity<String> updatePhone(@PathVariable long userId,
                                              @RequestParam String oldPhone,
                                              @RequestParam String newPhone) {
        userService.updatePhone(userId, oldPhone, newPhone);
        return ResponseEntity.ok("Successfully updated phone");
    }

    @DeleteMapping("/{userId}/phone")
    public ResponseEntity<String> deletePhone(@PathVariable long userId, @RequestParam String phone) {
        userService.deletePhone(userId, phone);
        return ResponseEntity.ok("Successfully deleted phone");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserEntity>> searchUsers(
            @RequestParam Optional<String> dateOfBirth,
            @RequestParam Optional<String> phone,
            @RequestParam Optional<String> email,
            @RequestParam Optional<String> name,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Optional<LocalDate> date = dateOfBirth.map(LocalDate::parse);

        Page<UserEntity> users = userService.searchUsers(date, phone, email, name, pageable);
        return ResponseEntity.ok(users);
    }
}
