package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalClinicException {
    public DoctorNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
