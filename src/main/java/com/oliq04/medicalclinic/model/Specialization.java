package com.oliq04.medicalclinic.model;

import com.oliq04.medicalclinic.model.doctor.Doctor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Specialization {
    @Id
    private Long id;
    private String specializationName;

    @ManyToMany(mappedBy = "specializations")
    List<Doctor> doctors;
}
