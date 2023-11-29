package ComixCliUi;

import java.util.List;
import java.util.Scanner;

import com.rit.comix.controllers.DataController;
import com.rit.comix.subsystems.hierarchy.SearchEntry;

public class ComixCliUtils {
    public static void divider(){
        System.out.println("\n----------------------------------------------------------------------------\n");
    }

    public static void printMessage(String message){
        System.out.println(message);
        divider();
    }

    public static String userInput(Scanner userInputScanner){
        String scannerOutput;
        System.out.print(">> ");
        scannerOutput = userInputScanner.nextLine();
        divider();
        return scannerOutput;
    }

    public static void printSearchList(Scanner userInputScanner, ComixCliClient client, List<SearchEntry> list, int entriesPerPage, String searchString, DataController controller) {
        printMessage("Usage: search <search_term> [field] [exact or partial]\n" +
            "NOTE: if none of the optional parameters are specified, search is executed across all fields partially\n" +
            "NOTE: all whitespaces in the arguments should be replaced with _\n" +
            "Example: search Stan_Lee creator\n\n" +
            "Possible search fields: series, title, principle_characters, creator,\n" +
            "description, issue, publisher, publication_date\n");
        searchString = userInput(userInputScanner);
        List<SearchEntry> searchResults = controller.search(searchString);
        client.currentList = List.copyOf(searchResults);
        int searchSize = 0;
        if (client.currentList!=null) searchSize = client.currentList.size();
        printMessage("Your search returned " + searchSize + " results!");
    }

    //shouldn't return be void?
    public static void sort(String sortChoice, DataController controller, ComixCliClient client) {
        if (sortChoice.equals("1"))
            client.currentList = controller.sort("publicationDate");
        else{
            client.currentList = controller.sort();
        } 
}

    public static void sort(DataController controller, ComixCliClient client) {
        client.currentList = controller.sort();
    }

    public static void printEntries(Scanner userInputScanner, List<SearchEntry> currentList) {
        int startIndex = 0;
        int endIndex = Math.min(startIndex + 10, currentList.size());
        do {
            for (int i = startIndex; i < endIndex; i++) {
                ComixCliUtils.printMessage(i + ". \n" + currentList.get(i));
            }
            if (endIndex >= currentList.size()) {
                break;
            }
            ComixCliUtils.printMessage("Would you like to see the next 10? [Y/n]");
            String answer = userInput(userInputScanner);
            if (answer.equalsIgnoreCase("y")) {
                startIndex += 10;
                endIndex = Math.min(startIndex+10, currentList.size());
            }
            else {
                break;
            }
        }
        while (true);
    }

    public static void heading() {
        System.out.print(" .----------------.  .----------------.  .----------------.  .----------------.  .----------------. \n" +
                "| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n" +
                "| |     ______   | || |     ____     | || | ____    ____ | || |     _____    | || |  ____  ____  | |\n" +
                "| |   .' ___  |  | || |   .'    `.   | || ||_   \\  /   _|| || |    |_   _|   | || | |_  _||_  _| | |\n" +
                "| |  / .'   \\_|  | || |  /  .--.  \\  | || |  |   \\/   |  | || |      | |     | || |   \\ \\  / /   | |\n" +
                "| |  | |         | || |  | |    | |  | || |  | |\\  /| |  | || |      | |     | || |    > `' <    | |\n" +
                "| |  \\ `.___.'\\  | || |  \\  `--'  /  | || | _| |_\\/_| |_ | || |     _| |_    | || |  _/ /'`\\ \\_  | |\n" +
                "| |   `._____.'  | || |   `.____.'   | || ||_____||_____|| || |    |_____|   | || | |____||____| | |\n" +
                "| |              | || |              | || |              | || |              | || |              | |\n" +
                "| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |\n" +
                " '----------------'  '----------------'  '----------------'  '----------------'  '----------------' ");
    }
}