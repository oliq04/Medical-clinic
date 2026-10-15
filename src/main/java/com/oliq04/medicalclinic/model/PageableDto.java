package com.oliq04.medicalclinic.model;

import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class PageableDto<T> {
    private int pageSize;
    private int currentPage;
    private int total;
    private int totalPages;
    private List<T> content;

    public static <T> PageableDto<T> toPageable(List<T> content, Page page) {
        return new PageableDto<>(page.getSize(), page.getNumber(), page.getNumberOfElements(), page.getTotalPages(), content);
    }
}
