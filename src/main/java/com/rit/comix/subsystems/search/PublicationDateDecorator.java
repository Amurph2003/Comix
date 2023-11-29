package com.rit.comix.subsystems.search;

import java.text.SimpleDateFormat;
import java.util.Date;

// dates in the data file go [year, month, day]
// dates in the input will be 2001/12/30
public class PublicationDateDecorator extends SearchDecorator{
    private String searchStringSegment = "[";


    public PublicationDateDecorator(Search searchOption, String fieldOpt, String inputDate, boolean first, boolean last) {
        super.searchOption = searchOption;
        super.firstDecorator = first;
        super.lastDecorator = last;
        super.fieldOpt = fieldOpt;
        // date format validation
        if (fieldOpt.equals("")) {
            searchStringSegment = "@.comicBook.publicationDate == []";
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        try {
            // used to check if date follows format
            Date date = dateFormat.parse(inputDate);

            String[] dateParts = inputDate.split("/");
            StringBuilder seg = new StringBuilder(searchStringSegment);
            for (int i = 0; i < dateParts.length; i++) {
                seg.append(dateParts[i]);
                if (i != dateParts.length-1) seg.append(',');
            }
            seg.append(']');
            searchStringSegment = "@.comicBook.publicationDate == " + seg;
        } catch (Exception e) {
            if (fieldOpt.equals("publication_date")) {
                System.out.println("Please specify the date for searching in the format yyyy/MM/dd");
            }
            searchStringSegment = "@.comicBook.publicationDate == []";
        }
    }
    public String getSearchString() {
        if (fieldOpt.equals("publication_date")) {
            return searchOption.getSearchString() + searchStringSegment;
        }
        if (!fieldOpt.equals("")) {
            if (lastDecorator) return searchOption.getSearchString() + ")]";
            else return searchOption.getSearchString();
        }
        if (firstDecorator && lastDecorator) {
            return searchOption.getSearchString() + searchStringSegment + ")]";
        }
        else if (firstDecorator) {
            return searchOption.getSearchString() + searchStringSegment;
        }
        else if (lastDecorator) {
            if (searchOption.getSearchString().equals("$[?(")) {
                return searchOption.getSearchString() + searchStringSegment + ")]";
            }

            return searchOption.getSearchString() + "|| " + searchStringSegment + ")]";
        }
        return searchOption.getSearchString() + "|| " + searchStringSegment;
    }
}
