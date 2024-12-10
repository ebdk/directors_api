package com.directors_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {

    @JsonProperty("Director")  // Map the "Director" field from JSON to the "director" field in your class
    private String director;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Rated")
    private String rated;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Writer")
    private String writer;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Released")
    private String released;

    // Getters and setters
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    // Other getters and setters...
}
