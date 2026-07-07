package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class VisitNotFoundException extends MedicalClinicException {
    public VisitNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
