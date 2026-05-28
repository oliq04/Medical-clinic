package com.oliq04.medicalclinic.Service;

import com.oliq04.medicalclinic.Model.Patient;
import com.oliq04.medicalclinic.Repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatients() {
        return patientRepository.getPatientList();
    }

    public void addPatient(Patient patient) {
        patientRepository.addPatientToList(patient);
    }

    public Patient findPatientByEmail(String email) {
        return patientRepository.findPatientByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void removePatientByEmail(String email) {
        patientRepository.deletePatientByEmail(email);
    }

//    public void modifyPatient(String email, Patient newPatientInfo) {
//        Patient patient = findPatientByEmail(email);
//
//        if (newPatientInfo.getFirstName() != null) {
//            patient.setFirstName(newPatientInfo.getFirstName());
//        }
//
//        if (newPatientInfo.getLastName() != null) {
//            patient.setLastName(newPatientInfo.getLastName());
//        }
//    }

    public void modifyPatient(String email, Patient newPatientInfo) {
        Patient patient = findPatientByEmail(email);
        patient = newPatientInfo;
    }
}
