package com.oliq04.medicalclinic.model.patient.entity;

import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.oliq04.medicalclinic.model.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime birthday;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Patient update(PatientEditCommand newPatientInfo) {
        this.setFirstName(newPatientInfo.getFirstName());
        this.setLastName(newPatientInfo.getLastName());
        this.setBirthday(newPatientInfo.getBirthday());
        this.setIdCardNo(newPatientInfo.getIdCardNo());
        this.setPhoneNumber(newPatientInfo.getPhoneNumber());
        return this;
    }
}