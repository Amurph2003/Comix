package com.rit.comix.subsystems.sort;

import java.util.List;
import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class SearchSortSystem {
    
    private ComicBookList sortContext;
    
    public SearchSortSystem(List<SearchEntry> searchResults){
        this.sortContext = new ComicBookList(searchResults);
    }

    public void sort(String args){
        ComicBookSorter comicBookSorter = SorterFactory.createSorter(args);
        sortContext.doSort(comicBookSorter);
    }

    public void sort(){
        sortContext.doSort();
    }
}
