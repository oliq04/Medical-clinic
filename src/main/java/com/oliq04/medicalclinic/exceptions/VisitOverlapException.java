package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class VisitOverlapException extends MedicalClinicException {
    public VisitOverlapException(String message, HttpStatus status) {
        super(message, status);
    }
}
