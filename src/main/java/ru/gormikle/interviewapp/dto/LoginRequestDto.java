package ru.gormikle.interviewapp.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    String login;
    String password;
}
