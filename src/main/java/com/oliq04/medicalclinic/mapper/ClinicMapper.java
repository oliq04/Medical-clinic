package com.oliq04.medicalclinic.mapper;

import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    @Mapping(target = "doctors", ignore = true)
    @Mapping(target = "id", ignore = true)
    Clinic toEntityFromCommand(ClinicCommand clinicCommand);

    ClinicDto toDtoFromEntity(Clinic clinic);
}
