package com.rit.comix.subsystems.search;

public class PublisherDecorator extends SearchDecorator{
    private String searchStringSegment;
    public PublisherDecorator(Search searchOption, String fieldOpt, String publisher, boolean first, boolean last,
                              String matchOpt) {
        super.searchOption = searchOption;
        super.firstDecorator = first;
        super.lastDecorator = last;
        super.fieldOpt = fieldOpt;
        if (matchOpt.equalsIgnoreCase("exact"))
            searchStringSegment = "@.publisher.publisherName =~/" + publisher + "/";
        else searchStringSegment = "@.publisher.publisherName =~/.*" + publisher + ".*/";
    }
    public String getSearchString() {
        if (fieldOpt.equals("publisher")) {
            return searchOption.getSearchString() + searchStringSegment;
        }
        if (!fieldOpt.equals("")) {
            if (lastDecorator) return  searchOption.getSearchString() + ")]";
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
