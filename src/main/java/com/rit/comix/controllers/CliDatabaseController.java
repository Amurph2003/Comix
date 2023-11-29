package com.rit.comix.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class CliDatabaseController extends CliDataController {

    private static ObjectMapper collectionMapper = new ObjectMapper();
    private List<SearchEntry> dataCollection;
    
    /**
     * Instantiate and pull json data to populate master database collection
     * @throws IOException
     */
    public CliDatabaseController() throws IOException {
        super("database.json");

        collectionMapper.registerModule(new JavaTimeModule());
        generateCollection();
    }

    public CliDatabaseController(String jsonFilePath) throws IOException {
        super(jsonFilePath);

        collectionMapper.registerModule(new JavaTimeModule());
        generateCollection();
    }



    public void generateCollection() throws IOException {
        File jsonFile = new File(jsonFilePath);

        //Check file validity
        if (!jsonFile.exists() || jsonFile.isDirectory()) {
            //Bad file; throw exception
            throw new IOException(jsonFilePath + "is not a file.");
        }

        dataCollection = new ArrayList<>();

        //Check file is not empty
        BufferedReader br = new BufferedReader(new FileReader(jsonFilePath));     
        if (br.readLine() == null) {
            br.close();
            //File empty; create empty collection and finish
            return;
        }
        br.close();

        SearchEntry[] dataArray = collectionMapper.readValue(new File(jsonFilePath), SearchEntry[].class);

        dataCollection = Arrays.asList(dataArray);
    }

    public List<SearchEntry> getDataCollection() {
        return dataCollection;
    }
}