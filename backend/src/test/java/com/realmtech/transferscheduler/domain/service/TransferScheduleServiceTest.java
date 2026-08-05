package com.realmtech.transferscheduler.domain.service;

import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import com.realmtech.transferscheduler.domain.repository.TransferScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferScheduleServiceTest {

    @Mock
    private TransferScheduleRepository repository;

    @Mock
    private TransferFeeCalculator feeCalculator;

    @InjectMocks
    private TransferScheduleService service;

    @Test
    void shouldCalculateFeeAndSaveWhenCreating() {
        TransferSchedule transferSchedule = TransferSchedule.create(
                "1234567890", "0987654321", new BigDecimal("100.00"), null,
                OffsetDateTime.now().plusDays(5));
        BigDecimal fee = new BigDecimal("12.00");

        when(feeCalculator.calculate(transferSchedule)).thenReturn(fee);
        when(repository.save(transferSchedule)).thenReturn(transferSchedule);

        TransferSchedule result = service.create(transferSchedule);

        assertThat(result.getFee()).isEqualTo(fee);
        verify(repository).save(transferSchedule);
    }

    @Test
    void shouldReturnAllTransferSchedules() {
        TransferSchedule transferSchedule = TransferSchedule.create(
                "1234567890", "0987654321", new BigDecimal("100.00"), null,
                OffsetDateTime.now().plusDays(5));

        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(transferSchedule)));

        Page<TransferSchedule> result = service.findAll(pageable);

        assertThat(result).containsExactly(transferSchedule);
    }
}
