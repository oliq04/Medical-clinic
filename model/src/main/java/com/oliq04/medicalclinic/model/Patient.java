package com.oliq04.medicalclinic.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Patient {

    private Long id;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime birthday;
    private User user;
    private List<Visit> visits;

    public Patient update(PatientEditCommand newPatientInfo) {
        this.setFirstName(newPatientInfo.getFirstName());
        this.setLastName(newPatientInfo.getLastName());
        this.setBirthday(newPatientInfo.getBirthday());
        this.setIdCardNo(newPatientInfo.getIdCardNo());
        this.setPhoneNumber(newPatientInfo.getPhoneNumber());
        return this;
    }
}
