package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.model.Clinic;
import com.oliq04.medicalclinic.model.Doctor;
import com.oliq04.medicalclinic.model.Specialization;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepositoryProvider doctorRepository;
    private final ClinicService clinicService;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Doctor assignToClinicByEmail(String email, String clinicName) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));

        if (doctorRepository.existsByUserEmailAndClinicsName(email, clinicName)) {
            throw new DoctorAlreadyAssignedException("Doctor already assigned to this clinic", HttpStatus.CONFLICT);
        }
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

    public PageableDto<DoctorDto> getDoctors(int pageNumber, int doctorsCount, String specializationName) {
        Pageable page = PageRequest.of(pageNumber, doctorsCount);

        Page<Doctor> doctorPage = (specializationName != null && !specializationName.isBlank())
                ? doctorRepository.findBySpecialization(
                Specialization.valueOf(specializationName.toUpperCase()), page)
                : doctorRepository.findAll(page);

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

    @Transactional
    public DoctorDto editDoctor(String email, DoctorEditCommand doctorEditCommand) {
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given email not found", HttpStatus.NOT_FOUND));

        List<Clinic> clinicsMapped = doctorEditCommand.getClinics().stream()
                .map(clinicNameCommand -> clinicService.findByName(clinicNameCommand.getName()))
                .collect(Collectors.toList());

        doctor.update(doctorEditCommand, clinicsMapped);
        return doctorMapper.toDtoFromEntity(doctorRepository.save(doctor));
    }

    @Transactional
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
