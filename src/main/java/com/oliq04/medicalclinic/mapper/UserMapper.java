package com.oliq04.medicalclinic.mapper;

import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "update", ignore = true)
    User toEntityFromCommand(UserCommand userCommand);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "update", ignore = true)
    @Mapping(target = "username", source = "patientCommand.email")
    User toEntityFromPatientCommand(PatientCommand patientCommand);

    @Mapping(target = "username", source = "patientCommand.email")
    @Mapping(target = "password", source = "patientCommand.password")
    @Mapping(target = "email", source = "patientCommand.email")
    UserCommand toCommandFromPatientCommand(PatientCommand patientCommand);

    UserCommand toCommand(DoctorCommand doctorCommand);
}
