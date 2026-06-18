package com.oliq04.medicalclinic.model.clinic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClinicDto {
    private String name;
    private String town;
    private String postCode;
    private String address;
}
