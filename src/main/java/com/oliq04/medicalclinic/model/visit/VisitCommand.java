package com.oliq04.medicalclinic.model.visit;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class VisitCommand {
    @Future(message = "Start date of visit must be from future")
    private LocalDateTime startTime;
    @Future(message = "End date of visit can't be from past")
    private LocalDateTime endTime;
    @Email(message = "Invalid email")
    @NotBlank(message = "Email can't be blank")
    private String doctorEmail;
    private String patientEmail;
    @NotBlank(message = "Clinic name can't be empty")
    private String clinicName;
}
