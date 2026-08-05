package com.realmtech.transferscheduler.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmtech.transferscheduler.api.mapper.TransferScheduleMapper;
import com.realmtech.transferscheduler.api.model.TransferScheduleResponse;
import com.realmtech.transferscheduler.domain.model.TransferSchedule;
import com.realmtech.transferscheduler.domain.service.TransferScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferScheduleController.class)
class TransferScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferScheduleService transferScheduleService;

    @MockBean
    private TransferScheduleMapper transferScheduleMapper;

    @Test
    void shouldReturn201WhenRequestIsValid() throws Exception {
        TransferSchedule entity = TransferSchedule.create(
                "1234567890", "0987654321", new BigDecimal("100.00"), null,
                OffsetDateTime.now().plusDays(5));
        TransferScheduleResponse response = TransferScheduleResponse.builder()
                .id(entity.getId())
                .sourceAccount(entity.getSourceAccount())
                .destinationAccount(entity.getDestinationAccount())
                .amount(entity.getAmount())
                .fee(new BigDecimal("12.00"))
                .transferDate(entity.getTransferDate())
                .schedulingDate(entity.getSchedulingDate())
                .build();

        when(transferScheduleMapper.toEntity(any())).thenReturn(entity);
        when(transferScheduleService.create(entity)).thenReturn(entity);
        when(transferScheduleMapper.toModel(entity)).thenReturn(response);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("sourceAccount", "1234567890");
        requestBody.put("destinationAccount", "0987654321");
        requestBody.put("amount", new BigDecimal("100.00"));
        requestBody.put("transferDate", entity.getTransferDate());

        mockMvc.perform(post("/api/transfer-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(entity.getId().toString()))
                .andExpect(jsonPath("$.fee").value(12.00));
    }

    @Test
    void shouldReturn400WithFieldErrorsWhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/transfer-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.properties.fields.sourceAccount").exists())
                .andExpect(jsonPath("$.properties.fields.destinationAccount").exists())
                .andExpect(jsonPath("$.properties.fields.amount").exists())
                .andExpect(jsonPath("$.properties.fields.transferDate").exists());
    }

    @Test
    void shouldReturn400WhenBodyIsMalformed() throws Exception {
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("sourceAccount", "1234567890");
        requestBody.put("destinationAccount", "0987654321");
        requestBody.put("amount", "not-a-number");
        requestBody.put("transferDate", "2030-01-01T00:00:00Z");

        mockMvc.perform(post("/api/transfer-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://realmtech.com/erros/corpo-invalido"));
    }

    @Test
    void shouldReturnAllTransferSchedules() throws Exception {
        TransferSchedule entity = TransferSchedule.create(
                "1234567890", "0987654321", new BigDecimal("100.00"),
                new BigDecimal("12.00"), OffsetDateTime.now().plusDays(5));
        TransferScheduleResponse response = TransferScheduleResponse.builder()
                .id(entity.getId())
                .sourceAccount(entity.getSourceAccount())
                .destinationAccount(entity.getDestinationAccount())
                .amount(entity.getAmount())
                .fee(entity.getFee())
                .transferDate(entity.getTransferDate())
                .schedulingDate(entity.getSchedulingDate())
                .build();

        when(transferScheduleService.findAll()).thenReturn(List.of(entity));
        when(transferScheduleMapper.toCollectionModel(List.of(entity))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/transfer-schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(entity.getId().toString()));
    }
}
