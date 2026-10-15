package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.DoctorAlreadyAssignedException;
import com.oliq04.medicalclinic.exceptions.DoctorNotFoundException;
import com.oliq04.medicalclinic.exceptions.UserAlreadyExistsException;
import com.oliq04.medicalclinic.mapper.ClinicMapper;
import com.oliq04.medicalclinic.mapper.DoctorMapper;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.doctor.DoctorEditCommand;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.visit.Visit;
import com.oliq04.medicalclinic.repository.DoctorRepository;
import com.oliq04.medicalclinic.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;

public class DoctorServiceTest {

    private DoctorRepository doctorRepository;
    private ClinicService clinicService;
    private DoctorMapper doctorMapper;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private DoctorService doctorService;
    private ClinicMapper clinicMapper;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.clinicService = Mockito.mock(ClinicService.class);
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.doctorService = new DoctorService(doctorRepository, clinicService,
                doctorMapper, userRepository, userMapper);
    }

    @Test
    void assignToClinicByEmail_CorrectData_AssignedDoctorReturned() {
        //given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(new ArrayList<>())
                .build();

        Clinic clinic1 = Clinic.builder()
                .id(1L)
                .name("Clinic1")
                .town("Warsaw")
                .postCode("22-33")
                .address("Address")
                .doctors(new ArrayList<>())
                .visits(new ArrayList<>())
                .build();

        ClinicDto clinicDto = clinicMapper.toDtoFromEntity(clinic1);

        String email = "email@wp.pl";
        String clinicName = "Clinic1";
        when(doctorRepository.findByUserEmail(email)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByUserEmailAndClinicsName(email, clinicName)).thenReturn(false);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(clinicService.findByName(clinicName)).thenReturn(clinic1);
        //when
        DoctorDto result = doctorService.assignToClinicByEmail(email, clinicName);
        //then
        ClinicDto clinicDtoResult = result.getClinics().getFirst();
        Assertions.assertAll(
                () -> assertEquals(1L, clinicDtoResult.getId()),
                () -> assertEquals("Clinic1", clinicDtoResult.getName()),
                () -> assertEquals("22-33", clinicDtoResult.getPostCode()),
                () -> assertEquals("Address", clinicDtoResult.getAddress())
        );
    }

    @Test
    void addDoctor_CorrectData_AddedDoctorReturned() {
        //given
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization("cardiology")
                .email("email@wp.pl")
                .password("12345678")
                .build();
        Doctor doctor = doctorMapper.toEntity(doctorCommand);
        doctor.setSpecialization(Specialization.valueOf(doctorCommand.getSpecialization().toUpperCase()));
        UserCommand userCommand = userMapper.toCommand(doctorCommand);
        when(userRepository.existsByEmail(doctorCommand.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(userMapper.toEntityFromCommand(userCommand));
        when(doctorRepository.save(any())).thenReturn(doctor);
        //when
        DoctorDto result = doctorService.addDoctor(doctorCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor", result.getFirstName()),
                () -> assertEquals("Oekter", result.getLastName()),
                () -> assertEquals("CARDIOLOGY", result.getSpecialization()),
                () -> assertNull("Initial list is null", result.getClinics())
        );
    }

    @Test
    void getDoctors_CorrectData_PageOfDoctorsReturned() {
        //given
        int pageNumber = 0;
        int pageSize = 2;
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(new ArrayList<>())
                .build();
        Doctor doctor2 = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Doctor")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(new ArrayList<>())
                .build();
        Pageable page = PageRequest.of(0, 2);
        List<Doctor> doctors = new ArrayList<>(List.of(doctor1, doctor2));
        PageImpl<Doctor> doctorPage = new PageImpl<>(doctors, page, doctors.size());
        List<DoctorDto> doctorDtoList = doctorPage.stream()
                .map(doctorMapper::toDtoFromEntity)
                .toList();

        when(doctorRepository.findAll(page)).thenReturn(doctorPage);
        //when
        PageableDto<DoctorDto> result = doctorService.getDoctors(0, 2);
        //then
        Assertions.assertAll(
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getPageSize()),
                () -> assertEquals(0, result.getCurrentPage()),
                () -> assertEquals(2, result.getTotal()),
                () -> assertEquals(doctorDtoList, result.getContent())
        );
    }

    @Test
    void getDoctor_CorrectData_DoctorReturned() {
        //given
        String email = "email@wp.pl";
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(new ArrayList<>())
                .build();

        List<ClinicDto> clinicDto = doctor1.getClinics().stream()
                .map(clinicMapper::toDtoFromEntity)
                .toList();

        when(doctorRepository.findByUserEmail(email)).thenReturn(Optional.of(doctor1));
        //when
        DoctorDto result = doctorService.getDoctor(email);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Doctor", result.getFirstName()),
                () -> assertEquals("Oekter", result.getLastName()),
                () -> assertEquals("CARDIOLOGY", result.getSpecialization()),
                () -> assertEquals(clinicDto, result.getClinics())
        );
    }

    @Test
    void editDoctor_CorrectData_EditedDoctorReturned() {
        //given
        String email = "email@wp.pl";
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(null)
                .build();
        DoctorEditCommand doctorEditCommand = DoctorEditCommand.builder()
                .firstName("newName")
                .lastName("newLastName")
                .specialization(Specialization.GASTROLOGY.getSpecializationName())
                .clinics(new ArrayList<>())
                .build();

        when(doctorRepository.findByUserEmail(email)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        //when
        DoctorDto result = doctorService.editDoctor(email, doctorEditCommand);

        //then
        Assertions.assertAll(
                () -> assertEquals("newName", result.getFirstName()),
                () -> assertEquals("newLastName", result.getLastName()),
                () -> assertEquals("GASTROLOGY", result.getSpecialization()),
                () -> assertEquals(0, result.getClinics().size())
        );
    }

    @Test
    void deleteDoctor_CorrectData_DeletedDoctorNoContent() {
        //given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(new ArrayList<>())
                .build();

        when(doctorRepository.findByUserEmail("email")).thenReturn(Optional.of(doctor));
        //when
        doctorService.deleteDoctor("email");
        //then
        verify(doctorRepository).save(doctor);
        verify(doctorRepository).deleteById(1L);
    }

    @Test
    void assignToClinicByEmail_DoctorNotFound_DoctorNotFoundExceptionThrown() {
        //given
        when(doctorRepository.findByUserEmail("email")).thenReturn(Optional.empty());
        //when
        DoctorNotFoundException exception = Assertions.assertThrows(DoctorNotFoundException.class,
                () -> doctorService.assignToClinicByEmail("email", "clinic1"));

        //then
        assertAll(
                () -> assertEquals("Doctor with given email not found", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus().value())
        );
    }

    @Test
    void assignToClinicByEmail_DoctorAlreadyAssigned_DoctorAlreadyAssignedExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("Doctor")
                .lastName("Oekter")
                .specialization(Specialization.CARDIOLOGY)
                .user(new User())
                .visits(List.of(new Visit()))
                .clinics(new ArrayList<>())
                .build();
        when(doctorRepository.findByUserEmail("email")).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByUserEmailAndClinicsName("email", "clinicName")).thenReturn(true);
        //when
        DoctorAlreadyAssignedException exception = Assertions.assertThrows(
                DoctorAlreadyAssignedException.class,
                () -> doctorService.assignToClinicByEmail("email", "clinicName"));

        //then
        assertAll(
                () -> assertEquals("Doctor already assigned to this clinic", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus().value())
        );
    }

    @Test
    void addDoctor_UserAlreadyExists_UserAlreadyExistsExceptionThrown() {
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("Doctor")
                .lastName("DoctorCommand")
                .email("email")
                .build();
        //given
        when(userRepository.existsByEmail("email")).thenReturn(true);
        //when
        UserAlreadyExistsException exception = Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> doctorService.addDoctor(doctorCommand));

        //then
        assertAll(
                () -> assertEquals("User with given email already exists", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus().value())
        );
    }

    @Test
    void getDoctor_DoctorDoesntExists_DoctorNotFoundExceptionThrown() {
        when(doctorRepository.findByUserEmail("email")).thenReturn(Optional.empty());
        //when
        DoctorNotFoundException doctorNotFoundException = Assertions.assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.getDoctor("email"));
        //then
        assertAll(() -> assertEquals(404, doctorNotFoundException.getStatus().value()),
                () -> assertEquals("Doctor with given email not found", doctorNotFoundException.getMessage())
        );
    }
}
