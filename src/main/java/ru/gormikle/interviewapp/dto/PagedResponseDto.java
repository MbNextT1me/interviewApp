package ru.gormikle.interviewapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponseDto<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
}
