package com.realmtech.transferscheduler.api.mapper;

import com.realmtech.transferscheduler.api.model.TransferScheduleRequest;
import com.realmtech.transferscheduler.api.model.TransferScheduleResponse;
import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransferScheduleMapperTest {

    private final TransferScheduleMapper mapper = new TransferScheduleMapper();

    @Test
    void shouldMapRequestToEntity() {
        TransferScheduleRequest request = new TransferScheduleRequest();
        request.setSourceAccount("1234567890");
        request.setDestinationAccount("0987654321");
        request.setAmount(new BigDecimal("100.00"));
        request.setTransferDate(OffsetDateTime.now().plusDays(5));

        TransferSchedule entity = mapper.toEntity(request);

        assertThat(entity.getSourceAccount()).isEqualTo(request.getSourceAccount());
        assertThat(entity.getDestinationAccount()).isEqualTo(request.getDestinationAccount());
        assertThat(entity.getAmount()).isEqualTo(request.getAmount());
        assertThat(entity.getTransferDate()).isEqualTo(request.getTransferDate());
    }

    @Test
    void shouldMapEntityToModel() {
        TransferSchedule entity = TransferSchedule.create(
                "1234567890", "0987654321", new BigDecimal("100.00"),
                new BigDecimal("12.00"), OffsetDateTime.now().plusDays(5));

        TransferScheduleResponse response = mapper.toModel(entity);

        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getSourceAccount()).isEqualTo(entity.getSourceAccount());
        assertThat(response.getDestinationAccount()).isEqualTo(entity.getDestinationAccount());
        assertThat(response.getAmount()).isEqualTo(entity.getAmount());
        assertThat(response.getFee()).isEqualTo(entity.getFee());
        assertThat(response.getTransferDate()).isEqualTo(entity.getTransferDate());
        assertThat(response.getSchedulingDate()).isEqualTo(entity.getSchedulingDate());
    }

    @Test
    void shouldMapEntityListToModelList() {
        TransferSchedule first = TransferSchedule.create(
                "1234567890", "0987654321", new BigDecimal("100.00"),
                new BigDecimal("12.00"), OffsetDateTime.now().plusDays(5));
        TransferSchedule second = TransferSchedule.create(
                "1111111111", "2222222222", new BigDecimal("200.00"),
                new BigDecimal("3.00"), OffsetDateTime.now());

        List<TransferScheduleResponse> result = mapper.toCollectionModel(List.of(first, second));

        assertThat(result)
                .extracting(TransferScheduleResponse::getId)
                .containsExactly(first.getId(), second.getId());
    }
}
