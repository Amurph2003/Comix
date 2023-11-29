package com.rit.comix.ComixDummy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @CrossOrigin()
    @GetMapping("/dummydatabase")
    public String getDatabaseJson() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("database.json"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    @CrossOrigin()
    @GetMapping("/dummycollections")
    public String getCollectionsJson() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("personalCollectionSearchList.json"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }
}