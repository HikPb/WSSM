package com.project.wsms.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Builder
@Getter @Setter @NoArgsConstructor
public class DatatableResponse <T> {
    private int draw;

    private long recordsTotal;

    private long recordsFiltered;

    private List<T> data;
}
