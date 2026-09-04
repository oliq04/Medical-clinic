package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.exceptions.ErrorMessage;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import com.oliq04.medicalclinic.model.visit.VisitCommand;
import com.oliq04.medicalclinic.model.visit.VisitDto;
import com.oliq04.medicalclinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "Create visit (given minutes must be in quarter of hour gaps - 00, 15, 30, 45)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit created", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VisitDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Clinic not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Visits overlap", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDto createVisit(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of new visit", required = true, content =
    @Content(mediaType = "application/json",
            schema = @Schema(implementation = PatientEditCommand.class)))
                                @Valid @RequestBody VisitCommand visitCommand) {
        return visitService.createVisit(visitCommand);
    }

    @Operation(summary = "Get page of visits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of patients", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageableDto.class))),
            @ApiResponse(responseCode = "404", description = "Page not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping
    public PageableDto<VisitDto> getVisits(@Parameter(description = "Page number") @RequestParam("page") int page,
                                           @Parameter(description = "Page size") @RequestParam("size") int size) {
        return visitService.getVisits(page, size);
    }

    @Operation(summary = "Assign patient to visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient assigned", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VisitDto.class))),
            @ApiResponse(responseCode = "404", description = "Visit not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Patient not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/patient")
    public VisitDto assignPatient(@Parameter(description = "Id of patient to assign to visit")
                                  @RequestParam("patientId") Long patientId,
                                  @Parameter(description = "Id of visit") @RequestParam("visitId") Long visitId) {
        return visitService.assignPatient(patientId, visitId);
    }

    @GetMapping("/patient/{id}")
    public PageableDto<VisitDto> visitsAssignedToPatient(@PathVariable Long id, @RequestParam("page") int page,
                                                         @RequestParam("size") int size) {
        return visitService.getVisitsAssignedToPatient(id, page, size);
    }

    @GetMapping("/doctor/{id}")
    public PageableDto<VisitDto> visitsAssignedToDoctor(@PathVariable Long id, @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        return visitService.getAvailableVisitsAssignedToDoctor(id, page, size);
    }

    @GetMapping("/doctor")
    public PageableDto<VisitDto> visitsOfSpecializationAndDate(@RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               @RequestParam("date") LocalDate date,
                                                               @RequestParam("specialization") String specialization) {

        return visitService.getAvailableVisitsBySpecializationAndDate(page,size,date,specialization);
    }


}