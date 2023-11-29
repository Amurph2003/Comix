package com.rit.comix.subsystems.search.queryer;

import java.io.IOException;
import java.util.List;

import com.rit.comix.subsystems.hierarchy.CollectionNode;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.history.EntryRevision;

public class ReadWriteQueryerProxy implements Queryer {

    private Queryer queryExecutor;

    public ReadWriteQueryerProxy() {
        queryExecutor = new QueryExecutor(new String[0]);
    }

    private boolean checkAuthCookie(String authCookie) {
        //TODO
        return true;
    }

    @Override
    public ComixCollection addComicBook(SearchEntry book, String authCookie, int userId , boolean restoration) throws IOException {
        checkAuthCookie(authCookie);
        return queryExecutor.addComicBook(book, authCookie, userId, restoration);
    }

    @Override
    public ComixCollection removeComicBook(SearchEntry book, String authCookie, int userId , boolean restoration) throws IOException {
        checkAuthCookie(authCookie);
        return queryExecutor.removeComicBook(book, authCookie, userId, restoration);
    }

    @Override
    public ComixCollection editComicBook(SearchEntry book, SearchEntry newBook, String authCookie, int userId , boolean restoration)
            throws IOException {
        checkAuthCookie(authCookie);
        return queryExecutor.editComicBook(book, newBook, authCookie, userId, restoration);
    }

    @Override
    public SearchEntry getEntry(int index, int userId) {
        return queryExecutor.getEntry(index, userId);
    }

    @Override
    public List<SearchEntry> getFlatList(int userId) {
        return queryExecutor.getFlatList(userId);
    }

    @Override
    public CollectionNode getTree(int userId) throws IOException {
        return queryExecutor.getTree(userId);
    }

    @Override
    public void query() {
        queryExecutor.query();
    }

    @Override
    public void query(int userId, String authCookie) {
        checkAuthCookie(authCookie);
        queryExecutor.query(userId, authCookie);
    }


    @Override
    public List<SearchEntry> getResults() {
        return queryExecutor.getResults();
    }

    public List<SearchEntry> getDatabase() {
        return queryExecutor.getDatabase();
    }

    @Override
    public void restoreUndoableRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException {
        queryExecutor.restoreUndoableRevision(entryRevision, authCookie, userId);
    }

    @Override
    public void restoreUndoneRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException {
        queryExecutor.restoreUndoableRevision(entryRevision, authCookie, userId);
    }
    
}
