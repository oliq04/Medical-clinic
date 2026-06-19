package com.oliq04.medicalclinic.model.doctor;

import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.specialization.Specialization;
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
    private List<Specialization> specialization;
    private List<Clinic> clinics;
}
