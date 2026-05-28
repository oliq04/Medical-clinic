package com.oliq04.medicalclinic.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Patient {
    private String email;
    private String password;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime birthday;
}




