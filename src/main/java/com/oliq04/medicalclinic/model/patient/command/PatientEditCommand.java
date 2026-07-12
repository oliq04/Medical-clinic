package com.oliq04.medicalclinic.model.patient.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class PatientEditCommand {
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
