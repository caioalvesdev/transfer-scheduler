package com.realmtech.transferscheduler.api.controller;

import com.realmtech.transferscheduler.api.mapper.TransferScheduleMapper;
import com.realmtech.transferscheduler.api.model.TransferScheduleRequest;
import com.realmtech.transferscheduler.api.model.TransferScheduleResponse;
import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import com.realmtech.transferscheduler.domain.service.TransferScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transfer-schedule")
@RequiredArgsConstructor
public class TransferScheduleController {

    private final TransferScheduleMapper transferScheduleMapper;
    private final TransferScheduleService transferScheduleService;

    @PostMapping
    public ResponseEntity<TransferScheduleResponse> create(@Valid @RequestBody TransferScheduleRequest request) {
        TransferSchedule entity = transferScheduleMapper.toEntity(request);
        TransferSchedule transferSchedule = transferScheduleService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferScheduleMapper.toModel(transferSchedule));
    }

    @GetMapping
    public ResponseEntity<List<TransferScheduleResponse>> findAll() {
        List<TransferSchedule> transferSchedules = transferScheduleService.findAll();
        return ResponseEntity.ok(transferScheduleMapper.toCollectionModel(transferSchedules));
    }
}
