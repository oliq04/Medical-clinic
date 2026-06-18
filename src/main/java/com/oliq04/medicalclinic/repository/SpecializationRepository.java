package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.specialization.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findBySpecializationName(String specializationName);
}
