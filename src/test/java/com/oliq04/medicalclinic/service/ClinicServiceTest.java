package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.mapper.ClinicMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.visit.Visit;
import com.oliq04.medicalclinic.repository.ClinicRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClinicServiceTest {
    private ClinicRepository repository;
    private ClinicMapper mapper;
    private ClinicService service;

    @BeforeEach
    void setup() {
        this.repository = Mockito.mock(ClinicRepository.class);
        this.mapper = Mappers.getMapper(ClinicMapper.class);
        this.service = new ClinicService(repository, mapper);
    }

    @Test
    void getClinics_CorrectData_PageOfClinics() {
        //given
        int pageNumber = 0;
        int pageSize = 2;
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Clinic clinic1 = Clinic.builder()
                .id(1L)
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .doctors(List.of(Mockito.mock(Doctor.class)))
                .visits(List.of(Mockito.mock(Visit.class)))
                .build();

        Clinic clinic2 = Clinic.builder()
                .id(2L)
                .name("Clinic2")
                .town("Wroclaw")
                .postCode("333-33")
                .address("Address")
                .doctors(List.of(Mockito.mock(Doctor.class)))
                .visits(List.of(Mockito.mock(Visit.class)))
                .build();

        List<Clinic> clinics = new ArrayList<>(List.of(clinic1, clinic2));
        PageImpl<Clinic> clinicPage = new PageImpl<>(clinics, page, clinics.size());
        List<ClinicDto> clinicDtos = clinics.stream()
                .map(mapper::toDtoFromEntity)
                .toList();
        when(repository.findAll(page)).thenReturn(clinicPage);
        //when
        PageableDto<ClinicDto> result = service.getClinics(0, 2);
        //then
        Assertions.assertAll(
                () -> assertEquals(2, result.getPageSize()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(0, result.getCurrentPage()),
                () -> assertEquals(2, result.getTotal()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(clinicDtos, result.getContent())
        );
    }

    @Test
    void getClinic_CorrectData_SpecificClinic() {
        //given
        String name = "Clinic1";
        Clinic clinic1 = Clinic.builder()
                .id(1L)
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .doctors(List.of(Mockito.mock(Doctor.class)))
                .visits(List.of(Mockito.mock(Visit.class)))
                .build();

        when(repository.findByName(name)).thenReturn(Optional.of(clinic1));
        //when
        ClinicDto result = service.getClinic(name);
        //then
        Assertions.assertAll(
                () -> assertEquals(name, result.getName()),
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("Warsaw", result.getTown()),
                () -> assertEquals("22-33", result.getPostCode()),
                () -> assertEquals("Address", result.getAddress())
        );
    }

    @Test
    void addClinic_CorrectData_ClinicAdded() {
        //given
        ClinicCommand clinicCommand = ClinicCommand.builder()
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Polna 1")
                .build();
        when(repository.save(any())).thenReturn(mapper.toEntityFromCommand(clinicCommand));
        //when
        ClinicDto result = service.addClinic(clinicCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals("Clinic1", result.getName()),
                () -> assertEquals("Warsaw", result.getTown()),
                () -> assertEquals("22-33", result.getPostCode()),
                () -> assertEquals("Polna 1", result.getAddress())
        );
    }

    @Test
    void editClinic_CorrectData_ClinicEdited() {
        //given
        ClinicCommand clinicCommand = ClinicCommand.builder()
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Polna 1")
                .build();
        String clinicName = "Clinic1";
        Clinic clinic = mapper.toEntityFromCommand(clinicCommand);
        when(repository.existsByName(clinicName)).thenReturn(false);
        when(repository.save(clinic)).thenReturn(clinic);
        //when
        ClinicDto result = service.addClinic(clinicCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals("Clinic1", result.getName()),
                () -> assertEquals("Warsaw", result.getTown()),
                () -> assertEquals("22-33", result.getPostCode()),
                () -> assertEquals("Polna 1", result.getAddress())
        );
    }

    @Test
    void deleteClinic_CorrectData_ClinicRemoved() {
        //given
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("name")
                .build();
        when(repository.findByName("name")).thenReturn(Optional.of(clinic));
        //when
        service.deleteClinic("name");
        //then
        verify(repository).delete(clinic);
    }
}
