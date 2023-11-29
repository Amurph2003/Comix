package com.rit.comix.searchsort;

import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.search.queryer.QueryExecutor;
import com.rit.comix.subsystems.search.queryer.Queryer;
import com.rit.comix.subsystems.sort.ComicBookList;
import com.rit.comix.subsystems.sort.ComicBookSorter;
import com.rit.comix.subsystems.sort.SorterFactory;

import java.util.List;

public class SearchSortSystem {
    private Queryer queryer;
    private ComicBookList sortContext;
    private List<SearchEntry> searchResults;

    public SearchSortSystem() {
        this.sortContext = new ComicBookList(searchResults);
    };

    public void performSearch(String args) {
        System.out.println("Your search query: " + args);
        queryer = new QueryExecutor(args.split(" "));
        queryer.query();
        searchResults = queryer.getResults();
        sortContext = new ComicBookList(searchResults);
        sort();
    }

    public void performSearchOnCollection(String args, int userId, String authCookie) {
        System.out.println("Your search query: " + args);
        queryer = new QueryExecutor(args.split(" "));
        queryer.query(userId, authCookie);
        searchResults = queryer.getResults();
        sortContext = new ComicBookList(searchResults);
        sort();
    }

    public List<SearchEntry> returnSearchResults() {
        return searchResults;
    }

    public void sort(String args) {
        ComicBookSorter comicBookSorter = SorterFactory.createSorter(args);
        sortContext.doSort(comicBookSorter);
    }
    
    public void sort() {
        sortContext.doSort();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SearchSortSystem) {
            return searchResults.equals(((SearchSortSystem) obj).searchResults);
        }
        return false;
    }

    public void setSearchResults(List<SearchEntry> searchResults){
        this.searchResults = searchResults;
    }
}
