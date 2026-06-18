package com.oliq04.medicalclinic.model.doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DoctorCommand {
    private String firstName;
    private String lastName;
    private String specialization;
    private String email;
    private String password;
}
