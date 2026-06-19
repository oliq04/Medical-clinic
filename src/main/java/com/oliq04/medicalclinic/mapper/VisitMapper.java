package com.oliq04.medicalclinic.mapper;

import com.oliq04.medicalclinic.model.visit.Visit;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    Visit toEntity(VisitCommand visitCommand);

    VisitDto toDto(Visit visit);
}
