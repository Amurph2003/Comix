package com.rit.comix.subsystems.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class SortBySeriesTitleHierarchy implements ComicBookSorter {
    @Override
    public void performSort(List<SearchEntry> entries) {

        Collections.sort(entries, new Comparator<SearchEntry>() {
            @Override
            public int compare(SearchEntry se1, SearchEntry se2) {
                int seriesTitleComparison = se1.getSeries().getSeriesNumber().compareTo(se2.getSeries().getSeriesNumber());
                if (seriesTitleComparison != 0) {
                    return seriesTitleComparison;
                }
                int volumeComparison = Integer.compare((se1.getVolume().getVolumeNumber()), (se2.getVolume().getVolumeNumber()));
                if (volumeComparison != 0) {
                    return volumeComparison;
                }
                int issueNumberComparison = se1.getComicBook().getIssueNumber().compareTo(se2.getComicBook().getIssueNumber());
                    return issueNumberComparison;
            }
        });        

    }

}