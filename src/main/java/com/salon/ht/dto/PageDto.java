package com.salon.ht.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class PageDto<T extends Serializable> implements Serializable {
    private boolean first;
    private boolean last;
    private long number;
    private long numberOfElements;
    private long size;
    private long totalElements;
    private long totalPages;
    private List<?> content;

    public PageDto(Page page, List<T> content) {
        this.content = content;
        this.first = page.isFirst();
        this.last = page.isLast();
        this.number = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
