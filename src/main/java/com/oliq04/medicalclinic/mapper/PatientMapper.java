package com.oliq04.medicalclinic.mapper;

import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toDto(Patient patient);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "update", ignore = true)
    Patient toEntity(PatientDto patientDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "update", ignore = true)
    Patient toEntityFromCommand(PatientCommand patientCommand);
}
