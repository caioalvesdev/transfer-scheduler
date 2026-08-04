package com.realmtech.transferscheduler.domain.service;

import com.realmtech.transferscheduler.domain.exception.DomainException;
import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransferFeeCalculatorTest {

    private static final BigDecimal AMOUNT = new BigDecimal("1000.00");

    private final TransferFeeCalculator calculator = new TransferFeeCalculator();

    @Test
    void shouldApplyFixedFeePlusPercentageWhenTransferIsSamDay() {
        TransferSchedule transfer = buildTransfer(AMOUNT, 0);

        BigDecimal expected = new BigDecimal("3.00")
                .add(AMOUNT.multiply(new BigDecimal("0.025")))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, calculator.calculate(transfer));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 5, 10})
    void shouldApplyFixedFeeWhenTransferIsWithinOneToTenDays(long days) {
        TransferSchedule transfer = buildTransfer(AMOUNT, days);

        assertEquals(new BigDecimal("12.00"), calculator.calculate(transfer));
    }

    @ParameterizedTest
    @CsvSource({
            "11, 0.082",
            "20, 0.082",
            "21, 0.069",
            "30, 0.069",
            "31, 0.047",
            "40, 0.047",
            "41, 0.017",
            "50, 0.017"
    })
    void shouldApplyPercentageFeeAccordingToDaysRange(long days, String percentage) {
        TransferSchedule transfer = buildTransfer(AMOUNT, days);

        BigDecimal expected = AMOUNT.multiply(new BigDecimal(percentage))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, calculator.calculate(transfer));
    }

    @Test
    void shouldThrowWhenTransferIsMoreThanFiftyDaysAhead() {
        TransferSchedule transfer = buildTransfer(AMOUNT, 51);

        assertThrows(DomainException.class, () -> calculator.calculate(transfer));
    }

    @Test
    void shouldThrowWhenTransferIsNull() {
        assertThrows(DomainException.class, () -> calculator.calculate(null));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        TransferSchedule transfer = buildTransfer(null, 5);

        assertThrows(DomainException.class, () -> calculator.calculate(transfer));
    }


    private TransferSchedule buildTransfer(BigDecimal amount, long daysFromNow) {
        return TransferSchedule.create(
                "1234567890",
                "0987654321",
                amount,
                BigDecimal.ZERO,
                OffsetDateTime.now().plusDays(daysFromNow));
    }
}
