package com.oliq04.medicalclinic.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Clinic {

    private Long id;

    private String name;
    private String town;
    private String postCode;
    private String address;
    private List<Doctor> doctors;
    private List<Visit> visits;

    public Clinic update(Clinic clinic) {
        this.setName(clinic.getName());
        this.setTown(clinic.getTown());
        this.setPostCode(clinic.getPostCode());
        this.setAddress(clinic.getAddress());
        return this;
    }
}
