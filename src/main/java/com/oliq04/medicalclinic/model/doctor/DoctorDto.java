package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<ClinicDto> clinics;
}
