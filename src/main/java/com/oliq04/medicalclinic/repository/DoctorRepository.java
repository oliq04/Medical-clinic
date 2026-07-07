package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserEmail(String email);

    boolean existsByUserEmailAndClinicsName(String email, String clinicName);
}
