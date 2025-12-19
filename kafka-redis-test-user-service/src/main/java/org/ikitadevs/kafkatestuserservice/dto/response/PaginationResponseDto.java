package org.ikitadevs.kafkatestuserservice.dto.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaginationResponseDto<T> {
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection;
    private Long totalElements;
    private int totalPages;
    private T content;
}
