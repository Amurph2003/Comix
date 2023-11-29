package com.rit.comix.controllers;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CliPersonalController extends CliDataController {
    protected ComixCollection comixCollection;
    

    public CliPersonalController() throws IOException {
        //Instantiate as DataController
        super("personalCollection.json");
        collectionMapper.registerModule(new JavaTimeModule());
        generateCollection();
    }

    public CliPersonalController(String filePath) throws IOException {
        //Instantiate as DataController
        super(filePath);
        collectionMapper.registerModule(new JavaTimeModule());
        generateCollection();
    }

    /**
     * Pull PeronalCollection from database.json
     * @throws IOException on failure to access database.json
     */
    public void generateCollection() throws IOException {
        File jsonFile = new File(jsonFilePath);

        //Check file validity
        if (!jsonFile.exists() || jsonFile.isDirectory()) {
            //Bad file; throw exception
            throw new IOException(jsonFilePath + "is not a file.");
        }

        //Check file is not empty
        BufferedReader br = new BufferedReader(new FileReader(jsonFilePath));     
        if (br.readLine() == null) {
            br.close();
            //File empty; create empty collection and finish
            comixCollection = new ComixCollection();
            return;
        }
        br.close();

        comixCollection = collectionMapper.readValue(new File(jsonFilePath), ComixCollection.class);
        comixCollection.setSearchListPath("personalCollectionSearchList.json");

        //Set parentNode of each Publisher to the PersonalCollection
        for (Publisher currentPublisher : comixCollection.getChildren()) {
            currentPublisher.setParentNode(comixCollection);

            //Set parentNode of each Series to its Publisher
            for (Series currentSeries : currentPublisher.getChildren()) {
                currentSeries.setParentNode(currentPublisher);

                //Set parentNode of each Volume to its Series
                for (Volume currentVolume : currentSeries.getChildren()) {
                    currentVolume.setParentNode(currentSeries);

                    //Set parentNode of each ComicBook to its Volume
                    for (ComicBook currentBook : currentVolume.getChildren()) {
                        currentBook.setParentNode(currentVolume);
                    }
                }
            }
        }
    }

    public void addComicBook(SearchEntry bookData) throws IOException{
        //Get Matching Publisher
        Publisher matchedPublisher = null;
        //Find matching Publisher, go to if found
        for (Publisher currentPublisher : this.comixCollection.getChildren()) {
            if (bookData.getPublisher().equals(currentPublisher)) {
                matchedPublisher = currentPublisher;
            }
            //If not found, insert Publisher
            else {
                comixCollection.add(bookData.getPublisher());
                //Get bookData.publisher from tree
                matchedPublisher = comixCollection.getChildren().get(comixCollection.getChildren().size() - 1);
            }
        }

        //Get Matching Series
        Series matchedSeries = null;
        //Find matching Series, go to if found
        for (Series currentSeries : matchedPublisher.getChildren()) {
            if (bookData.getSeries().equals(currentSeries)) {
                matchedSeries = currentSeries;
            }
            //If not found, insert Series
            else {
                matchedPublisher.add(bookData.getSeries());
                //Get bookData.series from tree
                matchedSeries = matchedPublisher.getChildren().get(matchedPublisher.getChildren().size() - 1);
            }
        }

        //Get Matching Volume
        Volume matchedVolume = null;
        //Find matching Volume, go to if found
        for (Volume currentVolume : matchedSeries.getChildren()) {
            if (bookData.getVolume().equals(currentVolume)) {
                matchedVolume = currentVolume;
            }
            //If not found, insert Volume
            else {
                matchedSeries.add(bookData.getVolume());
                //Get bookData.volume from tree
                matchedVolume = matchedSeries.getChildren().get(matchedSeries.getChildren().size() - 1);
            }
        }

        //Add book to branch
        matchedVolume.add(bookData.getComicBook());
    }

    public ComixCollection getComixCollection() {
        return comixCollection;
    }

    //removeComicBook(SearchEntry bookData){} TODO

    //editComicBook(ComicBook book, ComicBook altered) {} TODO

    public void saveCollection() throws IOException{
        collectionMapper.writeValue(new File(jsonFilePath), this.comixCollection);
    }
}
