package com.oliq04.medicalclinic.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.service.ClinicService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest {
    @MockitoBean
    private ClinicService clinicService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getClinics_CorrectData_PageOfClinicsReturned() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        ClinicDto clinicDtoOne = ClinicDto.builder()
                .id(1L)
                .name("ClinicOne")
                .town("Warsaw")
                .postCode("22-33")
                .address("Adress")
                .build();
        ClinicDto clinicDtoTwo = ClinicDto.builder()
                .id(2L)
                .name("ClinicTwo")
                .town("Wroclaw")
                .postCode("222-33")
                .address("Adress")
                .build();


        List<ClinicDto> clinicDtos = new ArrayList<>(List.of(clinicDtoOne, clinicDtoTwo));
        PageableDto<ClinicDto> expectedClinicDtoPageableDto = PageableDto.<ClinicDto>builder()
                .pageSize(1)
                .currentPage(0)
                .total(2)
                .totalPages(1)
                .content(clinicDtos)
                .build();
        PageImpl<ClinicDto> clinicDtosPage = new PageImpl<>(clinicDtos, pageable, 1);
        PageableDto<ClinicDto> pageableDto = PageableDto.toPageable(clinicDtos, clinicDtosPage);
        when(clinicService.getClinics(0, 1)).thenReturn(pageableDto);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/clinics")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageSize").value(1L))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedClinicDtoPageableDto)));
    }

    @Test
    void getClinic_CorrectData_ClinicReturned() throws Exception {
        ClinicDto clinicDtoOne = ClinicDto.builder()
                .id(1L)
                .name("ClinicOne")
                .town("Warsaw")
                .postCode("22-33")
                .address("Adress")
                .build();
        when(clinicService.getClinic(any())).thenReturn(clinicDtoOne);

        mockMvc.perform(MockMvcRequestBuilders.get("/clinics/{name}", "ClinicOne")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clinicDtoOne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ClinicOne"))
                .andExpect(jsonPath("$.postCode").value("22-33"))
                .andExpect(jsonPath("$.address").value("Adress"));
    }

    @Test
    void addClinic_CorrectData_AddedClinicReturned() throws Exception {
        ClinicDto clinicDtoOne = ClinicDto.builder()
                .id(1L)
                .name("ClinicOne")
                .town("Warsaw")
                .postCode("22-33")
                .address("Adress")
                .build();
        when(clinicService.addClinic(any())).thenReturn(clinicDtoOne);

        mockMvc.perform(MockMvcRequestBuilders.post("/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clinicDtoOne)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ClinicOne"))
                .andExpect(jsonPath("$.town").value("Warsaw"))
                .andExpect(jsonPath("$.postCode").value("22-33"))
                .andExpect(jsonPath("$.address").value("Adress"));
    }

    @Test
    void deleteClinic_CorrectData_DeletedClinicNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/clinics/{name}", "clinic"));
        verify(clinicService).deleteClinic("clinic");
    }

    @Test
    void editClinic_CorrectData_EditedClinicReturned() throws Exception {
        ClinicCommand clinicCommand = ClinicCommand.builder()
                .name("Clinic")
                .town("Warsaw")
                .postCode("22-33")
                .address("Adress")
                .build();

        ClinicDto clinicDto = ClinicDto.builder()
                .id(1L)
                .name("Clinic")
                .town("Warsaw")
                .postCode("22-33")
                .address("Adress")
                .build();
        when(clinicService.editClinic(any(), any())).thenReturn(clinicDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/clinics/{name}", "Clinic")
                        .content(objectMapper.writeValueAsString(clinicCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Clinic"))
                .andExpect(jsonPath("$.town").value("Warsaw"))
                .andExpect(jsonPath("$.postCode").value("22-33"))
                .andExpect(jsonPath("$.address").value("Adress"));
    }
}
