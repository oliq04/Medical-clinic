package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class IllegalTimeException extends MedicalClinicException {
    public IllegalTimeException(String message, HttpStatus status) {
        super(message, status);
    }
}
