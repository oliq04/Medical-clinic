package com.oliq04.medicalclinic.controller;

import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.user.UserDto;
import com.oliq04.medicalclinic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping
    public PageableDto<UserDto> getUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
        return userService.getUsers(page, size);
    }

    @GetMapping("/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCommand userCommand) {
        return userService.addUser(userCommand);
    }

    @PutMapping("/{email}")
    public UserDto editUser(@PathVariable String email, @Valid @RequestBody UserCommand userCommand) {
        return userService.editUser(email, userCommand);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
    }
}
