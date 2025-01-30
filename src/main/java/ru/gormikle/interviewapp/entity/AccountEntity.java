package ru.gormikle.interviewapp.entity;

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
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity userEntity;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    @Min(value = 0, message = "Balance cannot be negative")
    private BigDecimal balance;
}
