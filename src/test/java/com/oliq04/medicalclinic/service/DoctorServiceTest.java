package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.mapper.ClinicMapper;
import com.oliq04.medicalclinic.mapper.DoctorMapper;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    void assignToClinicByEmail_CorrectData_DoctorAssigned() {
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
        Assertions.assertAll(
                () -> assertThat(result.getClinics().contains(clinicDto))
        );
    }

    @Test
    void addDoctor_CorrectData_DoctorAdded() {
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
    void getDoctors_CorrectData_PageOfDoctors() {
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
        when(doctorRepository.findAll(page)).thenReturn(doctorPage);
        //when
        PageableDto<DoctorDto> result = doctorService.getDoctors(0, 2);
        //then
        Assertions.assertAll(
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(2, result.getPageSize())
        );
    }


}
