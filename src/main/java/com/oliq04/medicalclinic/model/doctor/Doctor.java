package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.visit.Visit;
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
    @Enumerated(value = EnumType.STRING)
    private Specialization specialization;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "doctor")
    private List<Visit> visits;

    @ManyToMany
    @JoinTable(
            name = "doctor_clinic",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id")
    )
    private List<Clinic> clinics;

    public Doctor update(DoctorEditCommand doctorEditCommand, List<Clinic> clinics) {
        this.setFirstName(doctorEditCommand.getFirstName());
        this.setLastName(doctorEditCommand.getLastName());
        this.setClinics(clinics);
        this.setSpecialization(Specialization.valueOf(doctorEditCommand.getSpecialization().toUpperCase()));
        return this;
    }
}
