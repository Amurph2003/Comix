package com.rit.comix.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rit.comix.searchsort.SearchSortSystem;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.history.EntryRevision;
import com.rit.comix.subsystems.history.RevisionHistory;
import com.rit.comix.subsystems.search.queryer.Queryer;
import com.rit.comix.subsystems.search.queryer.ReadWriteQueryerProxy;
import com.rit.user.User;

@RestController
public class DataController{
    private Queryer crudQueryer;
    private SearchSortSystem searchSortSystem;
    private User user;

    private ObjectMapperSingleton objectMapperSingleton;

    public DataController() {
        //Instantiate the crud queryer and searchSortSystem
        this.crudQueryer = new ReadWriteQueryerProxy();
        this.searchSortSystem = new SearchSortSystem();
        this.objectMapperSingleton = ObjectMapperSingleton.instance();
    }

    //Check username against user service, sets user to match
    private void getUserByUsername(String username) {
        user = UserService.findByUsername(username);
    }

    /**
     * Sets user if given values match database
     * @param username proposed username
     * @param password proposed password
     * @return String status
     */
    @CrossOrigin()
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        getUserByUsername(username);
        if (user != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            try {
                return ResponseEntity.ok(objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("WWW-Authenticate", "Basic realm=\"User Visible Realm\"");
            return new ResponseEntity<>("Invalid username or password", headers, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @param args SearchOption arguments
     * @return matching results
     * @throws IOException cannot access database file
     */
    @CrossOrigin()
    @GetMapping("/database")
    public ResponseEntity<String> getDatabaseJson(@RequestParam("searchArgs") String searchArgs, @RequestParam("sortOption") String sortOption) throws IOException {
        //TODO REMOVE THIS
        user = UserService.findByUsername("testUser1");

        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<SearchEntry> results;
        if (searchArgs.equals("undefined")) {
            if(sortOption.equals("undefined")) {
                results = crudQueryer.getDatabase();
            }
            //publication date
            else if(sortOption.equals("publicationDate")){
                results = crudQueryer.getDatabase();
                searchSortSystem.setSearchResults(results);
                searchSortSystem.sort("publicationDate");
                results = searchSortSystem.returnSearchResults();
            }
            //title sort
            else {
                results = crudQueryer.getDatabase();
                searchSortSystem.setSearchResults(results);
                searchSortSystem.sort();
                results = searchSortSystem.returnSearchResults();
            } 
        }
        //search
        else
        {
            //title sort
            if(sortOption.equals("title")) {
                searchSortSystem.performSearch(searchArgs);
                searchSortSystem.sort();
                results = searchSortSystem.returnSearchResults();
            }
            //publication date
            else{
                searchSortSystem.performSearch(searchArgs);
                searchSortSystem.sort("publicationDate");
                results = searchSortSystem.returnSearchResults();
            }
        }
        try {
            System.out.println(results);
            return ResponseEntity.ok(objectMapper.writeValueAsString(results));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * @param searchEntry information to add to collection
     * @param sessionKey user's session key for authentication
     * @return status message
     */
    @CrossOrigin()
    @PutMapping("/collection") 
    public ResponseEntity<String> addComicBook(@RequestBody String se, @RequestHeader String sessionKey) {
        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        if (!sessionKey.equals(user.getAuthCookie())) return new ResponseEntity<String>("Not Authorized", HttpStatus.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("d1");
        try {
            SearchEntry searchEntry = objectMapper.readValue(se, SearchEntry.class);
            System.out.println("23");
            crudQueryer.addComicBook(searchEntry, sessionKey, user.getId(), false);

            return ResponseEntity.ok("Added");

        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * @param args SearchOption arguments
     * @param sessionKey user's session key for authentication
     * @return matching results
     * @throws IOException
     */
    @CrossOrigin()
    @GetMapping("/collection")
    public ResponseEntity<String> getCollectionJson(@RequestParam("searchArgs") String searchArgs, @RequestParam("sortOption") String sortOption, @RequestHeader String sessionKey) throws IOException {
        //TODO REMOVE THIS
        user = UserService.findByUsername("testUser1");

        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<SearchEntry> results;

        if (searchArgs.equals("undefined")) {
            if(sortOption.equals("undefined")) {
                results = crudQueryer.getFlatList(user.getId());
            }
            //publication date
            else if(sortOption.equals("publicationDate")){
                results = crudQueryer.getFlatList(user.getId());
                searchSortSystem.setSearchResults(results);
                searchSortSystem.sort("publicationDate");
                results = searchSortSystem.returnSearchResults();
            }
            //title sort
            else {
                results = crudQueryer.getFlatList(user.getId());
                searchSortSystem.setSearchResults(results);
                searchSortSystem.sort();
                results = searchSortSystem.returnSearchResults();
            } 
        }
        //search
        else
        {
            //title sort
            if(sortOption.equals("title")) {
                searchSortSystem.performSearchOnCollection(searchArgs, user.getId(), sessionKey);
                searchSortSystem.sort();
                results = searchSortSystem.returnSearchResults();
            }
            //publication date
            else{
                searchSortSystem.performSearchOnCollection(searchArgs, user.getId(), sessionKey);
                searchSortSystem.sort("publicationDate");
                results = searchSortSystem.returnSearchResults();
            }
        }
        try {
            System.out.println(results);
            String myResults = objectMapper.writeValueAsString(results);
            // if(myResults.equals("[]")) {
            //     return ResponseEntity.ok(searchArgs);
            // }
            
            return ResponseEntity.ok(myResults);
            
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * @param searchEntry information to remove from collection
     * @param sessionKey user's session key for authentication
     * @return status message
     */
    @CrossOrigin()
    @DeleteMapping("/collection") 
    public ResponseEntity<String> removeComicBook(@RequestBody String se, @RequestHeader String sessionKey) throws JsonMappingException, JsonProcessingException {
        System.out.println(se);
        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        if (!sessionKey.equals(user.getAuthCookie())) return new ResponseEntity<String>("Not Authorized", HttpStatus.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("d1");
        try {
            SearchEntry searchEntry = objectMapper.readValue(se, SearchEntry.class);
            crudQueryer.removeComicBook(searchEntry, sessionKey, user.getId(), false);
            System.out.println("d2");
            return ResponseEntity.ok("Removed");

        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * @param searchEntry information to add
     * @param newSearchEntry information to remove
     * @param sessionKey user's session key for authentication
     * @return status message
     */
    @CrossOrigin()
    @PostMapping("/collection") 
    public ResponseEntity<String> editComicBook(@RequestBody SearchEntry searchEntry, @RequestBody SearchEntry newSearchEntry, @RequestHeader String sessionKey) {
        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        if (!sessionKey.equals(user.getAuthCookie())) return new ResponseEntity<String>("Not Authorized", HttpStatus.UNAUTHORIZED);
        
        try {
            crudQueryer.editComicBook(searchEntry, newSearchEntry, sessionKey, user.getId(), false);

            return ResponseEntity.ok("Replaced");

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/history/undoable")
    public ResponseEntity<String> getHistoryUndoableJson(@RequestHeader String sessionKey) throws IOException {
        //TODO REMOVE THIS
        user = UserService.findByUsername("testUser1");

        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        System.out.println("1");
        ObjectMapper objectMapper = new ObjectMapper();
        List<EntryRevision> undoableChanges = RevisionHistory.getInstance().getUndoableChanges();
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(undoableChanges));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/history/undone")
    public ResponseEntity<String> getHistoryUndoneJson(@RequestHeader String sessionKey) throws IOException {
        //TODO REMOVE THIS
        user = UserService.findByUsername("testUser1");

        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        System.out.println("1");
        ObjectMapper objectMapper = new ObjectMapper();
        List<EntryRevision> undoneChanges = RevisionHistory.getInstance().getUndoneChanges();
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(undoneChanges));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Do Undo
    @PostMapping("/history/undone")
    public ResponseEntity<String> undo(@RequestHeader String sessionKey) throws IOException {
        //TODO REMOVE THIS
        user = UserService.findByUsername("testUser1");

        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
        System.out.println("1");

        crudQueryer.restoreUndoableRevision(RevisionHistory.getInstance().getUndoableChanges().get(0), "authCookie", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        
        return ResponseEntity.ok("Undone");
    }

    //Do Redo
    @PostMapping("/history/undoable")
    public ResponseEntity<String> redo(@RequestHeader String sessionKey) throws IOException {
        //TODO REMOVE THIS
        user = UserService.findByUsername("testUser1");
    
        if (user == null) return new ResponseEntity<String>("Please log in", HttpStatus.UNAUTHORIZED);
            System.out.println("1");
    
            crudQueryer.restoreUndoneRevision(RevisionHistory.getInstance().getUndoableChanges().get(0), "authCookie", 1);
          
            return ResponseEntity.ok("Redone");
        }
}