package com.rit.comix.subsystems.hierarchy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class ComicBookTest {
    //Tests follow the convention of: functionNameBehaviorExpectedResult

    @Test
    public void toStringOneDimensionalComixCollectionStringificationTrue(){
        ComixCollection collection = new ComixCollection();

        Publisher publisher = new Publisher("Gotham Comics", collection);
        collection.add(publisher);

        Series series = new Series("1a", publisher);
        publisher.add(series);

        Volume volume = new Volume(1, series);
        series.add(volume);

        ComicBook comicBook = new ComicBook("Batman: Year One", "1", LocalDate.now(), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false, volume);
        volume.add(comicBook);

        System.out.println(collection.toString());
    }

    @Test
    public void toStringNormalComixCollectionStringificationTrue(){
        ComixCollection collection = new ComixCollection();

        Publisher publisher1 = new Publisher("Gotham Comics", collection);
        collection.add(publisher1);

        Series series1 = new Series("1a", publisher1);
        publisher1.add(series1);

        Volume volume1 = new Volume(1, series1);
        series1.add(volume1);

        ComicBook comicBook1 = new ComicBook("Batman: Year One", "1", LocalDate.now(), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false, volume1);
        volume1.add(comicBook1);

        Publisher publisher2 = new Publisher("Gotham Comics", collection);
        collection.add(publisher2);

        Series series2 = new Series("1a", publisher2);
        publisher2.add(series2);

        Volume volume2 = new Volume(1, series2);
        series2.add(volume2);

        ComicBook comicBook2 = new ComicBook("Batman: Year One", "1", LocalDate.now(), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false, volume2);
        volume2.add(comicBook2);

        System.out.println(collection.toString());
    }

    @Test
    public void toStringOneSearchEntryStringificationTrue(){
        Publisher publisher = new Publisher("Gotham Comics", null);

        Series series = new Series("1a", null);

        Volume volume = new Volume(1, null);

        ComicBook comicBook = new ComicBook("Batman: Year One", "1", LocalDate.now(), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false, null);

        SearchEntry searchEntry = new SearchEntry(comicBook, publisher, series, volume);

        System.out.println(searchEntry.toString());
    }

    @Test
    public void toStringTwoSearchEntriesStringificationTrue(){
        Publisher publisher1 = new Publisher("Gotham Comics", null);

        Series series1 = new Series("1a", null);

        Volume volume1 = new Volume(1, null);

        ComicBook comicBook1 = new ComicBook("Batman: Year One", "1", LocalDate.now(), new String[]{"FrankMiller", "DavidMiller"}, new String[]{"Batman", "James Gordon"}, "Batman story.", 50, 9, false, null);

        SearchEntry searchEntry1 = new SearchEntry(comicBook1, publisher1, series1, volume1);

        Publisher publisher2 = new Publisher("Oofman Coomics", null);

        Series series2 = new Series("2a", null);

        Volume volume2 = new Volume(2, null);

        ComicBook comicBook2 = new ComicBook("Spooderman: Year One", "2", LocalDate.now(), new String[]{"Jonathan Spooderson", "Maximillian Sanicson"}, new String[]{"Spooderman", "Sanic"}, "Batman story.", 50, 9, false, null);

        SearchEntry searchEntry2 = new SearchEntry(comicBook2, publisher2, series2, volume2);

        searchEntry1.toString();
        searchEntry2.toString();

        System.out.println(searchEntry1);
        System.out.println(searchEntry2);
    }

}