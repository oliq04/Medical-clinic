package com.oliq04.medicalclinic.model.clinic;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClinicCommand {
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Town can't be empty")
    private String town;
    @NotBlank(message = "Post code can't be empty")
    private String postCode;
    @NotBlank(message = "Address can't be empty")
    private String address;
}
