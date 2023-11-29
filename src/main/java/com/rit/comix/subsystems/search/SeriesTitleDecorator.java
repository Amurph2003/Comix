package com.rit.comix.subsystems.search;

public class SeriesTitleDecorator extends SearchDecorator{
    private String searchStringSegment;
    public SeriesTitleDecorator(Search searchOption, String fieldOpt, String seriesNumber, boolean first, boolean last,
                                String matchOpt) {
        super.searchOption = searchOption;
        super.firstDecorator = first;
        super.lastDecorator = last;
        super.fieldOpt = fieldOpt;
        if (matchOpt.equalsIgnoreCase("exact"))
            searchStringSegment = "@.series.seriesNumber =~/" + seriesNumber + "/";
        else searchStringSegment = "@.series.seriesNumber =~/.*" + seriesNumber + ".*/";
    }
    public String getSearchString() {
        if (fieldOpt.equals("series")) {
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
