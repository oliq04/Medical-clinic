package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.model.Clinic;

import java.util.List;
import java.util.Optional;

public interface ClinicRepositoryProvider {
    List<Clinic> findAll();

    Optional<Object> findByName(String name);

    Clinic save(Clinic clinic);

    void delete(Clinic byName);

    boolean existsByName(String name);
}
