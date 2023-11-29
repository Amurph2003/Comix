package com.rit.comix.subsystems.datastorage;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface DatabaseAdapter {
    public void importData(String datasource, ObjectMapper collectionMapper, int userID) throws IOException;
    public void exportData(String datasource, String destination, ObjectMapper collectionMapper);
}
