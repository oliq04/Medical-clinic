package com.oliq04.medicalclinic.model.exceptions;

public class ClinicAlreadyExistsException extends MedicalClinicException {
    public ClinicAlreadyExistsException(String message, Integer status) {
        super(message, status);
    }
}
