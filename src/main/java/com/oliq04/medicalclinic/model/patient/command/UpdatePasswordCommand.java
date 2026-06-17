package com.oliq04.medicalclinic.model.patient.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePasswordCommand {
    @NotBlank
    private String password;
}
