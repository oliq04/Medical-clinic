package com.oliq04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DomainPageRequest {
    private int page;
    private int size;
}
