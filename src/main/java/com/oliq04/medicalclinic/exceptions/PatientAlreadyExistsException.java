package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class PatientAlreadyExistsException extends MedicalClinicException {
    public PatientAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
