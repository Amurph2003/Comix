package com.rit.comix.subsystems.datastorage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;

public class CSVParser {

    public void importData(String source, ObjectMapper collectionMapper) {

    }

    /**
     * export file as a CSV
     * read JSON write CSV
     * @param destination
     * @param collectionMapper
     */
    public void exportData(String dataSource, String destination, ObjectMapper collectionMapper) throws IOException {
        JsonNode jsonTree = collectionMapper.readTree(new File(dataSource));

        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File(destination + ".csv"), jsonTree);
    }
}
