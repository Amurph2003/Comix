package com.rit.comix.subsystems.sort;

import java.util.List;
import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class ComicBookList {

    private List<SearchEntry> searchResults;
    private ComicBookSorter defaultSorter;

    public ComicBookList(List<SearchEntry> searchResults) {
        this.searchResults = searchResults;
        this.defaultSorter = new SortBySeriesTitleHierarchy();
    }
    
    public void doSort() {
        defaultSorter.performSort(searchResults);
    }

    public void doSort(ComicBookSorter comicBookSorter) {
        comicBookSorter.performSort(searchResults);
    }

}
