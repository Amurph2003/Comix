package com.rit.comix.subsystems.sort;

import java.util.Comparator;
import java.util.List;

import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class SortByPublicationDate implements ComicBookSorter {
    @Override
    public void performSort(List<SearchEntry> entries) {
        entries.sort(Comparator.comparing(entry -> ((SearchEntry) entry).getComicBook().getPublicationDate()));
    }
}