package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.specialization.Specialization;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private List<Specialization> specializations;

    @ManyToMany
    @JoinTable(
            name = "doctor_clinic",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id")
    )
    private List<Clinic> clinics;
}
