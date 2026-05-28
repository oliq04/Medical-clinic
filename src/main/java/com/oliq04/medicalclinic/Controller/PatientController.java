package com.oliq04.medicalclinic.Controller;

import com.oliq04.medicalclinic.Model.Patient;
import com.oliq04.medicalclinic.Model.UpdatePasswordCommand;
import com.oliq04.medicalclinic.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.findPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
    }

    @PutMapping("/{email}")
    public void modifyPatientCompletley(@PathVariable String email, @RequestBody Patient newPatientInfo) {
        patientService.modifyPatient(email, newPatientInfo);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        patientService.removePatientByEmail(email);
    }

    @PatchMapping("/{email}/password")
    public void editPassword(@PathVariable String email, @RequestBody UpdatePasswordCommand command) {
        patientService.editPassword(email, command.getPassword());
    }
}
