package ru.gormikle.interviewapp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    @NotNull(message = "Recipient ID can't be null")
    private Long recipientId;

    private BigDecimal amount;
}
