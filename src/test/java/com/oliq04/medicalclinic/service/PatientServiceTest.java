package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.mapper.PatientMapper;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.repository.PatientRepository;
import com.oliq04.medicalclinic.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    private PatientRepository repository;
    private PatientMapper mapper;
    private PatientService service;
    private UserMapper userMapper;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        this.repository = Mockito.mock(PatientRepository.class);
        this.mapper = Mappers.getMapper(PatientMapper.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.service = new PatientService(repository, mapper, userMapper, userRepository);
    }

    @Test
    void getPatients_GettingAllPatients_PageOfPatients() {
        //given
        int pageNumber = 0;
        int pageSize = 2;
        Patient patient1 = Patient.builder()
                .id(1L)
                .idCardNo(222L)
                .firstName("Tomasz")
                .lastName("Guma")
                .phoneNumber("4444")
                .build();
        Patient patient2 = Patient.builder()
                .id(2L)
                .idCardNo(2223L)
                .firstName("Tomasz")
                .lastName("Struna")
                .phoneNumber("6969")
                .build();

        List<Patient> patientList = new ArrayList<>(List.of(patient1, patient2));
        Pageable page = PageRequest.of(pageNumber, pageSize);
        PageImpl<Patient> pageImpl = new PageImpl<>(patientList, page, 2L);

        when(repository.findAll(page)).thenReturn(pageImpl);
        List<PatientDto> patientsDto = patientList.stream()
                .map(mapper::toDto)
                .toList();
        //when

        PageableDto<PatientDto> result = service.getPatients(pageNumber, pageSize);
        //then

        Assertions.assertAll(
                () -> assertEquals(2, result.getPageSize()),
                () -> assertEquals(0, result.getCurrentPage()),
                () -> assertEquals(2, result.getTotal()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(patientsDto, result.getContent())
        );
    }

    @Test
    void addPatient_CorrectData_PatientCreated() {
        //given
        PatientCommand patientCommand = PatientCommand.builder()
                .email("email@gmail.com")
                .password("1233333")
                .idCardNo(55555L)
                .firstName("Tomasz")
                .lastName("Guma")
                .phoneNumber("44444")
                .birthday(LocalDateTime.of(2004, 3, 3, 5, 0))
                .build();

        Patient patient = mapper.toEntityFromCommand(patientCommand);
        User userFromPatientCommand = userMapper.toEntityFromPatientCommand(patientCommand);
        when(repository.existsByUserEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(userFromPatientCommand);
        when(repository.save(any())).thenReturn(patient);
        //when
        PatientDto result = service.addPatient(patientCommand);

        //then
        Assertions.assertAll(
                () -> assertEquals("Tomasz", result.getFirstName()),
                () -> assertEquals("Guma", result.getLastName()),
                () -> assertEquals("44444", result.getPhoneNumber()),
                () -> assertEquals(LocalDateTime.of(2004, 3, 3, 5, 0), result.getBirthday())
        );
    }

    @Test
    void findByEmail_CorrectData_UserFound() {
        //given
        String email = "email@gmail.com";
        Patient patient = Patient.builder()
                .id(2L)
                .phoneNumber("444")
                .firstName("Tomasz")
                .lastName("Guma")
                .build();

        Optional<Patient> patientOptional = Optional.of(patient);
        when(repository.findPatientByUserEmail(email)).thenReturn(patientOptional);
        //when
        PatientDto result = service.findByEmail(email);
        //then
        Assertions.assertAll(
                () -> assertEquals(2L, result.getId()),
                () -> assertEquals("444", result.getPhoneNumber()),
                () -> assertEquals("Tomasz", result.getFirstName()),
                () -> assertEquals("Guma", result.getLastName())
        );
    }

    @Test
    void modifyPatient_CorrectData_UserModified() {
        //given
        String email = "email@gmail.com";
        Patient patient = Patient.builder()
                .id(2L)
                .phoneNumber("444")
                .firstName("Tomasz")
                .lastName("Guma")
                .birthday(LocalDateTime.of(2004, 3, 3, 5, 0))
                .idCardNo(3333L)
                .build();
        Optional<Patient> patientOptional = Optional.of(patient);
        PatientEditCommand patientEditCommand = PatientEditCommand.builder()
                .idCardNo(2000L)
                .firstName("Grzegorz")
                .lastName("Struna")
                .phoneNumber("4444")
                .birthday(LocalDateTime.of(2005, 3, 3, 5, 0))
                .build();

        when(repository.findPatientByUserEmail(email)).thenReturn(patientOptional);
        //when
        PatientDto result = service.modifyPatient(email, patientEditCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals(2000L, result.getIdCardNo()),
                () -> assertEquals("Grzegorz", result.getFirstName()),
                () -> assertEquals("Struna", result.getLastName()),
                () -> assertEquals("4444", result.getPhoneNumber()),
                () -> assertEquals(LocalDateTime.of(2005, 3, 3, 5, 0), result.getBirthday())
        );
    }
}
