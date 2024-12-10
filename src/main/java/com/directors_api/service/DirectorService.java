package com.directors_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.directors_api.model.Movie;
import com.directors_api.model.MovieGetResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class DirectorService {

    // Corrected API URL
    private static final String API_URL = "https://directa24-movies.wiremockapi.cloud/api/movies/search?page=";
    private final RestTemplate restTemplate;

    public DirectorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<Integer, List<String>> getMoviesPerDirector() {
        Map<String, Integer> directorMovieCountMap = new HashMap<>();
        int page = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            String url = API_URL + page;

            // Retrieve the raw response body as a byte array
            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);
            byte[] responseBody = response.getBody();

            if (responseBody == null) {
                break;
            }

            // Convert the byte array to a String
            String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);

            // Now parse the response body string to JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                MovieGetResponse movieResponse = objectMapper.readValue(responseBodyString, MovieGetResponse.class);

                if (movieResponse == null || movieResponse.getData() == null || movieResponse.getData().isEmpty()) {
                    break;
                }

                // Process directors and count their movies
                for (Movie movie : movieResponse.getData()) {
                    String director = movie.getDirector();
                    if (director != null) {
                        directorMovieCountMap.put(director, directorMovieCountMap.getOrDefault(director, 0) + 1);
                    }
                }

                int totalPages = movieResponse.getTotal_pages();
                page++;
                if (page > totalPages) {
                    hasMorePages = false;
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                // Handle the error if the JSON parsing fails
            }
        }

        // Group directors by the number of movies they have
        Map<Integer, List<String>> moviesPerDirectorMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : directorMovieCountMap.entrySet()) {
            String director = entry.getKey();
            int movieCount = entry.getValue();

            // Add the director to the list corresponding to their movie count
            moviesPerDirectorMap
                    .computeIfAbsent(movieCount, k -> new ArrayList<>())
                    .add(director);
        }

        return moviesPerDirectorMap;
    }

}
