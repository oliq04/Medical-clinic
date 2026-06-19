package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicNameCommand;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.specialization.SpecializationNameCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorEditCommand {
    private String firstName;
    private String lastName;
    private List<SpecializationNameCommand> specialization;
    private List<ClinicNameCommand> clinics;
}
