package com.oliq04.medicalclinic.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePasswordCommand {
    private String password;
}
