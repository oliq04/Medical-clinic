package com.oliq04.medicalclinic.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MedicalClinicException extends RuntimeException {
    private final HttpStatus status;

    public MedicalClinicException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
