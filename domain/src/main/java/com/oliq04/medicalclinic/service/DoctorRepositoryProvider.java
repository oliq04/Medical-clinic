package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.model.Doctor;
import java.util.Optional;

public interface DoctorRepositoryProvider {
    Optional<Doctor> findByUserEmail(String email);
    Doctor save(Doctor doctor);
}
