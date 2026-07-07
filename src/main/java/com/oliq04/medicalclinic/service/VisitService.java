package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.*;
import com.oliq04.medicalclinic.mapper.VisitMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.doctor.Doctor;
import com.oliq04.medicalclinic.model.patient.entity.Patient;
import com.oliq04.medicalclinic.model.visit.Visit;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.repository.ClinicRepository;
import com.oliq04.medicalclinic.repository.DoctorRepository;
import com.oliq04.medicalclinic.repository.PatientRepository;
import com.oliq04.medicalclinic.repository.VisitRepository;
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
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;

    @Transactional
    public VisitDto createVisit(VisitCommand visitCommand) {
        if (!isTimeQuarterOfHour(visitCommand.getStartTime().getMinute())) {
            throw new IllegalArgumentException();
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

    private boolean isTimeQuarterOfHour(int minute) {
        return minute % 15 == 0;
    }
}
