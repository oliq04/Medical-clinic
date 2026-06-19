package com.oliq04.medicalclinic.model.specialization;

import com.oliq04.medicalclinic.model.doctor.Doctor;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String specializationName;
    //zamienic na enum w kodzie
    @ManyToMany(mappedBy = "specializations")
    List<Doctor> doctors;
}
