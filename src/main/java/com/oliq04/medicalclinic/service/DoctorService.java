package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.DoctorNotFoundException;
import com.oliq04.medicalclinic.exceptions.UserAlreadyExistsException;
import com.oliq04.medicalclinic.mapper.DoctorMapper;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ClinicService clinicService;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
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
        doctor.setSpecialization(Specialization.valueOf(doctorCommand.getSpecialization().toUpperCase()));
        UserCommand userCommand = userMapper.toCommand(doctorCommand);
        User user = userRepository.save(userMapper.toEntityFromCommand(userCommand));
        doctor.setUser(user);
        return doctorMapper.toDtoFromEntity(doctorRepository.save(doctor));
    }

    public PageableDto<DoctorDto> getDoctors(int pageNumber, int doctorsCount) {
        Pageable page = PageRequest.of(pageNumber, doctorsCount);
        Page<Doctor> doctorPage = doctorRepository.findAll(page);
        List<DoctorDto> doctorDtoList = doctorPage.stream()
                .map(doctorMapper::toDtoFromEntity)
                .toList();
        return PageableDto.toPageable(doctorDtoList, doctorPage);
    }

    public DoctorDto getDoctor(String email) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));
        return doctorMapper.toDtoFromEntity(doctor);
    }

    public DoctorDto editDoctor(String email, DoctorEditCommand doctorEditCommand) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));

        List<Clinic> clinicsMapped = doctorEditCommand.getClinics().stream()
                .map(clinicNameCommand -> clinicService.findByName(clinicNameCommand.getName()))
                .collect(Collectors.toList());

        doctor.update(doctorEditCommand, clinicsMapped);
        return doctorMapper.toDtoFromEntity(doctorRepository.save(doctor));
    }

    public void deleteDoctor(String email) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));
        doctor.getVisits().forEach(visit -> visit.setDoctor(null));
        doctor.getClinics().forEach(clinic -> clinic.setDoctors(null));
        doctor.getUser().setDoctor(null);
        doctor.setUser(null);

        doctorRepository.save(doctor);
        doctorRepository.deleteById(doctor.getId());
    }

}
