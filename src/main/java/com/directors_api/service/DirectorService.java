package com.directors_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.directors_api.model.Movie;
import com.directors_api.model.MovieGetResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DirectorService {

    private static final String API_URL = "https://directa24-movies.wiremockapi.cloud/api/movies/search?page=";
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DirectorService.class);
    private static final int RETRY_COUNT = 3;
    private static final int RATE_LIMIT = 10;  // 10 requests per second

    public DirectorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("movies")
    @Async
    public CompletableFuture<MovieGetResponse> fetchMoviesPage(String url) throws InterruptedException {
        // Simulate rate limiting
        TimeUnit.MILLISECONDS.sleep(1000 / RATE_LIMIT);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);
        byte[] responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Empty response body.");
        }
        String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);
        try {
            return CompletableFuture.completedFuture(new ObjectMapper().readValue(responseBodyString, MovieGetResponse.class));
        } catch (JsonProcessingException e) {
            logger.error("Error parsing response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse response.");
        }
    }

    public Map<Integer, List<String>> getMoviesPerDirector() {
        Map<String, Integer> directorMovieCountMap = new HashMap<>();
        int page = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            String url = API_URL + page;

            // Retry logic for failed API calls
            MovieGetResponse movieResponse = null;
            int attempts = 0;
            while (attempts < RETRY_COUNT) {
                try {
                    movieResponse = fetchMoviesPage(url).join();  // Block to wait for async call
                    break;
                } catch (Exception e) {
                    logger.error("Error fetching movies (attempt {}): {}", attempts + 1, e.getMessage());
                    attempts++;
                    if (attempts == RETRY_COUNT) {
                        throw new RuntimeException("Failed to fetch movie data after retries.");
                    }
                }
            }

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
        }

        // Group directors by the number of movies they have
        Map<Integer, List<String>> moviesPerDirectorMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : directorMovieCountMap.entrySet()) {
            String director = entry.getKey();
            int movieCount = entry.getValue();

            // Add the director to the list corresponding to their movie count
            moviesPerDirectorMap.computeIfAbsent(movieCount, k -> new ArrayList<>()).add(director);
        }

        return moviesPerDirectorMap;
    }

    public List<String> getDirectorsByThreshold(int threshold, boolean sortDescending) {
        Map<Integer, List<String>> moviesPerDirectorMap = getMoviesPerDirector();
        List<String> directors = new ArrayList<>();

        // Get directors who have more than the threshold
        for (Map.Entry<Integer, List<String>> entry : moviesPerDirectorMap.entrySet()) {
            if (entry.getKey() > threshold) {
                directors.addAll(entry.getValue());
            }
        }

        // Sort alphabetically
        if (sortDescending) {
            Collections.sort(directors, Collections.reverseOrder());
        } else {
            Collections.sort(directors);
        }

        return directors;
    }
}