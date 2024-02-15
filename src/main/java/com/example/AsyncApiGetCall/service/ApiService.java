package com.example.AsyncApiGetCall.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ApiService {

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient httpClient;

    public CompletableFuture<JsonNode> makeAsyncApiCall(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpGet request = new HttpGet(url);
                return httpClient.execute(request, httpResponse -> {
                    String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                    return objectMapper.readTree(jsonResponse);
                });
            } catch (IOException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to call API");
                errorResponse.put("message", e.getMessage());
                return objectMapper.valueToTree(errorResponse);
            }
        });
    }
}
