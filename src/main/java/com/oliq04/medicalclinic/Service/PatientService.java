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
        return patientRepository.getPatients();
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.addPatient(patient);
    }

    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void removePatientByEmail(String email) {
        patientRepository.deleteByEmail(email);
    }

    public Patient modifyPatient(String email, Patient newPatientInfo) {
        Patient patient = findByEmail(email);

        patient.setFirstName(newPatientInfo.getFirstName());
        patient.setLastName(newPatientInfo.getLastName());
        patient.setEmail(newPatientInfo.getEmail());
        patient.setPassword(newPatientInfo.getPassword());
        patient.setBirthday(newPatientInfo.getBirthday());
        patient.setIdCardNo(newPatientInfo.getIdCardNo());
        patient.setPhoneNumber(newPatientInfo.getPhoneNumber());

        return patient;
    }

    public void editPassword(String email, String password) {
        Patient patient = findByEmail(email);
        patient.setPassword(password);
    }
}