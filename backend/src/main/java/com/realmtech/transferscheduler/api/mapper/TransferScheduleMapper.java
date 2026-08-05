package com.realmtech.transferscheduler.api.mapper;

import com.realmtech.transferscheduler.api.model.TransferScheduleRequest;
import com.realmtech.transferscheduler.api.model.TransferScheduleResponse;
import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import org.springframework.stereotype.Component;

@Component
public class TransferScheduleMapper {

    public TransferSchedule toEntity(TransferScheduleRequest request) {
        return TransferSchedule.create(
                request.getSourceAccount(),
                request.getDestinationAccount(),
                request.getAmount(),
                null,
                request.getTransferDate()
        );
    }

    public TransferScheduleResponse toModel(TransferSchedule transferSchedule) {
        return TransferScheduleResponse.builder()
                .id(transferSchedule.getId())
                .sourceAccount(transferSchedule.getSourceAccount())
                .destinationAccount(transferSchedule.getDestinationAccount())
                .amount(transferSchedule.getAmount())
                .fee(transferSchedule.getFee())
                .transferDate(transferSchedule.getTransferDate())
                .schedulingDate(transferSchedule.getSchedulingDate())
                .build();
    }
}
