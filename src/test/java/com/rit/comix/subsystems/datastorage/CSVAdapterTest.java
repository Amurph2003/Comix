package com.rit.comix.subsystems.datastorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

public class CSVAdapterTest {

    @Test
    public void testExportCSV() {
        // CSVAdapter csvAdapter = new CSVAdapter();
        // ObjectMapper collectionMapper = new ObjectMapper();
        // collectionMapper.registerModule(new JavaTimeModule());

        // QueryExecutor qe = new QueryExecutor(new String[]{"com/rit/comix/subsystems/search", "Hulk", "series"});
        // String datasource = qe.generateFlatCollection(0);
        // csvAdapter.exportData(datasource, "myfile", collectionMapper);

        CSVAdapter csvAdapter = new CSVAdapter();
        ObjectMapper collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());
        csvAdapter.exportData("personalCollection-0-SearchList.json", "personalCollection-0.csv", collectionMapper);
    }
}