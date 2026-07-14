package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<ClinicDto> clinics;
}
