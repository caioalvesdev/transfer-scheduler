package com.realmtech.transferscheduler.domain.service;

import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import com.realmtech.transferscheduler.domain.repository.TransferScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferScheduleService {

    private final TransferScheduleRepository repository;
    private final TransferFeeCalculator feeCalculator;

    @Transactional
    public TransferSchedule create(TransferSchedule transferSchedule) {
        BigDecimal fee = feeCalculator.calculate(transferSchedule);
        transferSchedule.setFee(fee);

        return repository.save(transferSchedule);
    }

    public Page<TransferSchedule> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
