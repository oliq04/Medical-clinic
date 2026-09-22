package com.oliq04.medicalclinic.model.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MedicalClinicException extends RuntimeException {
    private final Integer status;

    public MedicalClinicException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
