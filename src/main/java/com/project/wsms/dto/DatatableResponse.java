package com.project.wsms.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

//@Builder
@Getter @Setter
public class DatatableResponse <T> {
    private int draw;

    private long recordsTotal;

    private long recordsFiltered;

    private List<T> data;
}
