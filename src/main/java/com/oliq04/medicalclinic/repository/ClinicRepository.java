package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByName(String name);

    boolean existsByName(String name);
}
