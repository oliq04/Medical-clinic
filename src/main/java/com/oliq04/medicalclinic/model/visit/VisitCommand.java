package com.oliq04.medicalclinic.model.visit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class VisitCommand {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String doctorEmail;
    private String patientEmail;
    private String clinicName;
}
