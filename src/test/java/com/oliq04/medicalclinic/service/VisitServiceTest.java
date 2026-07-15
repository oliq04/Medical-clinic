package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.DoctorNotFoundException;
import com.oliq04.medicalclinic.exceptions.IllegalTimeException;
import com.oliq04.medicalclinic.mapper.ClinicMapper;
import com.oliq04.medicalclinic.mapper.DoctorMapper;
import com.oliq04.medicalclinic.mapper.PatientMapper;
import com.oliq04.medicalclinic.mapper.VisitMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.visit.Visit;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.repository.ClinicRepository;
import com.oliq04.medicalclinic.repository.DoctorRepository;
import com.oliq04.medicalclinic.repository.PatientRepository;
import com.oliq04.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VisitServiceTest {

    private VisitRepository visitRepository;
    private VisitMapper visitMapper;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private ClinicRepository clinicRepository;
    private VisitService visitService;
    private DoctorMapper doctorMapper;
    private ClinicMapper clinicMapper;
    private PatientMapper patientMapper;

    @BeforeEach
    void setup() {
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.clinicRepository = Mockito.mock(ClinicRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.visitService = new VisitService(visitRepository, visitMapper,
                doctorRepository, patientRepository, clinicRepository);
    }

    @Test
    void createVisit_CorrectData_NewVisit() {
        //given
        VisitCommand visitCommand = VisitCommand.builder()
                .startTime(LocalDateTime.of(2026, 2, 4, 1, 30))
                .endTime(LocalDateTime.of(2026, 2, 4, 1, 45))
                .doctorEmail("doctoremail@wp.pl")
                .patientEmail("patientemail@wp.pl")
                .clinicName("Clinic1")
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(null)
                .build();
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .doctors(List.of(Mockito.mock(Doctor.class)))
                .visits(List.of(Mockito.mock(Visit.class)))
                .build();
        Visit visit = Visit.builder()
                .id(1L)
                .startDate(visitCommand.getStartTime())
                .endDate(visitCommand.getEndTime())
                .patient(null)
                .doctor(doctor)
                .clinic(clinic)
                .build();

        DoctorDto doctorDto = doctorMapper.toDtoFromEntity(doctor);
        when(doctorRepository.findByUserEmail("doctoremail@wp.pl")).thenReturn(Optional.of(doctor));
        when(visitRepository.findOverlappingVisits(any(), any(), any())).thenReturn(new ArrayList<>());
        when(clinicRepository.findByName(visitCommand.getClinicName())).thenReturn(Optional.of(clinic));
        when(visitRepository.save(any())).thenReturn(visit);
        //when
        VisitDto result = visitService.createVisit(visitCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(doctorDto, result.getDoctor()),
                () -> assertNull(result.getPatient()),
                () -> assertEquals(visit.getEndDate(), result.getEndDate()),
                () -> assertEquals(visitCommand.getStartTime(), result.getStartDate())
        );
    }

    @Test
    void getVisits_CorrectData_PageOfVisits() {
        int pageNumber = 0;
        int pageSize = 2;
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(null)
                .build();
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .doctors(List.of(Mockito.mock(Doctor.class)))
                .visits(List.of(Mockito.mock(Visit.class)))
                .build();
        Visit visit = Visit.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2026, 2, 4, 1, 30))
                .endDate(LocalDateTime.of(2026, 2, 4, 1, 45))
                .patient(null)
                .doctor(doctor)
                .clinic(clinic)
                .build();
        Visit visitTwo = Visit.builder()
                .id(2L)
                .startDate(LocalDateTime.of(2026, 2, 5, 1, 30))
                .endDate(LocalDateTime.of(2026, 2, 5, 1, 45))
                .patient(null)
                .doctor(doctor)
                .clinic(clinic)
                .build();
        List<Visit> visits = new ArrayList<>(List.of(visit, visitTwo));
        Pageable page = PageRequest.of(pageNumber, pageSize);
        PageImpl<Visit> visitPage = new PageImpl<>(visits, page, visits.size());
        when(visitRepository.findAll(page)).thenReturn(visitPage);
        List<VisitDto> visitDtos = visitPage.stream()
                .map(visitMapper::toDto)
                .toList();
        //when
        PageableDto<VisitDto> result = visitService.getVisits(pageNumber, pageSize);
        //then
        Assertions.assertAll(
                () -> assertEquals(visitDtos, result.getContent()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(0, result.getCurrentPage()),
                () -> assertEquals(pageSize, result.getPageSize()),
                () -> assertEquals(2, result.getTotal())
        );
    }

    @Test
    void assignPatient_CorrectData_PatientAssignedToVisit() {
        //given
        Long patientId = 1L;
        Long visitId = 1L;

        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(null)
                .build();

        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .doctors(List.of(Mockito.mock(Doctor.class)))
                .visits(List.of(Mockito.mock(Visit.class)))
                .build();

        Visit visit = Visit.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2026, 2, 4, 1, 30))
                .endDate(LocalDateTime.of(2026, 2, 4, 1, 45))
                .patient(null)
                .doctor(doctor)
                .clinic(clinic)
                .build();

        Patient patient = Patient.builder()
                .id(1L)
                .idCardNo(222L)
                .firstName("Tomasz")
                .lastName("Guma")
                .phoneNumber("4444")
                .build();

        ClinicDto clinicDto = clinicMapper.toDtoFromEntity(clinic);
        DoctorDto doctorDto = doctorMapper.toDtoFromEntity(doctor);
        PatientDto patientDto = patientMapper.toDto(patient);

        when(visitRepository.findVisitById(1L)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.save(visit)).thenReturn(visit);
        //when
        VisitDto result = visitService.assignPatient(1L, 1L);
        Assertions.assertAll(
                () -> assertEquals(LocalDateTime.of(2026, 2, 4, 1, 30), result.getStartDate()),
                () -> assertEquals(LocalDateTime.of(2026, 2, 4, 1, 45), result.getEndDate()),
                () -> assertEquals(clinicDto, result.getClinic()),
                () -> assertEquals(doctorDto, result.getDoctor()),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(patientDto, result.getPatient())
        );
    }

    @Test
    void createVisit_IncorrectMinutes_IllegalArgumentExceptionThrown() {
        //given
        VisitCommand visitCommand = VisitCommand
                .builder()
                .startTime(LocalDateTime.of(2026, 2, 4, 1, 31))
                .endTime(LocalDateTime.of(2026, 2, 4, 1, 45))
                .doctorEmail("doctoremail@wp.pl")
                .patientEmail("patientemail@wp.pl")
                .clinicName("Clinic1")
                .build();

        //when
        IllegalTimeException exception = Assertions.assertThrows(IllegalTimeException.class,
                () -> visitService.createVisit(visitCommand));

        //then
        assertAll(() -> assertEquals("Minutes must be quarters (00,15,30,45)", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus().value())
        );
    }

    @Test
    void createVisit_DoctorNotFound_DoctorNotFoundExceptionThrown() {
        //given
        VisitCommand visitCommand = VisitCommand
                .builder()
                .startTime(LocalDateTime.of(2026, 2, 4, 1, 30))
                .endTime(LocalDateTime.of(2026, 2, 4, 1, 45))
                .doctorEmail("doctoremail@wp.pl")
                .patientEmail("patientemail@wp.pl")
                .clinicName("Clinic1")
                .build();

        when(doctorRepository.findByUserEmail(visitCommand.getDoctorEmail())).thenReturn(Optional.empty());
        //when
        DoctorNotFoundException exception = Assertions.assertThrows(DoctorNotFoundException.class,
                () -> visitService.createVisit(visitCommand));

        //then
        assertAll(() -> assertEquals("Doctor not found", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus().value())
        );
    }
}
