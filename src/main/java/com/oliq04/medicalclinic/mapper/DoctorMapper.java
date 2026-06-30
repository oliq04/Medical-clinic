package com.oliq04.medicalclinic.mapper;

import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDtoFromEntity(Doctor doctor);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "clinics", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    Doctor toEntity(DoctorCommand doctorCommand);
}
