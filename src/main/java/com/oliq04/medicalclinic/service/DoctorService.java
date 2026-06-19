package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.ClinicNotFoundException;
import com.oliq04.medicalclinic.exceptions.DoctorNotFoundException;
import com.oliq04.medicalclinic.exceptions.UserAlreadyExistsException;
import com.oliq04.medicalclinic.mapper.DoctorMapper;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.doctor.DoctorEditCommand;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.repository.ClinicRepository;
import com.oliq04.medicalclinic.repository.DoctorRepository;
import com.oliq04.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ClinicService clinicService;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SpecializationService specializationService;
    private final ClinicRepository clinicRepository;

    public DoctorDto assignToClinicByEmail(String email, String clinicName) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));

        List<Clinic> clinics = doctor.getClinics();
        clinics.add(clinicService.findByName(clinicName));

        return doctorMapper.toDtoFromEntity(doctorRepository.save(doctor));
    }

    public DoctorDto addDoctor(DoctorCommand doctorCommand) {
        if (userRepository.existsByEmail(doctorCommand.getEmail())) {
            throw new UserAlreadyExistsException("User with given email already exists", HttpStatus.CONFLICT);
        }

        Doctor doctor = doctorMapper.toEntity(doctorCommand);
        Specialization specialization = specializationService.findSpecialization(doctorCommand.getSpecialization());
        doctor.setSpecialization(specialization.getSpecializationName());
        UserCommand userCommand = userMapper.toCommand(doctorCommand);
        User user = userRepository.save(userMapper.toEntityFromCommand(userCommand));
        doctor.setUser(user);
        return doctorMapper.toDtoFromEntity(doctorRepository.save(doctor));
    }

    public List<DoctorDto> getDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDtoFromEntity)
                .toList();
    }

    public DoctorDto getDoctor(String email) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));
        return doctorMapper.toDtoFromEntity(doctor);
    }

    public DoctorDto editDoctor(String email, DoctorEditCommand doctorEditCommand) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));

        doctor.update(doctorEditCommand);
        return doctorMapper.toDtoFromEntity(doctorRepository.save(doctor));
    }

    public void deleteDoctor(String email) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));
        doctorRepository.delete(doctor);
    }

}
