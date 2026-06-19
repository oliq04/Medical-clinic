package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.visit.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAll();
}
