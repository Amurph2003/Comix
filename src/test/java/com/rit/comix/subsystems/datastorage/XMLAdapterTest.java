package com.rit.comix.subsystems.datastorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rit.comix.subsystems.search.queryer.QueryExecutor;
import org.junit.jupiter.api.Test;

public class XMLAdapterTest {
    @Test
    public void testExportXML() {
        // CSVAdapter csvAdapter = new CSVAdapter();
        // ObjectMapper collectionMapper = new ObjectMapper();
        // collectionMapper.registerModule(new JavaTimeModule());

        // QueryExecutor qe = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "series"});
        // String datasource = qe.generateFlatCollection(0);
        // csvAdapter.exportData(datasource, "myfile", collectionMapper);

        XMLAdapter csvAdapter = new XMLAdapter();
        ObjectMapper collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());
        csvAdapter.exportData("personalCollection-0-SearchList.json", "personalCollection-0.xml", collectionMapper);
    }
}