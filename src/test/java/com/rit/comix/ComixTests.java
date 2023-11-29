package com.rit.comix;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.opencsv.CSVReader;
import com.rit.comix.controllers.CliDatabaseController;

@Testable
public class ComixTests {
    //Tests follow the convention of: functionNameBehaviorExpectedResult

    // @Test
    // public void populateDatabase() throws Exception {
    //     List<SearchEntry> records = new ArrayList<SearchEntry>();
    //     try (CSVReader br = new CSVReader(new FileReader("C:\\Users\\edsto\\Downloads\\comics(1).csv"))) {
    //         //br.readNext();
    //         String[] values;
    //         while ((values = br.readNext()) != null) {

    //             //Parse Volume Number and Series
    //             int volNum = -1;
    //             String noQuotes = values[0].replace("\"", "");
    //             String[] seriesTileAndVolNum = noQuotes.split(" VOL");
    //             if (seriesTileAndVolNum.length == 1)  volNum = 1;
    //             else volNum = Integer.parseInt(seriesTileAndVolNum[1].split(" ")[0]);
    //             String seriesName = seriesTileAndVolNum[0];

    //             //Parse Date
    //             LocalDate date = null;
    //             DateTimeFormatter formatter;

    //             String[] dateComps = new String[]{""};
    //             if (values[5] == ""){
    //                 dateComps = "0001".split("-");
    //             }else{
    //                 dateComps = values[5].split("-");
    //             }


    //             //Should only happen if there is a year and nothing else
    //             if (dateComps.length == 1) {
    //                 date = LocalDate.of(Integer.parseInt(dateComps[0]), 1, 1);
    //             }
    //             if (dateComps.length == 2) {
    //                 formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.DAY_OF_MONTH, 1).optionalStart().appendLiteral("-")
    //                 .appendValue(ChronoField.MONTH_OF_YEAR, 1).optionalStart().appendLiteral("-")
    //                 .appendValue(ChronoField.YEAR).toFormatter();
    //                 if (Integer.parseInt(dateComps[1]) >= 49) {
    //                     dateComps[1] = "19" + dateComps[1];
    //                 }
    //                 else {
    //                     dateComps[1] = "20" + dateComps[1];
    //                 }
    //                 Month month = null;
    //                 switch (dateComps[0].toUpperCase()) {
    //                     case "JAN":
    //                         month = Month.JANUARY;
    //                         break;
    //                     case "FEB":
    //                         month = Month.FEBRUARY;
    //                         break;
    //                     case "MAR":
    //                         month = Month.MARCH;
    //                         break;
    //                     case "APR":
    //                         month = Month.APRIL;
    //                         break;
    //                     case "MAY":
    //                         month = Month.MAY;
    //                         break;
    //                     case "JUN":
    //                         month = Month.JUNE;
    //                         break;
    //                     case "JUL":
    //                         month = Month.JULY;
    //                         break;
    //                     case "AUG":
    //                         month = Month.AUGUST;
    //                         break;
    //                     case "SEP":
    //                         month = Month.SEPTEMBER;
    //                         break;
    //                     case "OCT":
    //                         month = Month.OCTOBER;
    //                         break;
    //                     case "NOV":
    //                         month = Month.NOVEMBER;
    //                         break;
    //                     case "DEC":
    //                         month = Month.DECEMBER;
    //                         break;
    //                     default:
    //                         throw new IllegalArgumentException("Invalid month: " + dateComps[0]);
    //                 }
    //                 date = LocalDate.of(Integer.parseInt(dateComps[1]), month, 1);
    //             }
    //             else if (dateComps.length == 3) {
    //                 formatter = DateTimeFormatter.ofPattern("dd-Mmm-yyyy");
    //                 if (Integer.parseInt(dateComps[2]) >= 49) {
    //                     dateComps[2] = "19" + dateComps[2];
    //                 }
    //                 else {
    //                     dateComps[2] = "20" + dateComps[2];
    //                 }
    //                 Month month = null;
    //                 switch (dateComps[1].toUpperCase()) {
    //                     case "JAN":
    //                         month = Month.JANUARY;
    //                         break;
    //                     case "FEB":
    //                         month = Month.FEBRUARY;
    //                         break;
    //                     case "MAR":
    //                         month = Month.MARCH;
    //                         break;
    //                     case "APR":
    //                         month = Month.APRIL;
    //                         break;
    //                     case "MAY":
    //                         month = Month.MAY;
    //                         break;
    //                     case "JUN":
    //                         month = Month.JUNE;
    //                         break;
    //                     case "JUL":
    //                         month = Month.JULY;
    //                         break;
    //                     case "AUG":
    //                         month = Month.AUGUST;
    //                         break;
    //                     case "SEP":
    //                         month = Month.SEPTEMBER;
    //                         break;
    //                     case "OCT":
    //                         month = Month.OCTOBER;
    //                         break;
    //                     case "NOV":
    //                         month = Month.NOVEMBER;
    //                         break;
    //                     case "DEC":
    //                         month = Month.DECEMBER;
    //                         break;
    //                     default:
    //                         throw new IllegalArgumentException("Invalid month: " + dateComps[1]);
    //                 }
    //                 date = LocalDate.of(Integer.parseInt(dateComps[2]), month, Integer.parseInt(dateComps[0]));
    //             }

    //             String fullDateString = "";
    //             for (String string : dateComps) {
    //                 fullDateString += string + "-";
    //             }

    //             //Parse Creators
    //             String[] creators = {};
    //             if (values.length >= 9) creators = values[8].split(" \\| ");

    //             String[] principleCharacters = {};

    //             records.add(new SearchEntry(new ComicBook(values[2], values[1], date, creators, principleCharacters, values[3], 0, 0, false),
    //                 new Publisher(values[4], null), new Series(seriesName, null), new Volume(volNum, null)));
    //         }
    //         br.close();

    //         DatabaseController dbController = new DatabaseController();
    //         for (SearchEntry searchEntry : records) {
    //             dbController.addComicBook(searchEntry);
    //         }
    //         dbController.saveCollection();
    //     }
    // }
}
