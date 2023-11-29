package com.rit.comix.subsystems.search.queryer;

import com.rit.comix.subsystems.hierarchy.CollectionNode;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.history.EntryRevision;

import java.io.IOException;
import java.util.List;

public interface Queryer {
    public ComixCollection addComicBook(SearchEntry book, String authCookie, int userId, boolean restoration) throws IOException;
    public ComixCollection removeComicBook(SearchEntry book, String authCookie, int userId, boolean restoration) throws IOException;
    public ComixCollection editComicBook(SearchEntry book, SearchEntry newBook, String authCookie, int userId, boolean restoration) throws IOException;
    public SearchEntry getEntry(int index, int userId);
    public List<SearchEntry> getFlatList(int userId);
    public CollectionNode getTree(int userId) throws IOException;
    public void query();
    public void query(int userId, String authCookie);
    public List<SearchEntry> getResults();
    public List<SearchEntry> getDatabase();
    public void restoreUndoableRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException;
    public void restoreUndoneRevision(EntryRevision entryRevision, String authCookie, int userId) throws IOException;
}
