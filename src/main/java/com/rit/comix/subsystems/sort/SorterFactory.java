package com.rit.comix.subsystems.sort;

public class SorterFactory {
    public static ComicBookSorter createSorter(String arg) {
        if(arg.equals("publicationDate")) {
            return new SortByPublicationDate();
        }
        
        return null;
    }
}
