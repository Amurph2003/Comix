package com.rit.comix;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.rit.comix.controllers.DataController;
import com.rit.comix.controllers.CliDatabaseController;
import com.rit.comix.controllers.CliPersonalController;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;

public class ComixClient {

    static final String HELPMENU = "Commands: \n\t'help': Access the help menu\n\t'exit': Quit the program";//\n\t'go back': Go to previous screen"

    public CliPersonalController personalController;
    public CliDatabaseController databaseController;
    public List<SearchEntry> currentList;
    public SearchEntry selected;
    
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to COMIX, the best way to manage your comic collection!");
        ComixClient comix = new ComixClient();
        System.out.println(comix.heading());
        comix.mainLoop();
    }

    private void mainLoop() throws IOException{
        while(true){
            System.out.println("What would you like to do?\n1. Access the entire comic library\n2. Access your personal comic collection\n3. View personal collection statistics\n\nType 'help' to bring up the Help Menu at any time or 'exit' to quit the program"); //3. Update your personal collection (Add, Remove, Grade, etc.)
            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();
            // System.out.println(input);

            if (input.toLowerCase().equals("exit")){
                userInput.close();
                return;
            }

            if (input.toLowerCase().equals("help")){
                System.out.println(HELPMENU);
            }

            if (input.equals("1")){
                System.out.println("Would you like to: \n1. View the entire comic library\n2. Search the comic library");
                String bigDB = userInput.nextLine();
                if (bigDB.equals("2")){
                    System.out.println("What would you like to search for in the Comic Library?");
                    printSearchList(userInput, null, 10, null, databaseController);
                } else if (bigDB.equals("1")) {
                    currentList = databaseController.getDataCollection();
                    printEntries(userInput);
                }
            } else if (input.equals("2")) {
                System.out.println("Would you like to:\n1. See all comics in your personal collection\n2. Search my personal Collection\n3. Add a comic to your collection\n4. Edit a comic in your collection\n5. Remove a comic from your collection\n");
                String personalCInput1 = userInput.nextLine();

                // View Entire Personal Collection
                if (personalCInput1.equals("1")){
                    printFullCollection(null, 10);

                    // Search Personal Collection
                } else if (personalCInput1.equals("2")) {
                    printSearchList(userInput, null, 10, null, this.personalController);

                    // Add to Personal Collection
                } else if (personalCInput1.equals("3")){
                    System.out.println("Great! Lets search the Comic Database for a comic to add!");
                    printSearchList(userInput, null, 10, null, personalController);
                    System.out.println("Which Comic would you like to add to your collection?");
                    String selectedComic = userInput.nextLine();
                    selectEntry(currentList, selectedComic);
                    addEntry();
                    
                    // Edit A Comic in Personal Collection
                } else if (personalCInput1.equals("4")) {
                    System.out.println("Here's your personal collection:");
                    printSearchList(userInput, personalController.getComixCollection().getSearchEntriesWithChildren(), 10, null, personalController);
                    System.out.println("Select a comic to edit");
                    String editing = userInput.nextLine();
                    selectEntry(currentList, editing);
                    ComicBook original = selected.getComicBook();
            // Not fully sure how to implement editing
                    SearchEntry edited = new SearchEntry(null, null, null, null);

                    updateEntry(edited);

                } else if (personalCInput1.equals("5")) {
                    ComixCollection personal = personalController.getComixCollection();
                    System.out.println(personal);
                    System.out.println("Which comic will we be deleting now? (Please enter the number)");
                    String deletion = userInput.nextLine();
                    selectEntry(currentList, deletion);
                    removeEntry(selected);
                }
            } else if (input.equals("3")) {
                // Comic Statistics
                ComixCollection personal = personalController.getComixCollection();
                System.out.println("Would you like to view stats for: \n1. Your entire collection\n2.A publisher in your collection\n3.A series in your collection\n4. A volume in your collection\n5. A comicbook in your collection");
                getStatsForTiers(personal);
            }
        }

    }





    public ComixClient() throws IOException{
        // collections are generated in the constructors
        personalController = new CliPersonalController();
        databaseController = new CliDatabaseController();
        currentList = null;
    }

    protected void printFullCollection(List<SearchEntry> list, int entriesPerPage) {
        this.currentList = this.personalController.getComixCollection().getSearchEntriesWithChildren();

        for (SearchEntry e: currentList)
            System.out.println(e);
    }

    protected void printSearchList(Scanner scanner, List<SearchEntry> list, int entriesPerPage, String searchString, DataController controller) {
        System.out.println("Usage: search <search_term> [field] [exact or partial]");
        System.out.println("NOTE: if none of the optional parameters are specified, search is executed across all fields partially");
        System.out.println("NOTE: all whitespaces in the arguments should be replaced with _");
        System.out.println("Example: search Stan_Lee creator");
        System.out.println("");
        System.out.println("Possible search fields: series, title, principle_characters, creator," +
                "description, issue, publisher, publication_date");
        searchString = scanner.nextLine();
        currentList = controller.search(searchString);
        int searchSize = 0;
        if (currentList!=null) searchSize = currentList.size();
        System.out.println("Your search returned " + searchSize + " results!");
        sort(scanner, controller);
        if (currentList!=null) {
            System.out.println("Would you like to view the first 10? [Y/n]");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                printEntries(scanner);
            }
        }
    }

    public List<SearchEntry> sort(Scanner scanner, DataController controller) {
        System.out.println("Would you like to sort your results? (y/n)");
        String doSort = scanner.nextLine();
        if (doSort.equalsIgnoreCase("y")) {
            System.out.println("Would you like to sort by publication date or the default hierarchical sort? Please enter pub or defualt");
            String sorting = scanner.nextLine();
            if (sorting == null)
                return controller.sort();
            else
                if (sorting == "default")
                    return controller.sort();
                else
                    return controller.sort("publicationDate");
        } else {
            return currentList;
        }
    }

    private String heading() {
        return " .----------------.  .----------------.  .----------------.  .----------------.  .----------------. \n" +
                "| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n" +
                "| |     ______   | || |     ____     | || | ____    ____ | || |     _____    | || |  ____  ____  | |\n" +
                "| |   .' ___  |  | || |   .'    `.   | || ||_   \\  /   _|| || |    |_   _|   | || | |_  _||_  _| | |\n" +
                "| |  / .'   \\_|  | || |  /  .--.  \\  | || |  |   \\/   |  | || |      | |     | || |   \\ \\  / /   | |\n" +
                "| |  | |         | || |  | |    | |  | || |  | |\\  /| |  | || |      | |     | || |    > `' <    | |\n" +
                "| |  \\ `.___.'\\  | || |  \\  `--'  /  | || | _| |_\\/_| |_ | || |     _| |_    | || |  _/ /'`\\ \\_  | |\n" +
                "| |   `._____.'  | || |   `.____.'   | || ||_____||_____|| || |    |_____|   | || | |____||____| | |\n" +
                "| |              | || |              | || |              | || |              | || |              | |\n" +
                "| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |\n" +
                " '----------------'  '----------------'  '----------------'  '----------------'  '----------------' ";
    }

    private void printEntries(Scanner scanner) {
        int startIndex = 0;
        int endIndex = Math.min(startIndex + 10, currentList.size());
        do {
            for (int i = startIndex; i < endIndex; i++) {
                System.out.println(i + ". \n" + currentList.get(i));
            }
            if (endIndex >= currentList.size()) {
                break;
            }
            System.out.println("Would you like to see the next 10? [Y/n]");
            String answer = scanner.nextLine();
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

    protected void selectEntry(List<SearchEntry> list, String name) {
        selected = currentList.get(Integer.parseInt(name));
    }

    protected void removeEntry(SearchEntry entry) throws IOException {
        personalController.removeComicBook(entry);
    }

    protected void addEntry() throws IOException {
        personalController.addComicBook(this.selected);
    }

    protected void updateEntry(SearchEntry original) throws IOException {
        personalController.editComicBook(selected, original);
    }

    protected void getStatsForTiers(ComixCollection collection) {
        Scanner userInput = new Scanner(System.in);
        String stats = userInput.nextLine();
        Integer comicNum;
        Double value;
        if (Integer.parseInt(stats) == 1){
            comicNum = collection.getNumIssues();
            value = collection.getTotalValue();
            System.out.println("Total number of comics in your personal collection: " + comicNum);
            System.out.println("The total value of comics in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 2) {
            System.out.println("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                System.out.println(i + ". " + pub.getPublisherName());
                i++;
            }
            System.out.println("Which Publisher would you like to get statistics on?");
            String publisher = userInput.nextLine();
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            comicNum = pubNode.getNumIssues();
            value = pubNode.getTotalValue();
            System.out.println("Total number of comics by " + pubNode.getPublisherName() + " in your personal collection: " + comicNum);
            System.out.println("The total value of comics " + pubNode.getPublisherName() + " in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 3) {
            System.out.println("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                System.out.println(i + ". " + pub.getPublisherName());
                i++;
            }
            System.out.println("Which Publisher would you like to get statistics on?");
            String publisher = userInput.nextLine();
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            int p = 1;
            for (Series series: pubNode.getChildren()){
                System.out.println(p + ". " + series.getSeriesNumber());
                p++;
            }
            System.out.println("Which Series would you like to get statistics on?");
            String seri = userInput.nextLine();
            Series seriNode = pubNode.getChildNode(Integer.parseInt(seri)-1);
            comicNum = seriNode.getNumIssues();
            value = seriNode.getTotalValue();
            System.out.println("Total number of comics in " + seriNode.getSeriesNumber() + " in your personal collection: " + comicNum);
            System.out.println("The total value of comics in " + seriNode.getSeriesNumber() + " in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 4) {
            System.out.println("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                System.out.println(i + ". " + pub.getPublisherName());
                i++;
            }
            System.out.println("Which Publisher would you like to get statistics on?");
            String publisher = userInput.nextLine();
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            int p = 1;
            for (Series series: pubNode.getChildren()){
                System.out.println(p + ". " + series.getSeriesNumber());
                p++;
            }
            System.out.println("Which Series would you like to get statistics on?");
            String seri = userInput.nextLine();
            Series seriNode = pubNode.getChildNode(Integer.parseInt(seri)-1);
            int s = 1;
            for (Volume volume: seriNode.getChildren()){
                System.out.println(s + ". Volume #" + volume.getVolumeNumber());
                s++;
            }
            System.out.println("Which Volume would you like to get statistics on?");
            String vol = userInput.nextLine();
            Volume volumeNode = seriNode.getChildNode(Integer.parseInt(vol)-1);
            comicNum = volumeNode.getNumIssues();
            value = volumeNode.getTotalValue();
            System.out.println("Total number of comics in Volume #" + volumeNode.getVolumeNumber() + " in your personal collection: " + comicNum);
            System.out.println("The total value of comics in Volume #" + volumeNode.getVolumeNumber() + " in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 5) {
            System.out.println("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                System.out.println(i + ". " + pub.getPublisherName());
                i++;
            }
            System.out.println("Which Publisher would you like to get statistics on?");
            String publisher = userInput.nextLine();
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            int p = 1;
            for (Series series: pubNode.getChildren()){
                System.out.println(p + ". " + series.getSeriesNumber());
                p++;
            }
            System.out.println("Which Series would you like to get statistics on?");
            String seri = userInput.nextLine();
            Series seriNode = pubNode.getChildNode(Integer.parseInt(seri)-1);
            int s = 1;
            for (Volume volume: seriNode.getChildren()){
                System.out.println(s + ". Volume #" + volume.getVolumeNumber());
                s++;
            }
            System.out.println("Which Volume would you like to get statistics on?");
            String vol = userInput.nextLine();
            Volume volumeNode = seriNode.getChildNode(Integer.parseInt(vol)-1);
            int v = 1;
            for (ComicBook comic: volumeNode.getChildren()){
                System.out.println(v + ". " + comic.getTitle());
                v++;
            }
            System.out.println("Which Comic would you like to get statistics on?");
            String comi = userInput.nextLine();
            ComicBook comicNode = volumeNode.getChildNode(Integer.parseInt(comi)-1);
            comicNum = comicNode.getNumIssues();
            value = comicNode.getTotalValue();
            System.out.println("Total number of comics in " + comicNode.getTitle() + " in your personal collection: " + comicNum);
            System.out.println("The total value of the comic " + comicNode.getTitle() + " in your personal collection is: $" + value + "\n");
        }
    }
}

