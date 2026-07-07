package com.oliq04.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;

public class DoctorAlreadyAssignedException extends MedicalClinicException {
    public DoctorAlreadyAssignedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
