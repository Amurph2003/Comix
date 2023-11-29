package com.rit.comix.subsystems.sort;

import java.util.List;

import com.rit.comix.subsystems.hierarchy.SearchEntry;

public interface ComicBookSorter {
        void performSort(List<SearchEntry> entries);
}
