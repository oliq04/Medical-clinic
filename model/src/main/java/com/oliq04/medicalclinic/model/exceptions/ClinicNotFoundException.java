package com.oliq04.medicalclinic.model.exceptions;

public class ClinicNotFoundException extends MedicalClinicException {
    public ClinicNotFoundException(String message, Integer status) {
        super(message, status);
    }
}
