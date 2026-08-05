package com.realmtech.transferscheduler.domain.model;

import com.realmtech.transferscheduler.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "transfer_schedules")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransferSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "source_account", nullable = false, length = 10)
    private String sourceAccount;

    @Column(name = "destination_account", nullable = false, length = 10)
    private String destinationAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Setter
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false)
    private OffsetDateTime transferDate;

    @Column(nullable = false)
    private OffsetDateTime schedulingDate;

    public static TransferSchedule create(String sourceAccount, String destinationAccount, BigDecimal amount, BigDecimal fee, OffsetDateTime transferDate) {
        validateAccount(sourceAccount);
        validateAccount(destinationAccount);
        TransferSchedule transferSchedule = new TransferSchedule();
        transferSchedule.id = UUID.randomUUID();
        transferSchedule.sourceAccount = sourceAccount;
        transferSchedule.destinationAccount = destinationAccount;
        transferSchedule.amount = amount;
        transferSchedule.fee = fee;
        transferSchedule.transferDate = transferDate;
        transferSchedule.schedulingDate = OffsetDateTime.now();
        return transferSchedule;
    }

    private static void validateAccount(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Account cannot be null or empty.");
        }
        if (!value.matches("\\d{10}")) {
            throw new DomainException("Account must be a 10-digit number.");
        }
    }
}
