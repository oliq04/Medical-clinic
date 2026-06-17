package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAll();

    Optional<Patient> findPatientByUserEmail(String email);

    boolean existsByUserEmail(String email);
}
