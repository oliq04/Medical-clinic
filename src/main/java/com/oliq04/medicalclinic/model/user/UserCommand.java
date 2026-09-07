package com.oliq04.medicalclinic.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserCommand {
    @NotBlank(message = "Username can't be empty")
    private String username;
    @NotBlank(message = "Password can't be empty")
    private String password;
    @Email(message = "Invalid email pattern")
    @NotBlank(message = "Email can't be empty")
    private String email;

}
