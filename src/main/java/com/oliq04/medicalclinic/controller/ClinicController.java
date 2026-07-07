package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.exceptions.ErrorMessage;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.service.ClinicService;
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

@RestController
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @Operation(summary = "Get page of clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinics found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageableDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            })
    })
    @GetMapping
    public PageableDto<ClinicDto> getClinics(@Parameter(description = "Page number") @RequestParam("page") int page,
                                             @Parameter(description = "Page size") @RequestParam("size") int size) {
        return clinicService.getClinics(page, size);
    }

    @Operation(summary = "Get clinic by given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinic found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            }),
            @ApiResponse(responseCode = "404", description = "Clinic not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            })
    })
    @GetMapping("/{name}")
    public ClinicDto getClinic(@Parameter(description = "Desired clinic name") @PathVariable String name) {
        return clinicService.getClinic(name);
    }

    @Operation(summary = "Add clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clinic added successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            }),
            @ApiResponse(responseCode = "409", description = "Clinic already exists", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicDto addClinic(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Clinic to create", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicCommand.class)))
            @Valid @RequestBody ClinicCommand clinicCommand) {
        return clinicService.addClinic(clinicCommand);
    }

    @Operation(summary = "Delete clinic by given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clinic deleted successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            }),
            @ApiResponse(responseCode = "404", description = "Clinic not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            })
    })
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClinic(@PathVariable String name) {
        clinicService.deleteClinic(name);
    }

    @Operation(summary = "Edit clinic by given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinic updated successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            }),
            @ApiResponse(responseCode = "404", description = "Clinic not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            }),
            @ApiResponse(responseCode = "409", description = "Clinic already exists", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)),
            })
    })
    @PutMapping("/{name}")
    public ClinicDto editClinic(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Edit of clinic", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicCommand.class)))
                                @Parameter(description = "Name of clinic to edit") @PathVariable String name,
                                @Valid @RequestBody ClinicCommand clinicCommand) {
        return clinicService.editClinic(name, clinicCommand);
    }
}
