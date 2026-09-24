package com.oliq04.medicalclinic.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Patient patient;
    private Doctor doctor;

    public User update(User userCommand) {
        this.setUsername(userCommand.getUsername());
        this.setPassword(userCommand.getPassword());
        this.setEmail(userCommand.getEmail());

        return this;
    }
}
