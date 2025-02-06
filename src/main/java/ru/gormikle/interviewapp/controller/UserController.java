package ru.gormikle.interviewapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gormikle.interviewapp.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}
