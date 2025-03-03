package ru.gormikle.interviewapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gormikle.interviewapp.dto.TransferRequestDto;
import ru.gormikle.interviewapp.service.TransferService;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            Authentication authentication,
            @Valid @RequestBody TransferRequestDto transferRequest){

        Long senderId = Long.parseLong(authentication.getName());
        transferService.transferFunds(senderId, transferRequest.getRecipientId(), transferRequest.getAmount());
        return ResponseEntity.ok("Transferred successfully");
    }
}
