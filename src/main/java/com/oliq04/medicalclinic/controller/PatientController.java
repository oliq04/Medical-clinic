package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public PageableDto<PatientDto> getPatients(@RequestParam("page") int page, @RequestParam("size") int size) {
        return patientService.getPatients(page, size);
    }

    @GetMapping("/{email}")
    public PatientDto getByEmail(@Email @PathVariable String email) {
        return patientService.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@Valid @RequestBody PatientCommand patient) {
        return patientService.addPatient(patient);
    }

    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable String email, @Valid @RequestBody PatientEditCommand newPatientInfo) {
        return patientService.modifyPatient(email, newPatientInfo);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByEmail(@PathVariable String email) {
        patientService.removeByEmail(email);
    }
}
