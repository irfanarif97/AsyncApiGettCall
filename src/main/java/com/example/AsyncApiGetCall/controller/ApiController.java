package com.example.AsyncApiGetCall.controller;


import com.example.AsyncApiGetCall.DTO.BookDTO;
import com.example.AsyncApiGetCall.service.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
class ApiController {

    private final ObjectMapper objectMapper;
    private final ApiService apiService;

    @GetMapping("/async-api-call/{authorName}")
    public String makeAsyncApiCall(@PathVariable String authorName) throws ExecutionException, InterruptedException {
        String baseUrl = "https://api.example.com/posts?page=1";
        Map<String, List<BookDTO>> books = apiService.getAllBooks(baseUrl, authorName);
        return convertToJson(books);
    }

    private String convertToJson( Map<String, List<BookDTO>> books) {
        try {
            return objectMapper.writeValueAsString(books);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
