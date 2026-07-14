package com.oliq04.medicalclinic.model.visit;

import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VisitDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PatientDto patient;
    private DoctorDto doctor;
    private ClinicDto clinic;
}
