package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.model.Clinic;
import com.oliq04.medicalclinic.model.DomainPageRequest;
import com.oliq04.medicalclinic.model.Pageable;

import java.util.Optional;

public interface ClinicRepositoryProvider {
    Pageable<Clinic> findAll(DomainPageRequest domainPageRequest);

    Optional<Clinic> findByName(String name);

    Clinic save(Clinic clinic);

    void delete(Clinic byName);

    boolean existsByName(String name);
}
