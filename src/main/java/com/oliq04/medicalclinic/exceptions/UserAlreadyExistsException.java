package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends MedicalClinicException {
    public UserAlreadyExistsException(String message, HttpStatus status) {
        super(message, status);
    }
}
