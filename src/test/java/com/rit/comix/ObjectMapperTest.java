package com.rit.comix;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rit.comix.subsystems.hierarchy.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testable
public class ObjectMapperTest {
    //Tests follow the convention of: functionNameBehaviorExpectedResult

    @Test
    public void readValueParseGenericComicTrue() {
        String rowComicJson =
                """
                {
                  "title": "Batman: Year One",
                  "publicationDate": [1987, 1, 14],
                  "creators": [
                    "FrankMiller",
                    "DavidMiller"
                  ],
                  "principleCharacters": [
                    "Batman",
                    "James Gordon"
                  ],
                  "description": "Batman story.",
                  "baseValue": 50,
                  "grade": 9,
                  "isSlabbed": false
                }
                """;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ComicBook comicBook = null;
        try {
            comicBook = objectMapper.readValue(rowComicJson, ComicBook.class);
        } catch (JsonProcessingException exception) {
            fail("Parsing error: " + exception.getMessage());
        }

        assertEquals(comicBook.getTitle(), "Batman: Year One");
        assertEquals(comicBook.getPublicationDate(), LocalDate.of(1987, 01, 14));
        assertArrayEquals(comicBook.getCreators(), new String[]{"FrankMiller", "DavidMiller"});
        assertArrayEquals(comicBook.getPrincipleCharacters(), new String[]{"Batman", "James Gordon"});
        assertEquals(comicBook.getDescription(), "Batman story.");
        assertEquals(comicBook.getBaseValue(), 50);
        assertEquals(comicBook.getGrade(), 9);
        assertEquals(comicBook.getIsSlabbed(), false);
    }

    @Test
    public void writeValueReadValueCreateSerializeAndDeserializeGenericComicTrue() {
        ComicBook comicBook = new ComicBook("Batman: Year One", "1", LocalDate.of(1987, 01, 14), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            objectMapper.writeValue(new File("data.json"), comicBook);
            comicBook = objectMapper.readValue(new File("data.json"), ComicBook.class);
        } catch (IOException exception) {
            fail("IOException error: " + exception.getMessage());
        }

        assertEquals(comicBook.getTitle(), "Batman: Year One");
        assertEquals(comicBook.getPublicationDate(), LocalDate.of(1987, 01, 14));
        assertArrayEquals(comicBook.getCreators(), new String[]{"FrankMiller", "DavidMiller"});
        assertArrayEquals(comicBook.getPrincipleCharacters(), new String[]{"Batman", "James Gordon"});
        assertEquals(comicBook.getDescription(), "Batman story.");
        assertEquals(comicBook.getBaseValue(), 50);
        assertEquals(comicBook.getGrade(), 9);
        assertEquals(comicBook.getIsSlabbed(), false);
    }

    @Test
    public void writeValueReadValueCreateSerializeAndDeserializeGenericComicCollectionTrue() {
        ComixCollection collection = new ComixCollection();

        Publisher publisher = new Publisher("Gotham Comics", collection);
        collection.add(publisher);

        Series series = new Series("1a", publisher);
        publisher.add(series);

        Volume volume = new Volume(1, series);
        series.add(volume);

        ComicBook comicBook = new ComicBook("Batman: Year One", "1", LocalDate.of(2000, 01, 01), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false, volume);
        volume.add(comicBook);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            objectMapper.writeValue(new File("data.json"), collection);

            ComixCollection newCollection = objectMapper.readValue(new File("data.json"), ComixCollection.class);

            //Set parentNode of each Publisher to the PersonalCollection
            for (Publisher currentPublisher : newCollection.getChildren()) {
                currentPublisher.setParentNode(newCollection);

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

            for (Publisher currentPublisher : newCollection.getChildren()) {
                assertEquals(currentPublisher.getParentNode(), newCollection);
                for (Series currentSeries : currentPublisher.getChildren()) {
                    assertEquals(currentSeries.getParentNode(), currentPublisher);
                    for (Volume currentVolume : currentSeries.getChildren()) {
                        assertEquals(currentVolume.getParentNode(), currentSeries);
                        for (ComicBook currentBook : currentVolume.getChildren()) {
                            assertEquals(currentBook.getParentNode(), currentVolume);
                        }
                    }
                }
            }

        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException exception) {
            fail("IOException error: " + exception.getMessage());
        }
    }

    @Test
    public void writeValueReadValueCreateSerializeAndDeserializeGenericLocalDateTrue(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LocalDate dateOriginal = LocalDate.of(2003, 01, 02);

        LocalDate dateDeserialized = LocalDate.of(2000, 01, 01);
        try {
            objectMapper.writeValue(new File("data.json"), dateOriginal);
            dateDeserialized = objectMapper.readValue(new File("data.json"), LocalDate.class);
        } catch (IOException exception) {
            fail("IOException error: " + exception.getMessage());
        }

        assertEquals(dateOriginal, dateDeserialized);
    }


}
