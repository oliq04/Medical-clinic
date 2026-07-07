package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.PatientNotFoundException;
import com.oliq04.medicalclinic.mapper.PatientMapper;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.repository.PatientRepository;
import com.oliq04.medicalclinic.exceptions.PatientAlreadyExistsException;
import com.oliq04.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public PageableDto<PatientDto> getPatients(int pageNumber, int patientsCount) {
        Pageable page = PageRequest.of(pageNumber, patientsCount);
        Page<Patient> patientDtoPage = patientRepository.findAll(page);
        List<PatientDto> patients = patientDtoPage.stream()
                .map(patientMapper::toDto)
                .toList();
        return PageableDto.toPageable(patients, patientDtoPage);
    }

    @Transactional
    public PatientDto addPatient(PatientCommand patient) {
        if (patientRepository.existsByUserEmail(patient.getEmail())) {
            throw new PatientAlreadyExistsException("Patient with given email already exists", HttpStatus.CONFLICT);
        }
        UserCommand userCommand = userMapper.toCommandFromPatientCommand(patient);
        User user = userRepository.save(userMapper.toEntityFromCommand(userCommand));
        Patient patientEntity = patientMapper.toEntityFromCommand(patient);
        patientEntity.setUser(user);
        return patientMapper.toDto(patientRepository.save(patientEntity));
    }

    public PatientDto findByEmail(String email) {
        return patientMapper.toDto(findByUserEmailOrThrowNotFoundException(email, "Patient not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void removeByEmail(String email) {
        Patient patient = findByUserEmailOrThrowNotFoundException(email, "Patient not found", HttpStatus.NOT_FOUND);
        patientRepository.delete(patient);
    }

    @Transactional
    public PatientDto modifyPatient(String email, PatientEditCommand newPatientInfo) {
        Patient patient = findByUserEmailOrThrowNotFoundException(email, "Patient not found", HttpStatus.NOT_FOUND);
        patient.update(newPatientInfo);
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    private Patient findByUserEmailOrThrowNotFoundException(String email, String message, HttpStatus status) {
        return patientRepository.findPatientByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(message, status));
    }

}