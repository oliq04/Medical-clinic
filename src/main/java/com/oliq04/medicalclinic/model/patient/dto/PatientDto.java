package com.oliq04.medicalclinic.model.patient.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PatientDto {
    private Long id;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime birthday;
}
