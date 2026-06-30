package com.oliq04.medicalclinic.model.specialization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum Specialization {
    CARDIOLOGY("cardiology"),
    STOMATOLOGY("stomatology"),
    GASTROLOGY("gastrology");

    private final String specializationName;
}
