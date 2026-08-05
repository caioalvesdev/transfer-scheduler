package com.realmtech.transferscheduler.domain.repository;

import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferScheduleRepository extends JpaRepository<TransferSchedule, UUID> {
}
