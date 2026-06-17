package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.Specialization;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Doctor {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;

    @OneToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "doctor_specialization",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    List<Specialization> specializations;

    @ManyToMany
    @JoinTable(
            name = "doctor_clinic",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id")
    )
    List<Clinic> clinics;
}
