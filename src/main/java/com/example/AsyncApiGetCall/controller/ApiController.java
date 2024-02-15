package com.example.AsyncApiGetCall.controller;


import com.example.AsyncApiGetCall.service.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
class ApiController {

    private final ApiService apiService;

    @GetMapping("/async-api-call")
    public CompletableFuture<JsonNode> makeAsyncApiCall() {
        return apiService.makeAsyncApiCall("https://jsonplaceholder.typicode.com/posts/1");
    }
}
