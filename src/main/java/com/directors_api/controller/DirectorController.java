package com.directors_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.directors_api.service.DirectorService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping("/api/directors")
    public Map<String, List<String>> getDirectorsByThreshold(@RequestParam("threshold") int threshold) {
        // Get the full list of directors with their movie counts
        Map<Integer, List<String>> moviesPerDirectorMap = directorService.getMoviesPerDirector();

        // Get the directors who have made at least the given threshold number of movies
        List<String> directors = moviesPerDirectorMap.getOrDefault(threshold, Collections.emptyList());

        // Create the response map
        Map<String, List<String>> response = new HashMap<>();
        response.put("directors", directors);

        return response;
    }
}
