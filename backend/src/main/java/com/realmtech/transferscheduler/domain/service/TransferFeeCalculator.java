package com.realmtech.transferscheduler.domain.service;

import com.realmtech.transferscheduler.domain.exception.DomainException;
import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Component
public class TransferFeeCalculator {

    private static final BigDecimal SAME_DAY_FIXED_FEE =
            new BigDecimal("3.00");

    private static final BigDecimal SAME_DAY_PERCENTAGE =
            new BigDecimal("0.025");

    private static final BigDecimal ONE_TO_TEN_DAYS_FIXED_FEE =
            new BigDecimal("12.00");

    private static final BigDecimal ELEVEN_TO_TWENTY_DAYS_PERCENTAGE =
            new BigDecimal("0.082");

    private static final BigDecimal TWENTY_ONE_TO_THIRTY_DAYS_PERCENTAGE =
            new BigDecimal("0.069");

    private static final BigDecimal THIRTY_ONE_TO_FORTY_DAYS_PERCENTAGE =
            new BigDecimal("0.047");

    private static final BigDecimal FORTY_ONE_TO_FIFTY_DAYS_PERCENTAGE =
            new BigDecimal("0.017");

    public BigDecimal calculate(TransferSchedule transfer) {
        validateTransfer(transfer);

        long days = ChronoUnit.DAYS.between(
                transfer.getSchedulingDate().toLocalDate(),
                transfer.getTransferDate().toLocalDate()
        );

        BigDecimal fee;

        // TODO: Refatorar essa lógica para torná-la mais legível
        if (days == 0) {
            fee = SAME_DAY_FIXED_FEE.add(
                    transfer.getAmount().multiply(SAME_DAY_PERCENTAGE)
            );
        } else if (days <= 10) {
            fee = ONE_TO_TEN_DAYS_FIXED_FEE;
        } else if (days <= 20) {
            fee = calculatePercentage(
                    transfer.getAmount(),
                    ELEVEN_TO_TWENTY_DAYS_PERCENTAGE
            );
        } else if (days <= 30) {
            fee = calculatePercentage(
                    transfer.getAmount(),
                    TWENTY_ONE_TO_THIRTY_DAYS_PERCENTAGE
            );
        } else if (days <= 40) {
            fee = calculatePercentage(
                    transfer.getAmount(),
                    THIRTY_ONE_TO_FORTY_DAYS_PERCENTAGE
            );
        } else if (days <= 50) {
            fee = calculatePercentage(
                    transfer.getAmount(),
                    FORTY_ONE_TO_FIFTY_DAYS_PERCENTAGE
            );
        } else {
            throw new DomainException(
                    "Não existe taxa aplicável para transferências acima de 50 dias."
            );
        }

        return fee.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePercentage(
            BigDecimal amount,
            BigDecimal percentage
    ) {
        return amount.multiply(percentage);
    }

    // TODO: Reavaliar a necessidade de validação aqui, visto que a validação já ocorre no controller
    private void validateTransfer(TransferSchedule transfer) {
        if (transfer == null) {
            throw new DomainException("A transferência não pode ser nula.");
        }

        if (transfer.getAmount() == null) {
            throw new DomainException("O valor da transferência é obrigatório.");
        }

        if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("O valor da transferência deve ser maior que zero.");
        }

        if (transfer.getSchedulingDate() == null) {
            throw new DomainException("A data de agendamento é obrigatória.");
        }

        if (transfer.getTransferDate() == null) {
            throw new DomainException("A data da transferência é obrigatória.");
        }

        if (transfer.getTransferDate()
                .toLocalDate()
                .isBefore(transfer.getSchedulingDate().toLocalDate())) {
            throw new DomainException(
                    "A data da transferência não pode ser anterior à data de agendamento."
            );
        }
    }
}
