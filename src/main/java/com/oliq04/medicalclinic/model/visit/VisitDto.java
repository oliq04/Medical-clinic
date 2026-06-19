package com.oliq04.medicalclinic.model.visit;

import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VisitDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Patient patient;
    private Doctor doctor;
}
