package com.oliq04.medicalclinic.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Pageable<T> {
    private DomainPage domainPage;
    private List<T> content;

    public static <T> Pageable<T> toPageable(List<T> content, DomainPage page) {
        return new Pageable<>(page, content);
    }
}