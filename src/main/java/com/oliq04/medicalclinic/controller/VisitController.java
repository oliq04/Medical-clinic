package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public VisitDto createVisit(@Valid @RequestBody VisitCommand visitCommand) {
        return visitService.createVisit(visitCommand);
    }

    @GetMapping
    public PageableDto<VisitDto> getVisits(@RequestParam("page") int page, @RequestParam("size") int size) {
        return visitService.getVisits(page, size);
    }

    @PostMapping("/patient")
    public VisitDto assignPatient(@RequestParam("patientId") Long patientId, @RequestParam("visitId") Long visitId) {
        return visitService.assignPatient(patientId, visitId);
    }
}
