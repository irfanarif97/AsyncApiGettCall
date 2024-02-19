package com.example.AsyncApiGetCall.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PaginationDTO {
    private int totalItems;
    private int itemsPerPage;
    private int currentPage;
    private int totalPages;
    private String nextPage;
    private String previousPage;
    private List<BookDTO> books;
}
