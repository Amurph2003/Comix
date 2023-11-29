package com.rit.comix.subsystems.datastorage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONAdapter implements DatabaseAdapter{
    private JSONParser jsonParser;
    @Override
    public void importData(String datasource, ObjectMapper collectionMapper, int userID) {

    }

    @Override
    public void exportData(String datasource, String destination, ObjectMapper collectionMapper) {

    }
}
