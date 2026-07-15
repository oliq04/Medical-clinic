package com.oliq04.medicalclinic.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.service.PatientService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PatientService patientService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getByEmail_CorrectData_SpecificPatient() throws Exception {
        //given
        PatientDto patientDto = PatientDto.builder()
                .id(2L)
                .idCardNo(333L)
                .firstName("Patient")
                .lastName("Test")
                .build();
        when(patientService.findByEmail("email@wp.pl")).thenReturn(patientDto);
        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{email}", "email@wp.pl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Patient"))
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void addPatient_CorrectData_AddedPatient() throws Exception {
        //given
        PatientDto patientDto = PatientDto.builder()
                .id(2L)
                .idCardNo(333L)
                .firstName("Patient")
                .lastName("Test")
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .idCardNo(333L)
                .firstName("Patient")
                .lastName("Test")
                .build();

        PatientCommand patientCommand = PatientCommand.builder()
                .email("email@wp.pl")
                .password("1234567")
                .idCardNo(333L)
                .firstName("O")
                .lastName("Z")
                .phoneNumber("444")
                .build();

        when(patientService.addPatient(any())).thenReturn(patientDto);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCardNo").value(333L))
                .andExpect(jsonPath("$.firstName").value("Patient"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }

    @Test
    void editPatient_CorrectData_EditedPatientDto() throws Exception {
        //given
        PatientEditCommand patientEditCommand = PatientEditCommand.builder()
                .idCardNo(123L)
                .firstName("Patient")
                .lastName("Test")
                .phoneNumber("444")
                .birthday(LocalDateTime.of(2024, 6, 5, 5, 10))
                .build();
        PatientDto patientDto = PatientDto.builder()
                .id(2L)
                .idCardNo(123L)
                .firstName("Patient")
                .lastName("Test")
                .phoneNumber("444")
                .birthday(LocalDateTime.of(2024, 6, 5, 5, 10))
                .build();
        when(patientService.modifyPatient("email@wp.pl", patientEditCommand)).thenReturn(patientDto);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", "email@wp.pl")
                        .content(objectMapper.writeValueAsString(patientEditCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCardNo").value(123L))
                .andExpect(jsonPath("$.firstName").value("Patient"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.phoneNumber").value("444"));
    }

    @Test
    void deleteByEmail_CorrectData_DeletePatient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{email}", "email@wp.pl"))
                .andExpect(status().isNoContent());
        verify(patientService).removeByEmail("email@wp.pl");
    }

    @Test
    void getPatients_CorrectData_PageOfPatients() throws Exception {
        //given
        PatientDto patientDto = PatientDto.builder()
                .id(1L)
                .idCardNo(222L)
                .firstName("Patient")
                .lastName("PatientLast")
                .phoneNumber("4444")
                .birthday(LocalDateTime.of(2024, 5, 5, 5, 0))
                .build();
        Pageable pageable = PageRequest.of(0, 1);
        List<PatientDto> patientList = new ArrayList<>(List.of(patientDto));
        PageImpl<PatientDto> patientDtoPage = new PageImpl<>(patientList, pageable, 1);
        PageableDto<PatientDto> pageableDto = PageableDto.toPageable(patientList, patientDtoPage);
        PageableDto<PatientDto> expectedPageableDto = PageableDto.<PatientDto>builder()
                .pageSize(1)
                .currentPage(0)
                .total(1)
                .totalPages(1)
                .content(patientList)
                .build();

        when(patientService.getPatients(0, 1)).thenReturn(pageableDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/patients", "page", "size")
                        .param("page", "0")
                        .param("size", "1"))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPageableDto)))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
}
