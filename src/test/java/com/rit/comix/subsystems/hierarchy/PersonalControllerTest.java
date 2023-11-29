package com.rit.comix.subsystems.hierarchy;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.rit.comix.subsystems.search.queryer.QueryExecutor;


@Testable
public class PersonalControllerTest {
    //Tests follow the convention of: functionNameBehaviorExpectedResult


    private static final String PERSONAL_COLLECTION_FILE_NAME = "personalCollection.json";
    private static final String PERSONAL_COLLECTION_SEARCH_LIST_FILE_NAME = "personalCollectionSearchList.json";
    private static final String TEST_DATA_FILE_NAME = "testData.json";
    private static final String TEST_DATA_SEARCH_LIST_FILE_NAME = "testDataSearchList.json";

    @BeforeEach
    public void overwriteJsonFiles() {
        try {
            // read test data from the files
            String testData = new String(Files.readAllBytes(Paths.get(TEST_DATA_FILE_NAME)));
            String testDataSearchList = new String(Files.readAllBytes(Paths.get(TEST_DATA_SEARCH_LIST_FILE_NAME)));

            // overwrite personalCollection.json with the test data
            Files.write(Paths.get(PERSONAL_COLLECTION_FILE_NAME), testData.getBytes());

            // overwrite personalCollectionSearchList.json with the test data search list
            Files.write(Paths.get(PERSONAL_COLLECTION_SEARCH_LIST_FILE_NAME), testDataSearchList.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateCollectionNoFatalOnEmptyFile() throws IOException {
        //Create with empty json file
        //Runs generateCollection on instantiation
        QueryExecutor queryExecutor = new QueryExecutor(new String[0]);

        //Test collection was made and is empty
        assertEquals(2, queryExecutor.getTree(0).getChildren().size());
    }

    @Test
    void generateCollectionSucceedsOnValidFile() throws IOException{
        //Runs generateCollection on instantiation
        QueryExecutor queryExecutor = new QueryExecutor(new String[0]);

        //Test collection was made and is not empty
        assertNotEquals(0, queryExecutor.getTree(0).getChildren().size());
    }

    @Test
    void updateNodeSetsAllStatistics() throws IOException {
        //Runs generateCollection on instantiation
        QueryExecutor queryExecutor = new QueryExecutor(new String[0]);
        //personalController.saveCollection();

        //The original collection value should be 17.7
        assertEquals(17.7, queryExecutor.getTree(0).getTotalValue());

        //Enter Publisher 0 "Acclaim Comics"
        Publisher publisher = queryExecutor.getTree(0).getChildNode(0);

        //Enter Series 0 "Magic: the Gathering - The Shadow Mage"
        Series series = publisher.getChildNode(0);

        //Enter volume 0 (1)
        Volume volume = series.getChildNode(0);

        //Get ComicBook 0 "The Aster Fall"
        ComicBook origBook = volume.getChildNode(0);

        //Update ComicBook 0
        ComicBook newBook = new ComicBook("An updated book", origBook.getIssueNumber(), origBook.getPublicationDate(), origBook.getCreators(), origBook.getCreators(), 
            origBook.getDescription(), 100, origBook.getGrade(), false);
        
        volume.update(0, newBook);
        //^ This should trigger a stat refresh in Volume then notify Series to do the same.

        //The new collection value should be 113
        assertEquals(17.7, queryExecutor.getTree(0).getTotalValue());
    }

    @Test
    void addComicBookAddsToExistingBranch() throws IOException {
        //Create new ComicBook
        ComicBook newBook = new ComicBook("A New Book", "1", LocalDate.now(), new String[3], new String[3], "", 0, 0, false);
        //Create SearchEntry with existing Subcategories
        SearchEntry newEntry = new SearchEntry(newBook, new Publisher("Acclaim Comics", null), 
            new Series("New Series", null), new Volume(1, null));
        //Create controller using prod json
        QueryExecutor queryExecutor = new QueryExecutor(new String[0]);
        System.out.println(queryExecutor.getTree(0));

        //Add Comic Book
        queryExecutor.addComicBook(newEntry, "", 0);

        //Get current collection
        ComixCollection collection = queryExecutor.getTree(0);
        

        //Test book exists
        assertEquals(newBook, collection.getChildNode(1)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(1));
    }

    @Test
    void addComicBookAddsToNonExistingBranch() throws IOException {
        //Create new ComicBook
        ComicBook newBook = new ComicBook("Another New Book", "1", LocalDate.now(), null, null, null, 0, 0, false);
        //Create SearchEntry with existing Subcategories
        SearchEntry newEntry = new SearchEntry(newBook, new Publisher("Fake Publisher", null), 
            new Series("Fake Series", null), new Volume(13, null));

        //Create controller using prod json
        CliPersonalController personalController = new CliPersonalController();
        System.out.println(personalController.getComixCollection());

        //Add Comic Book
        personalController.addComicBook(newEntry);

        //Get current collection
        ComixCollection collection = personalController.getComixCollection();
        

        //Test book exists
        assertEquals(newBook, collection.getChildNode(2)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(0));
    }

    @Test
    void removeComicBookRemovesFromExistingBranch() throws IOException {
        //Create controller using prod json
        CliPersonalController personalController = new CliPersonalController();

        //Find existing ComicBook
        Publisher publisher = personalController.getComixCollection().getChildNode(0);
        Series series = publisher.getChildNode(0);
        Volume volume = series.getChildNode(0);
        ComicBook comicBook = volume.getChildNode(0);
        SearchEntry searchEntry = new SearchEntry(comicBook, publisher, series, volume);

        //Remove Comic Book
        personalController.removeComicBook(searchEntry);

        //Get current collection
        ComixCollection collection = personalController.getComixCollection();
        

        //Test book not exists
        assertNotEquals(comicBook, collection.getChildNode(0)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(0));
    }

    @Test
    void removeComicBookFailsOnNonExistingBranch() throws IOException {
        //Create controller using prod json
        CliPersonalController personalController = new CliPersonalController();

        //Find existing ComicBook
        Publisher publisher = personalController.getComixCollection().getChildNode(0);
        Series series = publisher.getChildNode(0);
        Volume volume = series.getChildNode(0);
        ComicBook comicBook = volume.getChildNode(0);
        //Use fake Volume
        SearchEntry searchEntry = new SearchEntry(comicBook, publisher, series, new Volume(13, null));

        //Remove Comic Book
        boolean removalStatus = personalController.removeComicBook(searchEntry);

        //Test book not exists
        assertEquals(false, removalStatus);
    }

    @Test
    void updateComicBookOverwitesOnSameVolume() throws IOException {
        //Create controller using prod json
        CliPersonalController personalController = new CliPersonalController();

        //Find existing ComicBook
        Publisher publisher = personalController.getComixCollection().getChildNode(0);
        Series series = publisher.getChildNode(0);
        Volume volume = series.getChildNode(0);
        ComicBook comicBook = volume.getChildNode(0);
        SearchEntry searchEntry = new SearchEntry(comicBook, publisher, series, volume);

        //Make altered version of comic
        ComicBook newComicBook = new ComicBook("This book was updated", comicBook.getIssueNumber(), comicBook.getPublicationDate(), comicBook.getCreators(), comicBook.getPrincipleCharacters(), 
            comicBook.getDescription(), comicBook.getBaseValue(), comicBook.getGrade(), comicBook.getIsSlabbed());


        //Edit Comic Book
        personalController.editComicBook(searchEntry, new SearchEntry(newComicBook, publisher, series, volume));

        //Get current collection
        ComixCollection collection = personalController.getComixCollection();
        

        //Test original book not exists
        assertNotEquals(comicBook, collection.getChildNode(0)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(0));

        //Test new book exists
        assertEquals(newComicBook, collection.getChildNode(1)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(0));
    }

    @Test
    void updateComicBookOverwitesOnAlteredSubcategory() throws IOException {
        //Create controller using prod json
        CliPersonalController personalController = new CliPersonalController();

        //Find existing ComicBook
        Publisher publisher = personalController.getComixCollection().getChildNode(0);
        Series series = publisher.getChildNode(0);
        Volume volume = series.getChildNode(0);
        ComicBook comicBook = volume.getChildNode(0);
        SearchEntry searchEntry = new SearchEntry(comicBook, publisher, series, volume);

        //Make altered version of comic
        ComicBook newComicBook = new ComicBook("This book was updated", comicBook.getIssueNumber(), comicBook.getPublicationDate(), comicBook.getCreators(), comicBook.getPrincipleCharacters(), 
            comicBook.getDescription(), comicBook.getBaseValue(), comicBook.getGrade(), comicBook.getIsSlabbed());
        Series newSeries = new Series("New Series", null);


        //Edit Comic Book
        personalController.editComicBook(searchEntry, new SearchEntry(newComicBook, publisher, newSeries, volume));

        //Get current collection
        ComixCollection collection = personalController.getComixCollection();
        

        //Test original book not exists
        assertNotEquals(comicBook, collection.getChildNode(0)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(0));

        //Test new book exists
        assertEquals(newComicBook, collection.getChildNode(1)
            .getChildNode(0)
                .getChildNode(0)
                    .getChildNode(0));
    }
}
