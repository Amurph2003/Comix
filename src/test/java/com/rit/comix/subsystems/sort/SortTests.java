package com.rit.comix.subsystems.sort;

import org.junit.jupiter.api.Test;

import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SortTests {
    @Test
    public void PublicationDateSortTest() {
        List<SearchEntry> entries = new ArrayList<>();
        ComixCollection collection = new ComixCollection();

        ComicBook comicBook1 = new ComicBook("Spiderman: Year One","1", LocalDate.of(1992, 01, 14), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false);
        Publisher publisher1 = new Publisher("Gotham Comics", collection);
        Series series1 = new Series("Spiderman: 1", publisher1);
        Volume volume1 = new Volume(2, series1);

        ComicBook comicBook2 = new ComicBook("Batman: Year One","2", LocalDate.of(1991, 02, 15), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false);
        Publisher publisher2 = new Publisher("Gotham Comics", collection);
        Series series2 = new Series("Spiderman: 1", publisher2);
        Volume volume2 = new Volume(1, series2);

        ComicBook comicBook3 = new ComicBook("WonderWoman: Year One","1", LocalDate.of(1990, 03, 16), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false);
        Publisher publisher3 = new Publisher("Gotham Comics", collection);
        Series series3 = new Series("Spiderman: 1", publisher3);
        Volume volume3 = new Volume(1, series3);

        ComicBook comicBook4 = new ComicBook("WonderWoman: Year One","2", LocalDate.of(1990, 03, 18), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false);
        Publisher publisher4 = new Publisher("Gotham Comics", collection);
        Series series4 = new Series("Batman: 2", publisher4);
        Volume volume4 = new Volume(4, series4);

        ComicBook comicBook5 = new ComicBook("WonderWoman: Year One","3", LocalDate.of(1989, 04, 21), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false);
        Publisher publisher5 = new Publisher("Gotham Comics", collection);
        Series series5 = new Series("Batman: 3", publisher5);
        Volume volume5 = new Volume(4, series5);

        SearchEntry entry1 = new SearchEntry(comicBook1, publisher1, series1, volume1);
        SearchEntry entry2 = new SearchEntry(comicBook2, publisher2, series2, volume2);
        SearchEntry entry3 = new SearchEntry(comicBook3, publisher3, series3, volume3);
        SearchEntry entry4 = new SearchEntry(comicBook4, publisher4, series4, volume4);
        SearchEntry entry5 = new SearchEntry(comicBook5, publisher5, series5, volume5);

        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);
        entries.add(entry4);
        entries.add(entry5);
        
        SearchSortSystem searchSortSystem = new SearchSortSystem(entries);
        searchSortSystem.sort();

        // check that the list is sorted correctly by default which is title hierarchy sort
        assertEquals(entries.get(0), entry4);
        assertEquals(entries.get(1), entry5);
        assertEquals(entries.get(2), entry3);
        assertEquals(entries.get(3), entry2);
        assertEquals(entries.get(4), entry1);

        searchSortSystem.sort("publicationDate");

        // check that the list is sorted correctly by publication date
        assertEquals(entries.get(0), entry5);
        assertEquals(entries.get(1), entry3);
        assertEquals(entries.get(2), entry4);
        assertEquals(entries.get(3), entry2);
        assertEquals(entries.get(4), entry1);
    }    
}
