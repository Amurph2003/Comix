package com.rit.comix.subsystems.search;

public class StoryTitleDecorator extends SearchDecorator{
    private String searchStringSegment;
    public StoryTitleDecorator(Search searchOption, String fieldOpt, String title, boolean first, boolean last,
                               String matchOpt) {
        super.searchOption = searchOption;
        super.firstDecorator = first;
        super.lastDecorator = last;
        super.fieldOpt = fieldOpt;
        if (matchOpt.equalsIgnoreCase("exact"))
            searchStringSegment = "@.comicBook.title =~/" + title + "/";
        else searchStringSegment = "@.comicBook.title =~/.*" + title + ".*/";
    }

    public String getSearchString() {
        if (!fieldOpt.equals("title") && !fieldOpt.equals("")) {
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
            return searchOption.getSearchString() + "|| " + searchStringSegment + ")]";
        }
        return searchOption.getSearchString() + "|| " + searchStringSegment;
    }


}
