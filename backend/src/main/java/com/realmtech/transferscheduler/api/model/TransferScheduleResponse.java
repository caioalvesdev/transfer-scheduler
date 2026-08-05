package com.realmtech.transferscheduler.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TransferScheduleResponse {
    private UUID id;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private BigDecimal fee;
    private OffsetDateTime transferDate;
    private OffsetDateTime schedulingDate;
}
