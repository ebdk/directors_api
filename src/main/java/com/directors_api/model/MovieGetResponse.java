package com.directors_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unrecognized properties
public class MovieGetResponse {

    @JsonProperty("data")
    private List<Movie> data;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("page")  // Add this field to map the "page" field in the response
    private int currentPage;

    // Getters and setters
    public List<Movie> getData() {
        return data;
    }

    public void setData(List<Movie> data) {
        this.data = data;
    }

    public int getTotal_pages() {
        return totalPages;
    }

    public void setTotal_pages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
