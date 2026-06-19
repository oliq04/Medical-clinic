package com.oliq04.medicalclinic.model.visit;

import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Visit {
    @Id
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Clinic clinic;
}
