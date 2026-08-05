package com.realmtech.transferscheduler.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransferScheduleRequest {
    @NotBlank(message = "A conta de origem é obrigatória.")
    @Pattern(
            regexp = "\\d{10}",
            message = "A conta de origem deve possuir exatamente 10 dígitos."
    )
    private String sourceAccount;

    @NotBlank(message = "A conta de destino é obrigatória.")
    @Pattern(
            regexp = "\\d{10}",
            message = "A conta de destino deve possuir exatamente 10 dígitos."
    )
    private String destinationAccount;

    @NotNull(message = "O valor da transferência é obrigatório.")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "O valor da transferência deve ser maior que zero."
    )
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;

    @NotNull(message = "A data da transferência é obrigatória.")
    private OffsetDateTime transferDate;

    @AssertTrue(message = "A data da transferência deve ser hoje ou uma data futura.")
    private boolean isTransferDateValid() {
        if (transferDate == null) {
            return true;
        }
        return !transferDate.toLocalDate().isBefore(LocalDate.now(transferDate.getOffset()));
    }
}
