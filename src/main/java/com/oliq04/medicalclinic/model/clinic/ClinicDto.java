package com.oliq04.medicalclinic.model.clinic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ClinicDto {
    private Long id;
    private String name;
    private String town;
    private String postCode;
    private String address;
}
