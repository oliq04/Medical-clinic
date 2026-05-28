package com.oliq04.medicalclinic.Service;

import com.oliq04.medicalclinic.Model.Patient;
import com.oliq04.medicalclinic.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

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

    public void modifyPatient(String email, Patient newPatientInfo) {
        Patient patient = findPatientByEmail(email);
        patient = newPatientInfo;
    }

    public Patient editPassword(String email, String password) {
        Patient patient = findPatientByEmail(email);
        patient.setPassword(password);
        return patient;
    }
}