package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.*;
import com.oliq04.medicalclinic.mapper.VisitMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.model.visit.Visit;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;

    @Transactional
    public VisitDto createVisit(VisitCommand visitCommand) {
        if (!isTimeQuarterOfHour(visitCommand.getStartTime().getMinute())) {
            throw new IllegalTimeException("Minutes must be quarters (00,15,30,45)", HttpStatus.BAD_REQUEST);
        }

        Doctor doctor = doctorRepository.findByUserEmail(visitCommand.getDoctorEmail())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found", HttpStatus.NOT_FOUND));

        if (!visitRepository.findOverlappingVisits(visitCommand.getStartTime(), visitCommand.getEndTime(), doctor.getId()).isEmpty()) {
            throw new VisitOverlapException("Visits overlap", HttpStatus.CONFLICT);
        }
        Visit newVisit = new Visit();
        Clinic clinic = clinicRepository.findByName(visitCommand.getClinicName())
                .orElseThrow(() -> new ClinicNotFoundException("Clinic not found", HttpStatus.NOT_FOUND));
        newVisit.setDoctor(doctor);
        newVisit.setPatient(null);
        newVisit.setStartDate(visitCommand.getStartTime());
        newVisit.setEndDate(visitCommand.getEndTime());
        newVisit.setClinic(clinic);
        return visitMapper.toDto(visitRepository.save(newVisit));
    }

    public PageableDto<VisitDto> getVisits(int pageNumber, int visitsCount) {
        Pageable page = PageRequest.of(pageNumber, visitsCount);
        Page<Visit> visits = visitRepository.findAll(page);
        List<VisitDto> visitDtos = visits.stream()
                .map(visitMapper::toDto)
                .toList();
        return PageableDto.toPageable(visitDtos, visits);
    }

    @Transactional
    public VisitDto assignPatient(Long patientId, Long visitId) {
        Visit visit = visitRepository.findVisitById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found", HttpStatus.NOT_FOUND));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found", HttpStatus.NOT_FOUND));
        visit.setPatient(patient);
        visitRepository.save(visit);
        return visitMapper.toDto(visit);
    }

    public PageableDto<VisitDto> getVisitsAssignedToPatient(Long patientId, int pageNumber, int visitsCount) {
        PageRequest pageRequest = PageRequest.of(pageNumber, visitsCount);
        Page<Visit> visits = visitRepository.findVisitsByPatientId(patientId, pageRequest);
        List<VisitDto> visitDtoList = visits.stream()
                .map(visitMapper::toDto)
                .toList();
        return PageableDto.toPageable(visitDtoList, visits);
    }

    public PageableDto<VisitDto> getVisitsAssignedToDoctor(Long doctorId, int pageNumber, int visitsCount) {
        PageRequest pageRequest = PageRequest.of(pageNumber, visitsCount);
        Page<Visit> visits = visitRepository.findVisitsByDoctorId(doctorId, pageRequest);
        List<VisitDto> visitDtoList = visits.stream()
                .map(visitMapper::toDto)
                .toList();
        return PageableDto.toPageable(visitDtoList, visits);
    }

    public PageableDto<VisitDto> getAvailableVisitsBySpecializationAndDate(int page, int size, LocalDate fromDate, LocalDate toDate, String specializationName) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Specialization specialization = null;

        if (specializationName != null && !specializationName.isBlank()) {
            specialization = Specialization.valueOf(specializationName.toUpperCase());
        }

        if (toDate == null) {
            toDate = fromDate;
        }

        Specification<Visit> specification = Specification.allOf(
                VisitSpecification.hasNoPatient(),
                VisitSpecification.startDateBetween(fromDate, toDate.plusDays(1)),
                VisitSpecification.hasSpecialization(specialization)
        );
        Page<Visit> visits = visitRepository.findAll(specification, pageRequest);
        List<VisitDto> visitDtoList = visits.stream()
                .map(visitMapper::toDto)
                .toList();
        return PageableDto.toPageable(visitDtoList, visits);
    }

    private boolean isTimeQuarterOfHour(int minute) {
        return minute % 15 == 0;
    }

    public PageableDto<VisitDto> getAvailableVisitsAssignedToDoctor(Long id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Visit> visits = visitRepository.findVisitsByDoctorIdAndPatientIsNull(id, pageRequest);
        List<VisitDto> visitDtoList = visits.stream()
                .map(visitMapper::toDto)
                .toList();
        return PageableDto.toPageable(visitDtoList, visits);
    }

    public VisitDto cancelVisit(Long id) {
        Visit visit = visitRepository.findById(id).orElseThrow(() -> new VisitNotFoundException("Visit not found", HttpStatus.NOT_FOUND));
        visit.setDoctor(null);
        visit.setPatient(null);
        Visit savedVisit = visitRepository.save(visit);
        return visitMapper.toDto(savedVisit);
    }
}
