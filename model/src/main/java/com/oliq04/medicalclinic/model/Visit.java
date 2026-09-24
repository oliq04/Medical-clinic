package com.oliq04.medicalclinic.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Visit {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Patient patient;
    private Doctor doctor;
    private Clinic clinic;
}
