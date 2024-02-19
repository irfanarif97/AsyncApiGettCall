package com.example.AsyncApiGetCall.service;

import com.example.AsyncApiGetCall.DTO.BookDTO;
import com.example.AsyncApiGetCall.DTO.PaginationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class ApiService {

    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public Map<String, List<BookDTO>> getAllBooks(String baseUrl, String authorName) throws ExecutionException, InterruptedException {
        Map<String, List<BookDTO>> booksMap = new HashMap<>();
        List<BookDTO> authorBooks = new ArrayList<>();
        List<BookDTO> nullSubjectBooks = new ArrayList<>();

        String nextPageUrl = baseUrl;
        do {
            CompletableFuture<PaginationDTO> paginationFuture = makeAsyncApiCall(nextPageUrl);
            PaginationDTO paginationDTO = paginationFuture.get();

            paginationDTO.getBooks().forEach(bookDTO -> {
                if (bookDTO.getAuthorName().equals(authorName)) {
                    authorBooks.add(bookDTO);
                }
                if (bookDTO.getSubject() == null) {
                    nullSubjectBooks.add(bookDTO);
                }
            });

            nextPageUrl = paginationDTO.getNextPage();
        } while (nextPageUrl != null);

        booksMap.put("authorBooks", authorBooks);
        booksMap.put("nullSubjectBooks", nullSubjectBooks);

        return booksMap;
    }

    public CompletableFuture<PaginationDTO> makeAsyncApiCall(String url ){
        return  CompletableFuture.supplyAsync(() -> {
            try {
                HttpGet request = new HttpGet(url);
                return httpClient.execute(request, httpResponse -> {
                    String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                    return objectMapper.readValue(jsonResponse, PaginationDTO.class);
                });
            } catch (IOException e) {
                return null;
            }
        });
    }
}
