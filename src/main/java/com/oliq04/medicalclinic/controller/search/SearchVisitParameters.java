package com.oliq04.medicalclinic.controller.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@NoArgsConstructor
@Getter
public class SearchVisitParameters {
    private int page;
    private int size;
    private Long doctorId;
    private Long patientId;
    private LocalDate from;
    private LocalDate to;
    private String specialization;
    private Boolean availableOnly;
}
