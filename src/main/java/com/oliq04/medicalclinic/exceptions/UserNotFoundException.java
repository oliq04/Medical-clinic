package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MedicalClinicException {

    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
