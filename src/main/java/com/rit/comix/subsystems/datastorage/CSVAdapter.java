package com.rit.comix.subsystems.datastorage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore.Entry;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVParser;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;
import com.rit.comix.subsystems.search.queryer.QueryExecutor;

public class CSVAdapter implements DatabaseAdapter{

    private CSVParser csvParser;

    public CSVAdapter() {
        this.csvParser = new CSVParser();
        
    }
    
    @Override
    public void importData(String datasource, ObjectMapper collectionMapper, int userID) throws IOException {
        List<SearchEntry> searchEntries = new ArrayList<>();
        
        Reader reader = Files.newBufferedReader(Paths.get(datasource));
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(false).build();
        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).build();
        
        String[] header = csvReader.readNext();
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            String publisherName = line[0];
            String seriesName = line[1];
            String volumeNumber = line[2];
            String comicTitle = line[3];
            String issueNumber = line[4];
            LocalDate publicationDate = LocalDate.parse(line[5]);
            String[] creators = line[6].split(", ");
            String[] principleCharacters = line[7].split(", ");
            String description = line[8];
            float baseValue = Float.parseFloat(line[9]);
            int grade = Integer.parseInt(line[10]);
            boolean isSlabbed = Boolean.parseBoolean(line[11]);
            
            Publisher publisher = new Publisher(publisherName, null);
            Series series = new Series(seriesName, publisher);
            Volume volume = new Volume(Integer.parseInt(volumeNumber), series);
            ComicBook comicBook = new ComicBook(comicTitle, issueNumber, publicationDate, creators, principleCharacters, description, baseValue, grade, isSlabbed);

            SearchEntry searchEntry = new SearchEntry(comicBook, publisher, series, volume);
            searchEntries.add(searchEntry);
        }
        
        QueryExecutor qe = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "series"});
        ObjectMapper mapper = new ObjectMapper();
        qe.generateCollection(userID);
        qe.getComixCollection("Cookie", userID).getChildren().clear();
        for(SearchEntry entry : searchEntries){
            qe.addComicBook(entry, "Cookie", userID);
        }
    }

    @Override
    public void exportData(String datasource, String destination, ObjectMapper collectionMapper) {
        List<SearchEntry> entries = new ArrayList<>();
        try {
            entries = collectionMapper.readValue(new File(datasource), new TypeReference<List<SearchEntry>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        String csvFile = destination;
        
        try {
            FileWriter writer = new FileWriter(csvFile);
            
            CSVWriter csvWriter = new CSVWriter(writer);
            
            String[] header = {"Publisher Name", "Series Number", "Volume Number", "Title", "Issue Number", "Publication Date", "Creators", "Principle Characters", "Description", "Base Value", "Grade", "Is Slabbed"};
            csvWriter.writeNext(header);

            for(SearchEntry entry : entries){
                // Create a new String array with the values of each field in the entry
                String[] row = {
                    entry.getPublisher().getPublisherName(),
                    entry.getSeries().getSeriesNumber(),
                    String.valueOf(entry.getVolume().getVolumeNumber()),
                    entry.getComicBook().getTitle(),
                    entry.getComicBook().getIssueNumber(),
                    entry.getComicBook().getPublicationDate().toString(),
                    String.join(", ", entry.getComicBook().getCreators()),
                    String.join(", ", entry.getComicBook().getPrincipleCharacters()),
                    entry.getComicBook().getDescription(),
                    String.valueOf(entry.getComicBook().getBaseValue()),
                    String.valueOf(entry.getComicBook().getGrade()),
                    String.valueOf(entry.getComicBook().getIsSlabbed())
                };

                System.out.println(row);
                
                csvWriter.writeNext(row);
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
