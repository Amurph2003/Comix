package com.rit.comix.ComixDummy;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.rit.comix.controllers.DataController;

@SpringBootTest
public class EndpointTests {
    
    @Test
    public void getDatabaseJson_noArgs() throws IOException {
        DataController dataController = new DataController();
        dataController.getDatabaseJson(null);
    }
}
