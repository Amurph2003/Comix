package com.rit.comix.subsystems.search;

public class IssueNumberDecorator extends SearchDecorator{
    private String searchStringSegment;
    public IssueNumberDecorator(Search searchOption, String fieldOpt, String issueNum, boolean first, boolean last,
                                String matchOpt) {
        super.searchOption = searchOption;
        super.firstDecorator = first;
        super.lastDecorator = last;
        super.fieldOpt = fieldOpt;
        if (matchOpt.equalsIgnoreCase("exact"))
            searchStringSegment = "@.comicBook.issueNumber =~/" + issueNum + "/";
        else searchStringSegment = "@.comicBook.issueNumber =~/.*" + issueNum + ".*/";
    }
    public String getSearchString() {
        if (fieldOpt.equals("issue")) {
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
