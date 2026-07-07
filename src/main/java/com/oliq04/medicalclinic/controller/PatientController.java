package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.exceptions.ErrorMessage;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.patient.command.PatientCommand;
import com.oliq04.medicalclinic.model.patient.command.PatientEditCommand;
import com.oliq04.medicalclinic.model.patient.dto.PatientDto;
import com.oliq04.medicalclinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Get page of patients")
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
    public PageableDto<PatientDto> getPatients(@Parameter(description = "Number of page") @RequestParam("page") int page,
                                               @Parameter(description = "Size of page") @RequestParam("size") int size) {
        return patientService.getPatients(page, size);
    }

    @Operation(summary = "Get patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desired patient", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "404", description = "Patient not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{email}")
    public PatientDto getByEmail(@Parameter(description = "Email of desired patient") @Email @PathVariable String email) {
        return patientService.findByEmail(email);
    }

    @Operation(summary = "Add patient and create user if doesn't exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient added", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Patient already exists", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Patient details", required = true, content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PatientCommand.class)))
                                 @Valid @RequestBody PatientCommand patient) {
        return patientService.addPatient(patient);
    }

    @Operation(summary = "Edit patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient edited", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "404", description = "Patient not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Patient already exists", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PutMapping("/{email}")
    public PatientDto editPatient(@Parameter(description = "Patient's email") @PathVariable String email,
                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                          description = "New details of patient", required = true, content =
                                  @Content(mediaType = "application/json",
                                          schema = @Schema(implementation = PatientEditCommand.class)))
                                  @Valid @RequestBody PatientEditCommand newPatientInfo) {
        return patientService.modifyPatient(email, newPatientInfo);
    }

    @Operation(summary = "Delete patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByEmail(@PathVariable String email) {
        patientService.removeByEmail(email);
    }
}
