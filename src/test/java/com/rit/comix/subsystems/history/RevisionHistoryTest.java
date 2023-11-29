package com.rit.comix.subsystems.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;
import com.rit.comix.subsystems.search.queryer.QueryExecutor;
import com.rit.comix.subsystems.search.queryer.Queryer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RevisionHistoryTest {
    @BeforeEach
    @AfterEach
    public void clearJsonFile() {
        try {
            String filePath = "personalCollection-1.json";

            FileWriter fileWriter = new FileWriter(filePath, false);
            fileWriter.write("{}"); // write an empty JSON object to the file
            System.out.println("Hierarchical JSON file cleared successfully.");

            filePath = "personalCollection-1-SearchList.json";

            fileWriter.write("{}"); // write an empty JSON object to the file
            System.out.println("Hierarchical JSON file cleared successfully.");

            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the JSON file: " + e.getMessage());
        }
    }

    @Test
    public void TestAddRevisionCreating() throws IOException {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();

        List<SearchEntry> dbqResults = dbq.getResults();
        dbq.addComicBook(dbqResults.get(0), "authCookie", 1, false);

        SearchEntry entry = new SearchEntry(dbqResults.get(0));

        EntryRevision expected = new EntryRevision(null, entry);
        EntryRevision actual = RevisionHistory.getInstance().getUndoableChanges().get(0);

        assertTrue(expected.equals(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ComixCollection comixCollection = objectMapper.readValue(new File("personalCollection-1.json"), ComixCollection.class);

        assertEquals(entry.getPublisher(), comixCollection.getChildren().get(0));
        assertEquals(1, comixCollection.getChildren().size());
    }

    @Test
    public void TestAddRevisionRestoration() throws IOException {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();

        List<SearchEntry> dbqResults = dbq.getResults();
        dbq.addComicBook(dbqResults.get(0), "authCookie", 1, false);

        EntryRevision entryRevision = RevisionHistory.getInstance().getUndoableChanges().get(0);

        dbq.restoreUndoableRevision(entryRevision, "authCookie", 1);

        ObjectMapper objectMapper = new ObjectMapper();

        ComixCollection comixCollection = objectMapper.readValue(new File("personalCollection-1.json"), ComixCollection.class);

        assertEquals(0, comixCollection.getChildren().size());
    }

    @Test
    public void TestRemoveRevisionCreating() throws IOException {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();

        List<SearchEntry> dbqResults = dbq.getResults();
        dbq.addComicBook(dbqResults.get(0), "authCookie", 1, false);
        dbq.removeComicBook(dbqResults.get(0), "authCookie", 1, false);

        SearchEntry entry = new SearchEntry(dbqResults.get(0));

        EntryRevision expected = new EntryRevision(null, entry);
        EntryRevision actual = RevisionHistory.getInstance().getUndoableChanges().get(0);

        assertTrue(expected.equals(actual));

        expected = new EntryRevision(entry, null);
        actual = RevisionHistory.getInstance().getUndoableChanges().get(1);

        assertTrue(expected.equals(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ComixCollection comixCollection = objectMapper.readValue(new File("personalCollection-1.json"), ComixCollection.class);

        assertEquals(0, comixCollection.getChildren().size());
        assertEquals(2, RevisionHistory.getInstance().getUndoableChanges().size());
    }

    @Test
    public void TestRemoveRevisionRestoration() throws IOException {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();

        List<SearchEntry> dbqResults = dbq.getResults();
        dbq.addComicBook(dbqResults.get(0), "authCookie", 1, false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ComixCollection comixCollection = objectMapper.readValue(new File("personalCollection-1.json"), ComixCollection.class);

        comixCollection = dbq.removeComicBook(dbqResults.get(0), null, 1, false);

        EntryRevision entryRevision = RevisionHistory.getInstance().getUndoableChanges().get(1);

        dbq.restoreUndoableRevision(entryRevision, "authCookie", 1);

        // assertEquals(entry.getPublisher(), comixCollection.getChildren().get(0));
        assertEquals(0, comixCollection.getChildren().size());
        assertEquals(1, RevisionHistory.getInstance().getUndoableChanges().size());
    }

    @Test
    public void TestEditRevisionCreating() throws IOException {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();

        List<SearchEntry> dbqResults = dbq.getResults();

        dbq.addComicBook(dbqResults.get(0), "authCookie", 1, false);

        SearchEntry newEntry = new SearchEntry(new ComicBook(dbqResults.get(0).getComicBook()), new Publisher(dbqResults.get(0).getPublisher()), new Series(dbqResults.get(0).getSeries()), new Volume(dbqResults.get(0).getVolume()));
        newEntry.getComicBook().setDescription("Different description.");

        dbq.editComicBook(dbqResults.get(0), newEntry, "authCookie", 1, false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ComixCollection comixCollection = objectMapper.readValue(new File("personalCollection-1.json"), ComixCollection.class);
        
        assertEquals(1, comixCollection.getChildren().size());
        assertEquals(2, RevisionHistory.getInstance().getUndoableChanges().size());
    }

    @Test
    public void TestEditRevisionRestoration() throws IOException {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();

        List<SearchEntry> dbqResults = dbq.getResults();
        dbq.addComicBook(dbqResults.get(0), "authCookie", 1, false);

        SearchEntry newEntry = new SearchEntry(new ComicBook(dbqResults.get(0).getComicBook()), new Publisher(dbqResults.get(0).getPublisher()), new Series(dbqResults.get(0).getSeries()), new Volume(dbqResults.get(0).getVolume()));
        newEntry.getComicBook().setDescription("Different description.");

        dbq.editComicBook(dbqResults.get(0), newEntry, "authCookie", 1, false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ComixCollection comixCollection = objectMapper.readValue(new File("personalCollection-1.json"), ComixCollection.class);

        EntryRevision entryRevision = RevisionHistory.getInstance().getUndoableChanges().get(1);

        dbq.restoreUndoableRevision(entryRevision, "authCookie", 1);
        comixCollection = dbq.getComixCollection("", 1);

        entryRevision = RevisionHistory.getInstance().getUndoableChanges().get(0);

        dbq.restoreUndoableRevision(entryRevision, "authCookie", 1);
        comixCollection = dbq.getComixCollection("", 1);

        // assertEquals(entry.getPublisher(), comixCollection.getChildren().get(0));
        assertEquals(0, comixCollection.getChildren().size());
        assertEquals(0, RevisionHistory.getInstance().getUndoableChanges().size());
    }
}