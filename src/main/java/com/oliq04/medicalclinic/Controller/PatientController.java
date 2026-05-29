package com.oliq04.medicalclinic.Controller;

import com.oliq04.medicalclinic.Model.Patient;
import com.oliq04.medicalclinic.Model.UpdatePasswordCommand;
import com.oliq04.medicalclinic.Service.PatientService;
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
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getByEmail(@PathVariable String email) {
        return patientService.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @PutMapping("/{email}")
    public Patient editPatient(@PathVariable String email, @RequestBody Patient newPatientInfo) {
        return patientService.modifyPatient(email, newPatientInfo);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByEmail(@PathVariable String email) {
        patientService.removeByEmail(email);
    }

    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPasswordByEmail(@PathVariable String email, @RequestBody UpdatePasswordCommand command) {
        patientService.editPassword(email, command.getPassword());
    }
}
