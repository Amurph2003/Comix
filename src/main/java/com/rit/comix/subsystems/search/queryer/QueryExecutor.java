package com.rit.comix.subsystems.search.queryer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;
import com.rit.comix.subsystems.history.EntryRevision;
import com.rit.comix.subsystems.history.RevisionHistory;
import com.rit.comix.subsystems.search.CreatorDecorator;
import com.rit.comix.subsystems.search.IssueNumberDecorator;
import com.rit.comix.subsystems.search.PublicationDateDecorator;
import com.rit.comix.subsystems.search.PublisherDecorator;
import com.rit.comix.subsystems.search.Search;
import com.rit.comix.subsystems.search.SearchOptions;
import com.rit.comix.subsystems.search.SeriesTitleDecorator;
import com.rit.comix.subsystems.search.StoryTitleDecorator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryExecutor implements Queryer {
    //Why is everything in Spring a broken mess
    // private final String DATABASE_PATH = System.getProperty("database.path");
    // private final String COLLECTIONS_PATH = System.getProperty("collections.prefix");

    private final String DATABASE_PATH = "database.json";
    private final String COLLECTIONS_PATH = "personalCollection-";

    // fields: series, title, principle characters, creator names, description, issue number
    // publisher, publication date
    protected static final List<String> SEARCH_FIELDS = Arrays.stream(new String[]
            {"series", "title", "principle_characters", "creator",
            "description", "issue", "publisher", "publication_date"}).toList();

    protected static final List<String> PARTIAL_EXACT = Arrays.stream(new String[]{"partial", "exact"}).toList();

    protected String searchField = "";
    protected String matchOpt = "";
    protected String searchTerm = "";
    protected boolean wrongCommand = false;

    protected BufferedReader reader;
    protected BufferedWriter writer;
    protected List<SearchEntry> queryResults;
    protected Search searchOptions;

    protected List<String> res;

    @Autowired
    private RevisionHistory revisionHistory;

    public QueryExecutor(){}

    public QueryExecutor(String[] args) {
        int argSize = args.length;
        if (argSize < 2) {
            System.out.println("Usage: search <search term> [-field] [-partial or -exact]");
            wrongCommand = argSize < 2;
        }
        else {
            searchTerm = args[1].replace("_", " ");

            if (argSize == 2) {
                // search Hulk
            }
            else if (argSize == 3) {
                String option = args[2].toLowerCase();
                if (SEARCH_FIELDS.contains(option)) {
                    // search field specified
                    searchField = option;
                }
                else if (PARTIAL_EXACT.contains(option)) {
                    matchOpt = option;
                }
                else {
                    System.out.println("Usage: search <search term> [-field] [-partial or -exact]");
                }
            }
            else if (argSize == 4) {
                String field = args[2].toLowerCase();
                String matchOpt = args[3].toLowerCase();
                if (SEARCH_FIELDS.contains(field) && PARTIAL_EXACT.contains(matchOpt)) {
                    // parse search string here
                    searchField = field;
                    this.matchOpt = matchOpt;
                }
                else {
                    System.out.println("Usage: search <search term> [-field] [-partial or -exact]");
                }
            }
            else {
                System.out.println("Usage: search <search term> [-field] [-partial or -exact]");
            }
        }
    }

    /**
     * method that generates a flat personal collection json file
     * @param userId
     * @throws IOException
     */
    public String generateFlatCollection(int userId) {
        try {
            ComixCollection comixCollection = generateCollection(userId);
            comixCollection.writeFlatCollection(COLLECTIONS_PATH + userId + "FLAT.json" );
            return COLLECTIONS_PATH + userId + "FLAT.json";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Pull PersonalCollection from personalCollection.json
     * @throws IOException on failure to access personalCollection.json
     */
    public ComixCollection generateCollection(int userId) throws IOException {
        File jsonFile = new File(COLLECTIONS_PATH + userId + ".json");

        //Check file validity
        if (!jsonFile.exists() || jsonFile.isDirectory()) {
            //Bad file; throw exception
            throw new IOException(COLLECTIONS_PATH + userId + ".json" + " is not a file.");
        }

        //Check file is not empty
        BufferedReader br = new BufferedReader(new FileReader(COLLECTIONS_PATH + userId + ".json"));     
        if (br.readLine() == null) {
            br.close();
            //File empty; create empty collection and finish
            return new ComixCollection();
        }
        br.close();

        ObjectMapper collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());

        ComixCollection comixCollection = collectionMapper.readValue(new File(COLLECTIONS_PATH + userId + ".json"), ComixCollection.class);
        comixCollection.setSearchListPath(COLLECTIONS_PATH + userId + "-SearchList.json");

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
        return comixCollection;
    }

    /**
     * Pull List from personalCollectionSearchList.json
     * @throws IOException on failure to access personalCollectionSearchList.json
     */
    private List<SearchEntry> generateSearchList(int userId) {
        ObjectMapper collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());

        try {
            return collectionMapper.readValue(new File(COLLECTIONS_PATH + userId + "-SearchList.json"), ArrayList.class);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Overwrite json with comixCollection in memory
     * @throws IOException failure to access json
     */
    public void saveCollection(ComixCollection collection, int userId) throws IOException{
        ObjectMapper collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());
        collectionMapper.writeValue(new File(COLLECTIONS_PATH + userId + ".json"), collection);
    }

    
    /**
     * @param bookData Comic Book data to add
     * @throws IOException on failure to overwrite json
     */
    public ComixCollection addComicBook(SearchEntry book, String authCookie, int userId, boolean restoration) throws IOException {
        ComixCollection comixCollection = generateCollection(userId);

        //Get Matching Publisher
        Publisher matchedPublisher = null;
        //Find matching Publisher, go to if found
        for (Publisher currentPublisher : comixCollection.getChildren()) {
            if (book.getPublisher().equals(currentPublisher)) {
                matchedPublisher = currentPublisher;
                break;
            }
        }
        //If not found, insert Publisher
        if (matchedPublisher == null) {
            book.getPublisher().setParentNode(comixCollection);
            comixCollection.add(book.getPublisher());
            //Get bookData.publisher from tree
            matchedPublisher = comixCollection.getChildren().get(comixCollection.getChildren().size() - 1);
        }

        //Get Matching Series
        Series matchedSeries = null;
        //Find matching Series, go to if found
        for (Series currentSeries : matchedPublisher.getChildren()) {
            if (book.getSeries().equals(currentSeries)) {
                matchedSeries = currentSeries;
                break;
            }
        }
        //If not found, insert Series
        if (matchedSeries == null) {
            book.getSeries().setParentNode(matchedPublisher);
            matchedPublisher.add(book.getSeries());
            //Get bookData.series from tree
            matchedSeries = matchedPublisher.getChildren().get(matchedPublisher.getChildren().size() - 1);
        }

        //Get Matching Volume
        Volume matchedVolume = null;
        //Find matching Volume, go to if found
        for (Volume currentVolume : matchedSeries.getChildren()) {
            if (book.getVolume().equals(currentVolume)) {
                matchedVolume = currentVolume;
                break;
            }
        }
        //If not found, insert Volume
        if (matchedVolume == null) {
            book.getVolume().setParentNode(matchedSeries);
            matchedSeries.add(book.getVolume());
            //Get bookData.volume from tree
            matchedVolume = matchedSeries.getChildren().get(matchedSeries.getChildren().size() - 1);
        }

        //Add book to branch
        book.getComicBook().setParentNode(matchedVolume);
        matchedVolume.add(book.getComicBook());
        saveCollection(comixCollection, userId);

        if(restoration == false){
            RevisionHistory.getInstance().storeRevision(null, book);
        }

        return comixCollection;
    }

    /**
     * @param book Comic Book data to remove
     * @return true on success; false on data not found
     * @throws IOException
     */
    public ComixCollection removeComicBook(SearchEntry book, String authCookie, int userId, boolean restoration) throws IOException {
        ComixCollection comixCollection = generateCollection(userId);
        //Get Matching Publisher
        Publisher matchedPublisher = null;
        //Find matching Publisher, go to if found
        for (Publisher currentPublisher : comixCollection.getChildren()) {
            if (book.getPublisher().equals(currentPublisher)) {
                matchedPublisher = currentPublisher;
            }
        }
        //If not found, fail
        if (matchedPublisher == null) {
            return null;
        }

        //Get Matching Series
        Series matchedSeries = null;
        //Find matching Series, go to if found
        for (Series currentSeries : matchedPublisher.getChildren()) {
            if (book.getSeries().equals(currentSeries)) {
                matchedSeries = currentSeries;
            }
        }
        //If not found, fail
        if (matchedSeries == null) {
            return null;
        }

        //Get Matching Volume
        Volume matchedVolume = null;
        //Find matching Volume, go to if found
        for (Volume currentVolume : matchedSeries.getChildren()) {
            if (book.getVolume().equals(currentVolume)) {
                matchedVolume = currentVolume;
            }
        }
        //If not found, fail
        if (matchedVolume == null) {
            return null;
        }

        //Find matching ComicBook, remove if found
        ComicBook matchedBook = null;
        for (ComicBook currentBook : matchedVolume.getChildren()) {
            if (book.getComicBook().equals(currentBook)) {
                matchedBook = currentBook;
                break;
            }
        }
        //If not found, fail
        if (matchedBook == null) {
            return null;
        }
        //Else remove
        else {
            matchedVolume.remove(matchedBook);
        }

        //Prune subcategories with no children after removal
        if (matchedVolume.getChildren().size() == 0) {
            matchedSeries.remove(matchedVolume);

            if (matchedSeries.getChildren().size() == 0) {
                matchedPublisher.remove(matchedSeries);
    
                if (matchedPublisher.getChildren().size() == 0) {
                    comixCollection.remove(matchedPublisher);
                }
            }
        }

        if(restoration == false){
            RevisionHistory.getInstance().storeRevision(book, null);
        }

        saveCollection(comixCollection, userId);
        return comixCollection;
    }

    /**
     * @param book original book data to overwrite
     * @param altered new book data
     * @throws IOException on failure to overwrite json
     */
    public ComixCollection editComicBook(SearchEntry book, SearchEntry altered, String authCookie, int userId, boolean restoration) throws IOException {
        ComixCollection comixCollection = null;

        if(altered == null){
            comixCollection = removeComicBook(book, authCookie, userId, restoration);
            comixCollection = null;
        }
        else if(book == null){
            comixCollection = addComicBook(altered, authCookie, userId, restoration);
        } else {
            comixCollection = removeComicBook(book, authCookie, userId, true);
            comixCollection = addComicBook(altered, authCookie, userId, restoration);
        }

        return comixCollection;
    } 

    public ComixCollection getComixCollection(String authCookie, int userId) throws IOException {
        return generateCollection(userId);
    }

    public SearchEntry getEntry(int index, int userId) {
        return generateSearchList(userId).get(index);
    }
    public List<SearchEntry> getFlatList(int userId) {
        return generateSearchList(userId);
    }

    public ComixCollection getTree(int userId) throws IOException {
        return generateCollection(userId);
    }

    public List<SearchEntry> getResults() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        return om.convertValue(getRes(), new TypeReference<List<SearchEntry>>() {});
    }

    private ReadContext createContext(String filepath) {
        try {
            String json = Files.readString(Paths.get(filepath));
            ReadContext ctx = JsonPath.parse(json);
            return ctx;
        }
        catch (IOException ioe) {
            System.out.println("Could not locate the db file... terminating");
            System.exit(1);
            return null;
        }
    }

    public void query() {
        if (wrongCommand) return;
        // NOTE: THE ORDER OF CREATION HERE IS IMPORTANT
        searchOptions = new SearchOptions();
        searchOptions = new StoryTitleDecorator(searchOptions, searchField, searchTerm, true, false, matchOpt);
        searchOptions = new SeriesTitleDecorator(searchOptions, searchField, searchTerm, false, false, matchOpt);
        searchOptions = new PublisherDecorator(searchOptions, searchField, searchTerm, false, false, matchOpt);
        searchOptions = new PublicationDateDecorator(searchOptions, searchField, searchTerm, false, false);
        searchOptions = new IssueNumberDecorator(searchOptions, searchField, searchTerm, false, false, matchOpt);
        searchOptions = new CreatorDecorator(searchOptions, searchField, searchTerm, false, true, matchOpt);

        // actual search
        String jsonPathQuery = searchOptions.getSearchString();
        ReadContext ctx = createContext(DATABASE_PATH);
        res = ctx.read(jsonPathQuery);
    }

    public void query(int id, String authCookie) {
        if (wrongCommand) return;
        // NOTE: THE ORDER OF CREATION HERE IS IMPORTANT
        searchOptions = new SearchOptions();
        searchOptions = new StoryTitleDecorator(searchOptions, searchField, searchTerm, true, false, matchOpt);
        searchOptions = new SeriesTitleDecorator(searchOptions, searchField, searchTerm, false, false, matchOpt);
        searchOptions = new PublisherDecorator(searchOptions, searchField, searchTerm, false, false, matchOpt);
        searchOptions = new PublicationDateDecorator(searchOptions, searchField, searchTerm, false, false);
        searchOptions = new IssueNumberDecorator(searchOptions, searchField, searchTerm, false, false, matchOpt);
        searchOptions = new CreatorDecorator(searchOptions, searchField, searchTerm, false, true, matchOpt);

        // actual search
        String jsonPathQuery = searchOptions.getSearchString();
        ReadContext ctx = createContext(COLLECTIONS_PATH + id + "-SearchList.json");
        res = ctx.read(jsonPathQuery);
    }

    public List<String> getRes() {
        return  res;
    }

    public List<SearchEntry> getDatabase() {
        ObjectMapper collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());

        try {
            return collectionMapper.readValue(new File(DATABASE_PATH), ArrayList.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void restoreUndoableRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException {
        int entryRevisionIndex = 0;
        for (EntryRevision entryRevisionInForLoop : RevisionHistory.getInstance().getUndoableChanges()){
            if(entryRevisionInForLoop.equals(entryRevision)){
                break;
            }
            entryRevisionIndex++;
        }

        int reverseIndex = RevisionHistory.getInstance().getUndoableChanges().size();
        EntryRevision entryRevisionInWhileLoop = entryRevision;
        while(reverseIndex != entryRevisionIndex){
            entryRevisionInWhileLoop = RevisionHistory.getInstance().getUndoableChanges().get(reverseIndex - 1);
            editComicBook(entryRevisionInWhileLoop.getNewEntry(), entryRevisionInWhileLoop.getOldEntry(), authCookie, userId, true);
            RevisionHistory.getInstance().updateRevisionListsWhenRestoringUndoable(entryRevisionInWhileLoop);
            reverseIndex--;
        }

        // editComicBook(entryRevision.getNewEntry(), entryRevision.getOldEntry(), authCookie, userId);
        // RevisionHistory.getInstance().updateRevisionListsWhenRestoringUndoable(entryRevision);
    }

    public void restoreUndoneRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException {
        int entryRevisionIndex = 0;
        for (EntryRevision entryRevisionInForLoop : RevisionHistory.getInstance().getUndoneChanges()){
            if(entryRevisionInForLoop.equals(entryRevision)){
                break;
            }
            entryRevisionIndex++;
        }

        int reverseIndex = RevisionHistory.getInstance().getUndoneChanges().size();
        EntryRevision entryRevisionInWhileLoop = entryRevision;
        while(reverseIndex != entryRevisionIndex){
            entryRevisionInWhileLoop = RevisionHistory.getInstance().getUndoneChanges().get(reverseIndex - 1);
            editComicBook(entryRevisionInWhileLoop.getNewEntry(), entryRevisionInWhileLoop.getOldEntry(), authCookie, userId, true);
            RevisionHistory.getInstance().updateRevisionListsWhenRestoringUndoable(entryRevisionInWhileLoop);
            reverseIndex--;
        }

        // editComicBook(entryRevision.getNewEntry(), entryRevision.getOldEntry(), authCookie, userId);
        // RevisionHistory.getInstance().updateRevisionListsWhenRestoringUndone(entryRevision);
    }
}