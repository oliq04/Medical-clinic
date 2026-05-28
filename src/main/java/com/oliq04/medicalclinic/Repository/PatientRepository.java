package com.oliq04.medicalclinic.Repository;

import com.oliq04.medicalclinic.Model.Patient;
import com.oliq04.medicalclinic.Service.PatientService;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@Repository
public class PatientRepository {
    private final List<Patient> patientList;

    public PatientRepository(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public void addPatientToList(Patient patient) {
        patientList.add(patient);
    }

    public Optional<Patient> findPatientByEmail(String email) {
        return patientList.stream().filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public void deletePatientByEmail(String email) {
        patientList.remove(findPatientByEmail(email).orElseThrow());
    }

}
