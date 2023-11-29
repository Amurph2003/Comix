package com.rit.comix.searchsort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSortSystemTests {

    @Test
    public void TestSearchSort() {
        SearchSortSystem sss1 = new SearchSortSystem();
        sss1.performSearch("search Hulk title");
        sss1.sort();
//        System.out.println(sss1.returnSearchResults().stream().toList());
        SearchSortSystem sss2 = new SearchSortSystem();
        sss2.performSearch("search Hulk title");
        sss2.sort();

        SearchSortSystem sss3 = new SearchSortSystem();
        sss3.performSearch("search Hulk title");

        System.out.println(sss1.equals(sss2));

        assertEquals(sss1, sss2);

    }
}
