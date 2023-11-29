package com.rit.comix.subsystems.hierarchy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ComixCollection extends Subcategory<Publisher>{
    private String searchListPath;
    protected static ObjectMapper collectionMapper = new ObjectMapper();

    public ComixCollection(String searchListPath){
        super();
        this.searchListPath = searchListPath;
    }

    public ComixCollection(){
        super();
    }

    public void setSearchListPath(String searchListPath) {
        this.searchListPath = searchListPath;
    }

    public void add(Publisher node){
        super.add(node);
        repopulateSearchList();
    }

    public void remove(int nodeIndex){
        super.remove(nodeIndex);
        repopulateSearchList();
    }

    public void remove(Publisher node){
        super.remove(node);
        repopulateSearchList();
    }

    public void update(int nodeIndex, Publisher newNode){
        super.update(nodeIndex, newNode);
        repopulateSearchList();
    }

    @JsonIgnore
    public List<SearchEntry> getSearchEntries() {
        //Make list of SearchEntries from Comix tree
        List<SearchEntry> searchEntries = new ArrayList<>();
        for (Publisher publisher : this.getChildren()) {
            for (Series series : publisher.getChildren()) {
                for (Volume volume : series.getChildren()) {
                    for (ComicBook comicBook : volume.getChildren()) {
                        //Clone subcategories without reference (as to not damage in-use tree)
                        publisher = new Publisher(publisher);
                        series = new Series(series);
                        volume = new Volume(volume);
                        //Remove children
                        publisher.setChildren(null);
                        series.setChildren(null);
                        volume.setChildren(null);
                        searchEntries.add(new SearchEntry(comicBook, publisher, series, volume));
                    }
                }
            }
        }
        return searchEntries;
    }

    @JsonIgnore
    public List<SearchEntry> getSearchEntriesWithChildren() {
        //Make list of SearchEntries from Comix tree
        List<SearchEntry> searchEntries = new ArrayList<>();
        for (Publisher publisher1 : this.getChildren()) {
            for (Series series1 : publisher1.getChildren()) {
                for (Volume volume1 : series1.getChildren()) {
                    for (ComicBook comicBook : volume1.getChildren()) {
                        //Clone subcategories without reference (as to not damage in-use tree)
                        Publisher publisher = new Publisher(publisher1);
                        Series series = new Series(series1);
                        Volume volume = new Volume(volume1);
                        //Remove children
                        publisher.setChildren(publisher.getChildren().subList(0, series.getChildren().size()));
                        series.setChildren(series.getChildren().subList(0, volume.getChildren().size()));
                        volume.setChildren(volume.getChildren().subList(0, 0));
                        searchEntries.add(new SearchEntry(comicBook, publisher, series, volume));
                    }
                }
            }
        }
        return searchEntries;
    }

    //I understand how inefficient this is, but its the only way it'll work with the sorter and we are out of time
    protected void repopulateSearchList() {
        if (searchListPath == null) return;
        collectionMapper.registerModule(new JavaTimeModule());
        try {
            //Add updated list
            collectionMapper.writeValue(new File(searchListPath), this.getSearchEntries().toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void writeFlatCollection(String filename) {
        try {
            collectionMapper.registerModule(new JavaTimeModule());
            collectionMapper.writeValue(new File(filename), this.getSearchEntries().toArray());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

//        if (getChildren() == null) return stringBuilder.toString();
        stringBuilder.append(getNewTabString() + "Collection:");

        for (Publisher publisher : getChildren()){
            stringBuilder.append("\n");
            publisher.setNewTabCounter(this.getNewTabCounter()+1);
            publisher.setNewTabString();
            stringBuilder.append(publisher.toString());
        }

        return stringBuilder.toString();
    }
}