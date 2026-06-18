package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class SpecializationNotFoundException extends MedicalClinicException {
    public SpecializationNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
