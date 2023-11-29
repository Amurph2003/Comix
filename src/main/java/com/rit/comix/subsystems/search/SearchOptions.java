package com.rit.comix.subsystems.search;
/*
*
* user input: search Spyderman [-fields] [-exact or -partial]
* fields: series, title, principle characters, creator names, description, issue number
* publisher, publication date
* if not specified by default search is partial
*
*  */
public class SearchOptions implements Search{
    private String searchString;
    public SearchOptions() {
        this.searchString = "$[?(";
    }

    public String getSearchString() {
        return searchString;
    }

}
