package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @GetMapping
    public PageableDto<ClinicDto> getClinics(@RequestParam("page") int page, @RequestParam("size") int size) {
        return clinicService.getClinics(page, size);
    }

    @GetMapping("/{name}")
    public ClinicDto getClinic(@PathVariable String name) {
        return clinicService.getClinic(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicDto addClinic(@Valid @RequestBody ClinicCommand clinicCommand) {
        return clinicService.addClinic(clinicCommand);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClinic(@PathVariable String name) {
        clinicService.deleteClinic(name);
    }

    @PutMapping("/{name}")
    public ClinicDto editClinic(@PathVariable String name, @Valid @RequestBody ClinicCommand clinicCommand) {
        return clinicService.editClinic(name, clinicCommand);
    }
}
