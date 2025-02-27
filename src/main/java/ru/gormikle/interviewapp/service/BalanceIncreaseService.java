package ru.gormikle.interviewapp.service;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.gormikle.interviewapp.entity.AccountEntity;
import ru.gormikle.interviewapp.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceIncreaseService {

    private final AccountRepository accountRepository;

    @Scheduled(fixedRate = 30_000)
    public void increaseBalance() {
        List<AccountEntity> accounts = accountRepository.findAll();
        for (AccountEntity account : accounts) {
            BigDecimal initialBalance = account.getInitialBalance();
            BigDecimal currentBalance = account.getBalance();
            BigDecimal maxBalance = initialBalance.multiply(new BigDecimal("2.07"));

            if (currentBalance.compareTo(maxBalance) < 0) {
                BigDecimal newBalance = currentBalance.multiply(new BigDecimal("1.10"));
                // Зададим ограничение, при привышении макс. баланса, чтобы прировнять к 207%
                if (newBalance.compareTo(maxBalance) > 0) {
                    account.setBalance(maxBalance);
                }
                account.setBalance(newBalance);
                accountRepository.save(account);
            }
        }
    }
}
