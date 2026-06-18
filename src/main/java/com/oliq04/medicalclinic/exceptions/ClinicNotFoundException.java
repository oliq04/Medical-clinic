package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class ClinicNotFoundException extends MedicalClinicException {
    public ClinicNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
