package com.oliq04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DomainPage {
    private int size;
    private int number;
    private int numberOfElements;
    private int totalPages;


}
