package com.oliq04.medicalclinic.model.patient.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PatientDto {

    @NotNull(message = "ID of card can't be null")
    @Positive(message = "Value of id card must be greater than 0")
    private Long idCardNo;
    @NotBlank(message = "Name can't be empty")
    private String firstName;
    @NotBlank(message = "Last name can't be empty")
    private String lastName;
    private String phoneNumber;
    @Past(message = "Date of birth can't be from future")
    private LocalDateTime birthday;
}
