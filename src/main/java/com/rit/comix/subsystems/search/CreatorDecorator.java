package com.rit.comix.subsystems.search;

public class CreatorDecorator extends SearchDecorator{
    private String searchStringSegment;
    public CreatorDecorator(Search searchOption, String fieldOpt, String creator, boolean first, boolean last,
                            String matchOpt) {
        super.searchOption = searchOption;
        super.firstDecorator = first;
        super.lastDecorator = last;
        super.fieldOpt = fieldOpt;
        if (matchOpt.equalsIgnoreCase("exact"))
            searchStringSegment = "@.comicBook.creators == ['" + creator + "']";
        else searchStringSegment = "@.comicBook.creators anyof [" + "'" + creator + "']";
    }
    public String getSearchString() {
        if (!fieldOpt.equals("creator") && !fieldOpt.equals("")) {
            if (lastDecorator) return searchOption.getSearchString() + ")]";
            else return searchOption.getSearchString();
        }

        if (firstDecorator && lastDecorator) {
            return searchOption.getSearchString() + searchStringSegment + ")]";
        }
        else if (firstDecorator) return searchOption.getSearchString() + searchStringSegment;
        else if (lastDecorator) {
            if (searchOption.getSearchString().equals("$[?(")) {
                return searchOption.getSearchString() + searchStringSegment + ")]";
            }
            return searchOption.getSearchString() + "|| " + searchStringSegment + ")]";
        }
        return searchOption.getSearchString() + "|| " + searchStringSegment;
    }

}
