package com.oliq04.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class VisitControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private VisitService visitService;

    @Test
    void createVisit_CorrectData_VisitReturned() throws Exception {
        VisitCommand visitCommand = VisitCommand.builder()
                .startTime(LocalDateTime.of(2027, 5, 5, 2, 0))
                .endTime(LocalDateTime.of(2027, 5, 5, 2, 15))
                .doctorEmail("doctoremail@wp.pl")
                .patientEmail("patientemail@wp.pl")
                .clinicName("Clinic")
                .build();

        VisitDto visitDto = VisitDto.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2027, 5, 5, 2, 0))
                .endDate(LocalDateTime.of(2027, 5, 5, 2, 15))
                .patient(null)
                .doctor(null)
                .clinic(null)
                .build();

        when(visitService.createVisit(any())).thenReturn(visitDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/visit")
                        .content(objectMapper.writeValueAsString(visitCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.startDate").value("2027-05-05T02:00:00"))
                .andExpect(jsonPath("$.endDate").value("2027-05-05T02:15:00"));

    }

    @Test
    void getVisits_CorrectData_PageOfVisitsReturned() throws Exception {
        Pageable pageable = PageRequest.of(0, 1);
        VisitDto visitDto = VisitDto.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2027, 5, 5, 2, 0))
                .endDate(LocalDateTime.of(2027, 5, 5, 2, 15))
                .patient(null)
                .doctor(null)
                .clinic(null)
                .build();

        List<VisitDto> visitDtos = new ArrayList<>(List.of(visitDto));
        PageImpl<VisitDto> visitDtoPage = new PageImpl<>(visitDtos, pageable, 1);
        PageableDto<VisitDto> pageableDto = PageableDto.toPageable(visitDtos, visitDtoPage);
        when(visitService.getVisits(0, 1)).thenReturn(pageableDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/visit")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].startDate").value("2027-05-05T02:00:00"))
                .andExpect(jsonPath("$.content[0].endDate").value("2027-05-05T02:15:00"));
    }

    @Test
    void assignPatient_CorrectData_VisitDtoReturned() throws Exception {
        PatientDto patientDto = PatientDto.builder()
                .id(1L)
                .idCardNo(123L)
                .firstName("First")
                .lastName("Last")
                .phoneNumber("333")
                .birthday(LocalDateTime.of(2025,4,2,1,0))
                .build();
        VisitDto visitDto = VisitDto.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2027, 5, 5, 2, 0))
                .endDate(LocalDateTime.of(2027, 5, 5, 2, 15))
                .patient(patientDto)
                .doctor(null)
                .clinic(null)
                .build();

        when(visitService.assignPatient(1L,1L)).thenReturn(visitDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/visit/patient")
                .param("patientId", "1")
                .param("visitId", "1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startDate").value("2027-05-05T02:00:00"))
                .andExpect(jsonPath("$.endDate").value("2027-05-05T02:15:00"))
                .andExpect(jsonPath("$.patient.id").value(1))
                .andExpect(jsonPath("$.patient.idCardNo").value(123))
                .andExpect(jsonPath("$.patient.firstName").value("First"))
                .andExpect(jsonPath("$.patient.lastName").value("Last"));
    }
}
