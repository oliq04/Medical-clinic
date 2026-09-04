package com.oliq04.medicalclinic.model.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class DoctorCommand {
    @NotBlank(message = "Name can't be blank")
    private String firstName;
    @NotBlank(message = "Name can't be blank")
    private String lastName;
    @NotBlank(message = "Name can't be blank")
    private String specialization;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email can't be empty")
    private String email;
    @NotBlank(message = "Name can't be blank")
    private String password;
}
