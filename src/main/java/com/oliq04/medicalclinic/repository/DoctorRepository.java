package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserEmail(String email);

    boolean existsByUserEmailAndClinicsName(String email, String clinicName);

    Page<Doctor> findBySpecialization(Specialization specialization, Pageable pageable);
}
