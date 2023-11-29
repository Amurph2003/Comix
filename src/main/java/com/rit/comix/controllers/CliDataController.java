package com.rit.comix.controllers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.searchsort.SearchSortSystem;

public abstract class CliDataController{
    protected static ObjectMapper collectionMapper = new ObjectMapper();
    protected String jsonFilePath;
    protected SearchSortSystem searchSortSystem;

    public CliDataController(String jsonFilePath) throws IOException {
        this.jsonFilePath = jsonFilePath;
        this.searchSortSystem = new SearchSortSystem();
    }

    public List<SearchEntry> search(String args) {
        searchSortSystem.performSearch(args);
        return searchSortSystem.returnSearchResults();
    }

    public List<SearchEntry> sort() {
        searchSortSystem.sort();
        return searchSortSystem.returnSearchResults();
    }

    public List<SearchEntry> sort(String args) {
        searchSortSystem.sort(args);
        return searchSortSystem.returnSearchResults();
    }

}
