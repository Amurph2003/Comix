package com.rit.comix.subsystems.search.queryer;

import java.io.IOException;
import java.util.List;

import com.rit.comix.subsystems.hierarchy.CollectionNode;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.history.EntryRevision;

public class ReadQueryerProxy implements Queryer {
    private Queryer queryExecutor;

    public ReadQueryerProxy() {
        queryExecutor = new QueryExecutor(new String[0]);
    }

    private boolean checkAuthCookie(String authCookie) {
        //TODO
        return true;
    }

    @Override
    public ComixCollection addComicBook(SearchEntry book, String authCookie, int userId, boolean restoration) throws IOException {
        return null;
    }

    @Override
    public ComixCollection removeComicBook(SearchEntry book, String authCookie, int userId, boolean restoration) throws IOException {
        return null;
    }

    @Override
    public ComixCollection editComicBook(SearchEntry book, SearchEntry newBook, String authCookie, int userId, boolean restoration)
            throws IOException {
        return null;
    }

    @Override
    public SearchEntry getEntry(int index, int userId) {
        checkAuthCookie(null);
        return queryExecutor.getEntry(index, userId);
    }

    @Override
    public List<SearchEntry> getFlatList(int userId) {
        checkAuthCookie(null);
        return queryExecutor.getFlatList(userId);
    }

    @Override
    public CollectionNode getTree(int userId) throws IOException {
        checkAuthCookie(null);
        return queryExecutor.getTree(userId);
    }

    @Override
    public void query() {
        queryExecutor.query();
    }

    @Override
    public void query(int userId, String authCookie) {
        return;
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

    }

    @Override
    public void restoreUndoneRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException {

    }
}
