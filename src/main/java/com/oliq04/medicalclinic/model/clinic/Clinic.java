package com.oliq04.medicalclinic.model.clinic;

import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.visit.Visit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String town;
    private String postCode;
    private String address;

    @ManyToMany(mappedBy = "clinics")
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "clinic")
    private List<Visit> visits;

    public Clinic update(ClinicCommand clinic) {
        this.setName(clinic.getName());
        this.setTown(clinic.getTown());
        this.setPostCode(clinic.getPostCode());
        this.setAddress(clinic.getAddress());
        return this;
    }
}
