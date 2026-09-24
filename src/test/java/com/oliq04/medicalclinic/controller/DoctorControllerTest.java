package com.oliq04.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.doctor.DoctorEditCommand;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.service.DoctorService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {
    @MockitoBean
    private DoctorService doctorService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getDoctors_CorrectData_PageOfDoctorsReturned() throws Exception {

        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .firstName("name")
                .lastName("lastName")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .clinics(new ArrayList<>())
                .build();

        List<DoctorDto> doctorDtoList = new ArrayList<>(List.of(doctorDto));
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<DoctorDto> pageImplDoctors = new PageImpl<>(doctorDtoList, pageable, 1);
        PageableDto<DoctorDto> pageOfDoctors = PageableDto.toPageable(doctorDtoList, pageImplDoctors);
        PageableDto<DoctorDto> expectedPage = PageableDto.<DoctorDto>builder()
                .pageSize(1)
                .currentPage(0)
                .total(1)
                .totalPages(1)
                .content(doctorDtoList)
                .build();
        when(doctorService.getDoctors(0, 1, null)).thenReturn(pageOfDoctors);
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    void getDoctorByEmail_CorrectData_DoctorReturned() throws Exception {
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("lastName")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .clinics(new ArrayList<>())
                .build();
        when(doctorService.getDoctor(any())).thenReturn(doctorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{email}", "email@wp.pl"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Doctor"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.specialization").value("cardiology"));
    }

    @Test
    void assignToClinic_CorrectData_AssignedDoctorReturned() throws Exception {
        ClinicDto clinicDto = ClinicDto.builder()
                .id(1L)
                .name("Clinic")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .build();
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("lastName")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .clinics(new ArrayList<>(List.of(clinicDto)))
                .build();


        when(doctorService.assignToClinicByEmail(any(), any())).thenReturn(doctorDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{email}", "email@wp.pl")
                        .param("clinicName", "Clinic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Doctor"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.specialization").value("cardiology"))
                .andExpect(jsonPath("$.clinics[0].id").value(1))
                .andExpect(jsonPath("$.clinics[0].name").value("Clinic"))
                .andExpect(jsonPath("$.clinics[0].town").value("Warsaw"))
                .andExpect(jsonPath("$.clinics[0].postCode").value("22-33"))
                .andExpect(jsonPath("$.clinics[0].address").value("Address"));
    }

    @Test
    void addDoctor_CorrectData_AddedDoctorReturned() throws Exception {
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("Doctor")
                .lastName("Oktor")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .email("email@wp.pl")
                .password("12345")
                .build();
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oktor")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .clinics(new ArrayList<>())
                .build();

        when(doctorService.addDoctor(any())).thenReturn(doctorDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Doctor"))
                .andExpect(jsonPath("$.lastName").value("Oktor"))
                .andExpect(jsonPath("$.specialization").value("cardiology"));
    }

    @Test
    void editDoctor_CorrectData_EditedUserReturned() throws Exception {
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oktor")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .clinics(new ArrayList<>())
                .build();

        DoctorEditCommand doctorEditCommand = DoctorEditCommand.builder()
                .firstName("Doctor")
                .lastName("Oktor")
                .specialization(Specialization.CARDIOLOGY.getSpecializationName())
                .clinics(new ArrayList<>())
                .build();
        when(doctorService.editDoctor(any(), any())).thenReturn(doctorDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{email}", "email@wp.pl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorEditCommand)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Doctor"))
                .andExpect(jsonPath("$.lastName").value("Oktor"))
                .andExpect(jsonPath("$.specialization").value("cardiology"));
    }

    @Test
    void deleteDoctor_CorrectData_NoContentReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{email}", "email@wp.pl"))
                .andExpect(status().isNoContent());
        verify(doctorService).deleteDoctor("email@wp.pl");
    }
}
