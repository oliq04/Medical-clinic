package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class ClinicAlreadyExistsException extends MedicalClinicException {
    public ClinicAlreadyExistsException(String message, HttpStatus status) {
        super(message, status);
    }
}
