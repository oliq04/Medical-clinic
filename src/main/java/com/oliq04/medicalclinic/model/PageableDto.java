package com.oliq04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto<T> {
    private int pageSize;
    private int currentSite;
    private int total;
    private int totalPages;
    private List<T> content;

    public static <T> PageableDto<T> toPageable(List<T> content, Page page) {
        return new PageableDto<>(page.getSize(), page.getNumber(), page.getNumberOfElements(), page.getTotalPages(), content);
    }
}
