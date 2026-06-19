package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public VisitDto createVisit(@RequestBody VisitCommand visitCommand) {
        return visitService.createVisit(visitCommand);
    }

    @GetMapping
    public List<VisitDto> getVisits() {
        return visitService.getVisits();
    }

}
