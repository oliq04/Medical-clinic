package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.visit.Visit;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class VisitSpecification {

    public static Specification<Visit> hasNoPatient() {
        return (root, query, cb) -> cb.isNull(root.get("patient"));
    }

    public static Specification<Visit> hasSpecialization(Specialization specialization) {
        if (specialization == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("doctor").get("specialization"), specialization);
    }

    public static Specification<Visit> startDateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> cb.between(root.get("startDate"), start, end);
    }
}