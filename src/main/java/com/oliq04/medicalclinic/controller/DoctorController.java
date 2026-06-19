package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.doctor.DoctorEditCommand;
import com.oliq04.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDto> getDoctors() {
        return doctorService.getDoctors();
    }

    @GetMapping("/{email}")
    public DoctorDto getDoctor(@PathVariable String email) {
        return doctorService.getDoctor(email);
    }

    @PostMapping("/{email}")
    public DoctorDto assignToClinic(@PathVariable String email, @RequestParam("clinicName") String clinicName) {
        return doctorService.assignToClinicByEmail(email, clinicName);
    }

    @PostMapping
    public DoctorDto addDoctor(@RequestBody DoctorCommand doctorCommand) {
        return doctorService.addDoctor(doctorCommand);
    }

    @PutMapping("/{email}")
    public DoctorDto editDoctor(@PathVariable String email, @RequestBody DoctorEditCommand doctorEditCommand) {
        return doctorService.editDoctor(email, doctorEditCommand);
    }

    @DeleteMapping("/{email}")
    public void deleteDoctor(@PathVariable String email) {
        doctorService.deleteDoctor(email);
    }
}
