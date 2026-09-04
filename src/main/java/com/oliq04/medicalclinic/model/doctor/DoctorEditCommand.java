package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.clinic.ClinicNameCommand;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DoctorEditCommand {
    private String firstName;
    private String lastName;
    private String specialization;
    private List<ClinicNameCommand> clinics;
}
