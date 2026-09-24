package com.oliq04.medicalclinic.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Doctor {

    private Long id;
    private String firstName;
    private String lastName;
    private Specialization specialization;
    private User user;
    private List<Visit> visits;
    private List<Clinic> clinics;

    public Doctor update(Doctor doctorEditCommand, List<Clinic> clinics) {
        this.setFirstName(doctorEditCommand.getFirstName());
        this.setLastName(doctorEditCommand.getLastName());
        this.setClinics(clinics);
        this.setSpecialization(Specialization.valueOf(doctorEditCommand.getSpecialization().toUpperCase()));
        return this;
    }
}
