package com.rit.comix.subsystems.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.rit.comix.controllers.CliPersonalController;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.search.queryer.QueryExecutor;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SearchTests {

    @Test
    public void TestJsonPath() {
        try {
            String json = Files.readString(Paths.get("database.json"));
            ReadContext ctx = JsonPath.parse(json);
            List<String> creators = ctx.read("$[?(@..comicBook.title anyof ['Creation'])]");
            System.out.println(creators);
            System.out.println(creators.size());
            List<String> creatorStanLee = ctx.read("$..[?(@.comicBook.creators anyof ['Stan Lee'])]");
            System.out.println(creatorStanLee.size());
//            Filer filer = Filter.filter(Criteria.where(""))
//            List<String> creatorStanLee = ctx.read("$..[?('Spyder' in @['comicBook']['title'])]");
//            String query = "$[?(@.comicBook.title =~/.*Hulk/i || @.series.seriesNumber =~/.*Hulk/i || @.comicBook.creators anyof ['Stan Lee'])]";
            String query = "$[?(@.series.seriesNumber =~/.*man.*/ || @.comicBook.title =~/.*Hulk.*/)]";
            List<String> containsMan = ctx.read(query);
//            System.out.println(containsMan);
            System.out.println(containsMan.size());

            query = "$[?(@.publisher.publisherName =~/.*Marvel.*/ || @.comicBook.title =~/.*Marvel.*/ || @.series.seriesNumber =~/.*Marvel.*/" +
                    "|| @.comicBook.creators anyof ['Marvel'])]";
            List<String> Marvel = ctx.read(query);
//            System.out.println(publisherMarvel);
            System.out.println(Marvel.size());
            query = "$[?(@.publisher.publisherName =~/.*Marvel.*/)]";
            List<String> publisherMarvel = ctx.read(query);
//            System.out.println(publisherMarvel);
            System.out.println(publisherMarvel.size());

            query = "$[?(@.comicBook.publicationDate == [2017,10,18])]";
            List<String> publicationDate = ctx.read(query);
            System.out.println(publicationDate.size());

            query = "$[?(@.comicBook.issueNumber =~/.*7A.*/)]";
            List<String> issueNum = ctx.read(query);
            System.out.println(issueNum.size());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void TestDecoratorPartial() {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "title"});
        dbq.query();
//        System.out.println(dbq.getRes());
        assertEquals(134, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "series"});
        dbq.query();
//        System.out.println(dbq.getRes());
        assertEquals(675, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk"});
        dbq.query();
//        System.out.println(dbq.getRes());
        assertEquals(690, dbq.getRes().size());
//        System.out.println("Stan_Lee".replace("_", " "));
//        System.out.println("Hulk".replace("_", " "));

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Stan_Lee", "creator"});
        dbq.query();
//        System.out.println(dbq.getRes());
        assertEquals(10, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Marvel", "publisher"});
        dbq.query();
        assertEquals(7032, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Marvel"});
        dbq.query();
        // because everything that has Marvel in one of the search fields is probably published by Marvel
        assertEquals(7032, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "2017/10/18", "publication_date"});
        dbq.query();
        assertEquals(21, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "abc", "publication_date"});
        dbq.query();
        assertEquals(0, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "7A", "issue"});
        dbq.query();
        assertEquals(400, dbq.getRes().size());

        dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Bob_Bolling", "creator", "exact"});
        dbq.query();
        assertEquals(1, dbq.getRes().size());

//        dbq = new DatabaseQueryer(new String[]{"search", "Hulk", "title", "exact"});
//        dbq.query();
//        System.out.println(dbq.getRes().size());

    }

    @Test
    public void Deserialize() {
        QueryExecutor dbq = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Stan_Lee", "creator"});
        dbq.query();
        List<String> res = dbq.getRes();
        assertEquals(10, dbq.getRes().size());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<SearchEntry> se = objectMapper.convertValue(dbq.getRes(), new TypeReference<List<SearchEntry>>() {
        });

        assertEquals(dbq.getResults().size(), se.size());

    }

    @Test
    public void testPersonalCollectionSearch() {
        try {
            CliPersonalController pc = new CliPersonalController();
            System.out.println(pc.search("search Hulk"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
