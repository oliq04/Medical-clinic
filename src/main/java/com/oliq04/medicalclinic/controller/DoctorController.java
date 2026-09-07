package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.exceptions.ErrorMessage;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorCommand;
import com.oliq04.medicalclinic.model.doctor.DoctorDto;
import com.oliq04.medicalclinic.model.doctor.DoctorEditCommand;
import com.oliq04.medicalclinic.service.DoctorService;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Get page of doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of doctors returned successfully", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageableDto.class))),
            @ApiResponse(responseCode = "404", description = "Page not found", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping
    public PageableDto<DoctorDto> getDoctors(@Parameter(description = "Page number") @RequestParam("page") int page,
                                             @Parameter(description = "Page size") @RequestParam("size") int size,
                                             @RequestParam(value = "specialization", required = false) String specialization) {
        return doctorService.getDoctors(page, size, specialization);
    }

    @Operation(summary = "Get doctor by given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageableDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{email}")
    public DoctorDto getDoctor(@Parameter(description = "Email of desired doctor") @PathVariable String email) {
        return doctorService.getDoctor(email);
    }

    @Operation(summary = "Assign doctor by given email to clinic by given clinic name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor assigned successfully", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor or clinic not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Doctor already assigned to given clinic", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/{email}")
    public DoctorDto assignToClinic(@PathVariable String email, @RequestParam("clinicName") String clinicName) {
        return doctorService.assignToClinicByEmail(email, clinicName);
    }

    @Operation(summary = "Add doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor added successfully", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Doctor already exists", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Adding doctor", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorEditCommand.class)))
                               @Valid @RequestBody DoctorCommand doctorCommand) {
        return doctorService.addDoctor(doctorCommand);
    }

    @Operation(summary = "Edit doctor by given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor edited successfully", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Doctor already exists", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PutMapping("/{email}")
    public DoctorDto editDoctor(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Edit of doctor", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorEditCommand.class)))
                                @PathVariable String email, @Valid @RequestBody DoctorEditCommand doctorEditCommand) {
        return doctorService.editDoctor(email, doctorEditCommand);
    }

    @Operation(summary = "Delete doctor by given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted successfully", content =
            @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Doctor already exists", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Doctor or clinic not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@Parameter(description = "Email of doctor to delete") @PathVariable String email) {
        doctorService.deleteDoctor(email);
    }
}
