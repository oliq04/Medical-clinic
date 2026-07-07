package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.exceptions.ErrorMessage;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.user.UserDto;
import com.oliq04.medicalclinic.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @Operation(summary = "Get page of users")
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
    public PageableDto<UserDto> getUsers(@Parameter(description = "Page number") @RequestParam("page") int page,
                                         @Parameter(description = "Page size") @RequestParam("size") int size) {
        return userService.getUsers(page, size);
    }

    @Operation(summary = "Get user by given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desired patient", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageableDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{email}")
    public UserDto getUserByEmail(@Parameter(description = "Email of desired User") @PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "User already exists", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of new user", required = true, content =
    @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserCommand.class))) @Valid @RequestBody UserCommand userCommand) {
        return userService.addUser(userCommand);
    }

    @Operation(summary = "Edit user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User edited", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PutMapping("/{email}")
    public UserDto editUser(@Parameter(description = "Email of user to edit") @PathVariable String email,
                            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "New details of user", required = true, content =
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserCommand.class)))
                            @Valid @RequestBody UserCommand userCommand) {
        return userService.editUser(email, userCommand);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content =
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByEmail(@Parameter(description = "Email of user to delete") @PathVariable String email) {
        userService.deleteUserByEmail(email);
    }
}
