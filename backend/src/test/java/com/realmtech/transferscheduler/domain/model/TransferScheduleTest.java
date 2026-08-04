package com.realmtech.transferscheduler.domain.model;

import com.realmtech.transferscheduler.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransferScheduleTest {

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal FEE = BigDecimal.valueOf(2);
    private static final OffsetDateTime TRANSFER_DATE = OffsetDateTime.now().plusDays(1);

    @Test
    void shouldCreateScheduleWhenAccountsAreValid() {
        TransferSchedule schedule = TransferSchedule.create("1234567890", "0987654321", AMOUNT, FEE, TRANSFER_DATE);

        assertNotNull(schedule.getId());
        assertEquals("1234567890", schedule.getSourceAccount());
        assertEquals("0987654321", schedule.getDestinationAccount());
        assertEquals(AMOUNT, schedule.getAmount());
        assertEquals(FEE, schedule.getFee());
        assertEquals(TRANSFER_DATE, schedule.getTransferDate());
        assertNotNull(schedule.getSchedulingDate());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "123", "12345678901", "abcdefghij"})
    void shouldThrowWhenSourceAccountIsInvalid(String invalidAccount) {
        assertThrows(DomainException.class,
                () -> TransferSchedule.create(invalidAccount, "0987654321", AMOUNT, FEE, TRANSFER_DATE));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "123", "12345678901", "abcdefghij"})
    void shouldThrowWhenDestinationAccountIsInvalid(String invalidAccount) {
        assertThrows(DomainException.class,
                () -> TransferSchedule.create("1234567890", invalidAccount, AMOUNT, FEE, TRANSFER_DATE));
    }
}
