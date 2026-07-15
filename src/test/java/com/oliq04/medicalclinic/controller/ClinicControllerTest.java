package com.oliq04.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliq04.medicalclinic.service.ClinicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    void getClinics_CorrectData_PageOfClinics() {

    }
}
