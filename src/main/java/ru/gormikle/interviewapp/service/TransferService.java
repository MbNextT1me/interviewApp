package ru.gormikle.interviewapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gormikle.interviewapp.entity.AccountEntity;
import ru.gormikle.interviewapp.repository.AccountRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {


    private final AccountRepository accountRepository;

    @Transactional
    public void transferFunds(Long senderId, Long recipientId, BigDecimal amount) {
        if (senderId.equals(recipientId)) {
            throw new IllegalArgumentException("You can't transfer money to yourself!");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        AccountEntity sender = accountRepository.findByIdForUpdate(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        AccountEntity recipient = accountRepository.findByIdForUpdate(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        if (sender.getBalance().compareTo(amount) <= 0) {
            throw new RuntimeException("You don't have enough money, bro:/");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(recipient);
    }
}
