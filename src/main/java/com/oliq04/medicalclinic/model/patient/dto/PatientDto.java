package com.oliq04.medicalclinic.model.patient.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PatientDto {

    private Long id;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime birthday;
}
