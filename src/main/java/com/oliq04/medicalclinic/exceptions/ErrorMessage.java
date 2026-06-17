package com.oliq04.medicalclinic.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {
    private LocalDateTime createdAt;
    private String error;
    private HttpStatus httpStatus;
}
