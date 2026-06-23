package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.visit.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAll();

    @Query("SELECT v FROM Visit v WHERE v.startDate < :endDate AND v.endDate > :startDate")
    List<Visit> findOverlappingVisits(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Visit> findVisitById(Long id);
}
