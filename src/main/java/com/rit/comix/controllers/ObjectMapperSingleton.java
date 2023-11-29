package com.rit.comix.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperSingleton {
    private static ObjectMapperSingleton INSTANCE;

    private final ObjectMapper collectionMapper;
    private ObjectMapperSingleton() {

        System.out.println("Object mapper created");
        collectionMapper = new ObjectMapper();
        collectionMapper.registerModule(new JavaTimeModule());
    }

    public ObjectMapper getCollectionMapper() {
        return collectionMapper;
    }

    public static ObjectMapperSingleton instance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectMapperSingleton();
        }
        return INSTANCE;
    }
}
