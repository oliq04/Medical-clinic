package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.ClinicNotFoundException;
import com.oliq04.medicalclinic.exceptions.DoctorNotFoundException;
import com.oliq04.medicalclinic.exceptions.PatientNotFoundException;
import com.oliq04.medicalclinic.exceptions.VisitOverlapException;
import com.oliq04.medicalclinic.mapper.VisitMapper;
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
import lombok.RequiredArgsConstructor;
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
    private final PatientService patientService;

    public VisitDto createVisit(VisitCommand visitCommand) {
        if (!isTimeQuarterOfHour(visitCommand.getStartTime().getMinute())) {
            throw new IllegalArgumentException();
        }

        if (!visitRepository.findOverlappingVisits(visitCommand.getStartTime(), visitCommand.getEndTime()).isEmpty()) {
            throw new VisitOverlapException("Visits overlap", HttpStatus.CONFLICT);
        }
        Visit newVisit = new Visit();
        Doctor doctor = doctorRepository.findByUserEmail(visitCommand.getDoctorEmail())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found", HttpStatus.NOT_FOUND));
        //Patient patient = patientRepository.findPatientByUserEmail(visitCommand.getPatientEmail())
        //.orElseThrow(() -> new PatientNotFoundException("Patient not found", HttpStatus.NOT_FOUND));
        Clinic clinic = clinicRepository.findByName(visitCommand.getClinicName())
                .orElseThrow(() -> new ClinicNotFoundException("Clinic not found", HttpStatus.NOT_FOUND));
        newVisit.setDoctor(doctor);
        newVisit.setPatient(null);
        newVisit.setStartDate(visitCommand.getStartTime());
        newVisit.setEndDate(visitCommand.getEndTime());
        newVisit.setClinic(clinic);
        return visitMapper.toDto(visitRepository.save(newVisit));
    }

    public List<VisitDto> getVisits() {
        return visitRepository.findAll().stream()
                .map(visitMapper::toDto)
                .toList();
    }

    public VisitDto assignPatient(Long patientId, Long visitId) {
        Visit visit = visitRepository.findVisitById(visitId).getFirst();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found", HttpStatus.NOT_FOUND));
        visit.setPatient(patient);
        return visitMapper.toDto(visit);
    }

    private boolean isTimeQuarterOfHour(int minute) {
        return minute % 15 == 0;
    }
}
