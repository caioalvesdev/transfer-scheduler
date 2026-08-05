package com.realmtech.transferscheduler.domain.service;

import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import com.realmtech.transferscheduler.domain.repository.TransferScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

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

    public List<TransferSchedule> findAll() {
        return repository.findAll();
    }
}
