package com.rit.comix.subsystems.datastorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class XMLAdapter implements DatabaseAdapter {
    private DocumentBuilderFactory domFactory;
    private DocumentBuilder domBuilder;

    public XMLAdapter() {
        try {
            domFactory = DocumentBuilderFactory.newInstance();
            domBuilder = domFactory.newDocumentBuilder();
        } catch (FactoryConfigurationError exp) {
            System.err.println(exp.toString());
        } catch (ParserConfigurationException exp) {
            System.err.println(exp.toString());
        } catch (Exception exp) {
            System.err.println(exp.toString());
        }
    }

    @Override
    public void importData(String datasource, ObjectMapper collectionMapper, int userID) {
        // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // factory.setValidating(true);
        // factory.setIgnoringElementContentWhitespace(true);
        // DocumentBuilder builder;
        // try {
        //     builder = factory.newDocumentBuilder();
        // } catch (ParserConfigurationException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // File file = new File("test.xml");
        // Document doc;
        // try {
        //     doc = builder.parse(file);
        // } catch (SAXException | IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    @Override
    public void exportData(String datasource, String destination, ObjectMapper collectionMapper) {

        String firstDestination = "";

        int index = datasource.indexOf("json");

        if (index != -1) {
            firstDestination = datasource.substring(0, index) + "csv";
        }

        List<SearchEntry> entries = new ArrayList<>();
        try {
            entries = collectionMapper.readValue(new File(datasource), new TypeReference<List<SearchEntry>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        String csvFile = firstDestination;
        
        try {
            FileWriter writer = new FileWriter(csvFile);
            
            CSVWriter csvWriter = new CSVWriter(writer);
            
            String[] header = {"Publisher Name", "Series Number", "Volume Number", "Title", "Issue Number", "Publication Date", "Creators", "Principle Characters", "Description", "Base Value", "Grade", "Is Slabbed"};
            csvWriter.writeNext(header);

            for(SearchEntry entry : entries){
                // Create a new String array with the values of each field in the entry
                String[] row = {
                    entry.getPublisher().getPublisherName(),
                    entry.getSeries().getSeriesNumber(),
                    String.valueOf(entry.getVolume().getVolumeNumber()),
                    entry.getComicBook().getTitle(),
                    entry.getComicBook().getIssueNumber(),
                    entry.getComicBook().getPublicationDate().toString(),
                    String.join(", ", entry.getComicBook().getCreators()),
                    String.join(", ", entry.getComicBook().getPrincipleCharacters()),
                    entry.getComicBook().getDescription(),
                    String.valueOf(entry.getComicBook().getBaseValue()),
                    String.valueOf(entry.getComicBook().getGrade()),
                    String.valueOf(entry.getComicBook().getIsSlabbed())
                };

                System.out.println(row);
                
                csvWriter.writeNext(row);
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rowsCount = -1;
        BufferedReader csvReader;
        try {
            Document newDoc = domBuilder.newDocument();
            Element rootElement = newDoc.createElement("XMLCreators");
            newDoc.appendChild(rootElement);
            csvReader = new BufferedReader(new FileReader(firstDestination));

            CSVParser parser = new CSVParserBuilder()
                    //Not too sure about this 
                    .withSeparator(',')
                    .build();

            CSVReader reader = new CSVReaderBuilder(new FileReader(firstDestination))
                    .withCSVParser(parser)
                    .build();
            //CSVReader reader = new CSVReader(csvReader);
            String[] nextLine;
            int line = 0;
            List<String> headers = new ArrayList<String>(5);
            while ((nextLine = reader.readNext()) != null) {
                if (line == 0) { // Header row
                    for (String col : nextLine) {
                        headers.add(col);
                    }
                } else {
                    Element rowElement = newDoc.createElement("row");
                    rootElement.appendChild(rowElement);

                    int col = 0;
                    for (String value : nextLine) {
                        String header = headers.get(col).replaceAll("[\\t\\p{Zs}\\u0020]", "_");

                        Element curElement = newDoc.createElement(header);
                        curElement.appendChild(newDoc.createTextNode(value.trim()));
                        rowElement.appendChild(curElement);

                        col++;
                    }
                }
                line++;
            }

            FileWriter writer = null;

            try {

                writer = new FileWriter(new File(destination));

                TransformerFactory tranFactory = TransformerFactory.newInstance();
                Transformer aTransformer = tranFactory.newTransformer();
                aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
                aTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
                aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                Source src = new DOMSource(newDoc);
                Result result = new StreamResult(writer);
                aTransformer.transform(src, result);

                writer.flush();

            } catch (Exception exp) {
                exp.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException exp) {
            exp.printStackTrace();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        File file = new File(firstDestination);
        file.delete();
    }
}