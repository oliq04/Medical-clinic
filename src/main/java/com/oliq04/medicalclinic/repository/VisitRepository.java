package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.visit.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    @Query("SELECT v FROM Visit v WHERE v.startDate < :endDate AND v.endDate > :startDate AND v.doctor.id = :doctorId")
    List<Visit> findOverlappingVisits(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("doctorId") Long doctorId
    );

    Optional<Visit> findVisitById(Long id);

    Page<Visit> findVisitsByPatientId(Long patientId, Pageable pageable);

    Page<Visit> findVisitsByDoctorId(Long doctorId, Pageable pageable);

    Page<Visit> findVisitsByDoctorIdAndPatientIsNull(Long id, Pageable pageable);
}
