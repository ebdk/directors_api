package com.directors_api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class DirectorServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DirectorService directorService;

    @Test
    public void testGetDirectors() throws Exception {
        // Mocking the API response
        String url = "https://directa24-movies.wiremockapi.cloud/api/movies/search?page=1";

        // Mock the JSON structure as a byte array response
        String jsonResponse = "{ \"data\": [ {\"Director\": \"Quentin Tarantino\"}, {\"Director\": \"Martin Scorsese\"} ], \"total_pages\": 1 }";
        byte[] responseBody = jsonResponse.getBytes();  // Simulate the byte array response

        // Create the ResponseEntity with the byte array
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // Mock the RestTemplate's exchange method to return the mock response
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(byte[].class)))
                .thenReturn(responseEntity);

        // Call the service method
        Map<Integer, List<String>> directors = directorService.getMoviesPerDirector();

        // Validate the result
        assertNotNull(directors);  // Ensure the result is not null
        // Add additional assertions to check if the map contains expected results
        assertNotNull(directors.get(1));  // Expect at least one director with one movie
        assertTrue(directors.get(1).contains("Quentin Tarantino"));
        assertTrue(directors.get(1).contains("Martin Scorsese"));
    }
}
