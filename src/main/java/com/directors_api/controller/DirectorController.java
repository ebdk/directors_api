package com.directors_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.directors_api.service.DirectorService;

import java.util.List;
import java.util.Map;

@RestController
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping("/api/directors")
    public Map<String, List<String>> getDirectorsByThreshold(@RequestParam("threshold") int threshold,
                                                             @RequestParam(value = "sortDescending", defaultValue = "false") boolean sortDescending) {
        List<String> directors = directorService.getDirectorsByThreshold(threshold, sortDescending);

        // Create the response map
        Map<String, List<String>> response = Map.of("directors", directors);
        return response;
    }
}
