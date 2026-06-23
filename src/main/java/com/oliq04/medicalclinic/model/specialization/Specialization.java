package com.oliq04.medicalclinic.model.specialization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor

public enum Specialization {
    CARDIOLOGY("cardiology"),
    STOMATOLOGY("stomatology"),
    GASTROLOGY("gastrology");

    private final String specializationName;
}
