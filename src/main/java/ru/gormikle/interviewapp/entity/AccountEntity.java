package ru.gormikle.interviewapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name="account", schema="interview_app")
@Data
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private UserEntity userEntity;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    @Min(value = 0, message = "Balance cannot be negative")
    private BigDecimal balance;

    @Column(name = "initial_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal initialBalance;

    // Оптимистичная блокировка
    @Version
    private Long version;

    public void setBalance(BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance can't be negative");
        }
        this.balance = newBalance;
    }
}
