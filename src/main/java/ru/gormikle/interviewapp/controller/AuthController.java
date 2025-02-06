package ru.gormikle.interviewapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.gormikle.interviewapp.dto.AuthResponseDto;
import ru.gormikle.interviewapp.dto.LoginRequestDto;
import ru.gormikle.interviewapp.dto.RefreshRequestDto;
import ru.gormikle.interviewapp.entity.UserEntity;
import ru.gormikle.interviewapp.repository.UserRepository;
import ru.gormikle.interviewapp.service.JwtTokenService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String userIdStr = authentication.getName();
        Long userId = Long.parseLong(userIdStr);
        UserEntity user = getUserEntity(userId);

        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        AuthResponseDto response = new AuthResponseDto(accessToken, refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshRequestDto refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();

        if (!jwtTokenService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        Long userId = jwtTokenService.getUserIdFromToken(refreshToken);
        UserEntity user = getUserEntity(userId);

        String newAccessToken = jwtTokenService.generateAccessToken(user);
        String newRefreshToken = jwtTokenService.generateRefreshToken(user);

        AuthResponseDto response = new AuthResponseDto(newAccessToken, newRefreshToken);
        return ResponseEntity.ok(response);
    }

    private UserEntity getUserEntity(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}






