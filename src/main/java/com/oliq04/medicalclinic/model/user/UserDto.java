package com.oliq04.medicalclinic.model.user;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String username;
    private String email;
}
