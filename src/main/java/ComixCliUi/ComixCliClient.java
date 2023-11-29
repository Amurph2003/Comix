package ComixCliUi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.rit.comix.controllers.CliDatabaseController;
import com.rit.comix.controllers.CliPersonalController;
import com.rit.comix.subsystems.hierarchy.ComicBook;
import com.rit.comix.subsystems.hierarchy.ComixCollection;
import com.rit.comix.subsystems.hierarchy.Publisher;
import com.rit.comix.subsystems.hierarchy.SearchEntry;
import com.rit.comix.subsystems.hierarchy.Series;
import com.rit.comix.subsystems.hierarchy.Volume;

public class ComixCliClient {

    static final String HELPMENU = "Commands: \n\t'help': Access the help menu\n\t'exit': Quit the program";//\n\t'go back': Go to previous screen"

    public CliPersonalController personalController;
    public CliDatabaseController databaseController;
    public List<SearchEntry> currentList;
    public SearchEntry selected;
    public Scanner userInputScanner;

    public ComixCliClient() {
        try{
            personalController = new CliPersonalController();
            databaseController = new CliDatabaseController();
            userInputScanner = new Scanner(System.in);
            currentList = new ArrayList<>();
        } catch (IOException exception) {
            System.out.println(exception);
        }
    }

    // private static List<SearchEntry> searchSort(PersonalController comixPC){
    //     Scanner ComixCliUtils.userInput = new Scanner(System.in);
    //     ComixCliUtils.printMessage("What would you like to search for?");
    //     // Gets search term
    //     String query = ComixCliUtils.userInput();
    //     ComixCliUtils.printMessage("Would you prefer to sort by Publication dates (y/n)?");
    //     // checks if sort is default or pub. date
    //     String category = ComixCliUtils.userInput();
    //     List<SearchEntry> searched = comixPC.search("search " + query);
    //     // handles sort for publication date
    //     if (category.equals("y"))
    //         searched = comixPC.sort("publicationDate");
    //     else
    //     // handles default sort
    //         searched = comixPC.sort();
    //     // displays found comics with a number     
    //     for (int i = 0; i < searched.size(); i++){
    //         System.out.print(i+1);
    //         ComixCliUtils.printMessage(searched.get(i)); 
    //     }
    //     ComixCliUtils.printMessage("Of the " + searched.size()+1 + " comics that match '" + query + "', which comic would you like to add to your collection?");
    //     ComixCliUtils.userInput.close();
    //     return searched;
    // }

    protected void mainLoop() throws IOException {
        while(true){
            ComixCliUtils.printMessage("What would you like to do?\n1. Access the entire comic library\n2. Access your personal comic collection\n3. View personal collection statistics\n\nType 'help' to bring up the Help Menu at any time or 'exit' to quit the program"); //3. Update your personal collection (Add, Remove, Grade, etc.)
//             Scanner ComixCliUtils.userInput = new Scanner(System.in);
            String input = ComixCliUtils.userInput(userInputScanner);
            // ComixCliUtils.printMessage(input);

            if (input.toLowerCase().equals("exit")){
                userInputScanner.close();
                return;
            }

            if (input.toLowerCase().equals("help")){
                ComixCliUtils.printMessage(HELPMENU);
            }

            if (input.equals("1")){
                ComixCliUtils.printMessage("Would you like to: \n1. View the entire comic library\n2. Search the comic library\n\n" + 
                "Type 'exit' to quit the program or 'back' to go back to the main menu.");
                String bigDB = ComixCliUtils.userInput(userInputScanner);
                if (bigDB.equals("2")){
                    ComixCliUtils.printMessage("What would you like to search for in the Comic Library?");
                    ComixCliUtils.printSearchList(userInputScanner, this, null, 10, null, databaseController);
                    ComixCliUtils.printMessage("Would you like to sort the results using a custom order? [Y/n] (Sorted by Series Title, Volume, then Issue Number by default)");
                    String doSort = ComixCliUtils.userInput(userInputScanner);

                    //custom sort
                    if (doSort.equalsIgnoreCase("y")) {
                        //remove menu for a single custom sort?
                        ComixCliUtils.printMessage("Supported custom sort orders:\n1. Publication Date");
                        String sorting = ComixCliUtils.userInput(userInputScanner);
                        ComixCliUtils.sort(sorting, databaseController, this);
                    }
                    //default sort
                    else{
                        ComixCliUtils.sort(databaseController, this);
                    }
                    
                    if (currentList != null) {
                        ComixCliUtils.printMessage("Would you like to view the first 10? [Y/n]");
                        String answer = ComixCliUtils.userInput(userInputScanner);
                        if (answer.equalsIgnoreCase("y")) {
                            ComixCliUtils.printEntries(userInputScanner, currentList);
                        }
                    }
                } else if (bigDB.equals("1")) {
                    currentList = databaseController.getDataCollection();
                    ComixCliUtils.printEntries(userInputScanner, currentList);
                }
                else if(bigDB.equals("exit")){
                    userInputScanner.close();
                    return;
                }
                else if(bigDB.equals("back")){
                    continue;
                }
            } else if (input.equals("2")) {
                ComixCliUtils.printMessage("Would you like to:\n1. See all comics in your personal collection\n2. Search my personal Collection\n3. Add a comic to your collection\n4. Edit a comic in your collection\n5. Remove a comic from your collection\n\n" + 
                "Type 'exit' to quit the program or 'back' to go back to the main menu.");
                String personalCInput1 = ComixCliUtils.userInput(userInputScanner);

                // View Entire Personal Collection
                if (personalCInput1.equals("1")){
                    printFullCollection(null, 10);
                }

                // Search Personal Collection
                else if (personalCInput1.equals("2")){
                    ComixCliUtils.printSearchList(userInputScanner, this, null, 10, null, personalController);
                    // ComixCliUtils.sort(userInputScanner, personalController);

                    ComixCliUtils.printMessage("Would you like to sort the results using a custom order? [Y/n] (Sorted by Series Title, Volume, then Issue Number by default)");
                    String doSort = ComixCliUtils.userInput(userInputScanner);

                    //custom sort
                    if (doSort.equalsIgnoreCase("y")) {
                        //remove menu for a single custom sort?
                        ComixCliUtils.printMessage("Supported custom sort orders:\n1. Publication Date");
                        String sorting = ComixCliUtils.userInput(userInputScanner);
                        ComixCliUtils.sort(sorting, personalController, this);
                    }
                    //default sort
                    else{
                        ComixCliUtils.sort(personalController, this);
                    }
                    
                    if (currentList != null) {
                        ComixCliUtils.printMessage("Would you like to view the first 10? [Y/n]");
                        String answer = ComixCliUtils.userInput(userInputScanner);
                        if (answer.equalsIgnoreCase("y")) {
                            ComixCliUtils.printEntries(userInputScanner, currentList);
                        }
                    }
                }

                // Add to Personal Collection
                else if (personalCInput1.equals("3")){
                    ComixCliUtils.printMessage("Great! Lets search the Comic Database for a comic to add to your Personal Collection!");
                    ComixCliUtils.printSearchList(userInputScanner, this, null, 10, null, databaseController);
                    ComixCliUtils.printMessage("Would you like to sort the results using a custom order? [Y/n] (Sorted by Series Title, Volume, then Issue Number by default)");
                    String doSort = ComixCliUtils.userInput(userInputScanner);

                    //custom sort
                    if (doSort.equalsIgnoreCase("y")) {
                        //remove menu for a single custom sort?
                        ComixCliUtils.printMessage("Supported custom sort orders:\n1. Publication Date");
                        String sorting = ComixCliUtils.userInput(userInputScanner);
                        ComixCliUtils.sort(sorting, databaseController, this);
                    }
                    //default sort
                    else{
                        ComixCliUtils.sort(databaseController, this);
                    }
                    
                    if (currentList != null) {
                        ComixCliUtils.printMessage("Would you like to view the first 10? [Y/n]");
                        String answer = ComixCliUtils.userInput(userInputScanner);
                        if (answer.equalsIgnoreCase("y")) {
                            ComixCliUtils.printEntries(userInputScanner, currentList);
                        }
                    }
                    
                    ComixCliUtils.printMessage("Which Comic would you like to add to your collection?");
                    String selectedComic = ComixCliUtils.userInput(userInputScanner);
                    selectEntry(selectedComic);
                    addEntry();
                }
                // Edit A Comic in Personal Collection 
                else if (personalCInput1.equals("4")) {
                    ComixCliUtils.printMessage("Great! Lets search the Comic Database for a comic to edit within your Personal Collection!");
                    //rename getSearchEntiresWithChildren to Entries
                    ComixCliUtils.printSearchList(userInputScanner, this, personalController.getComixCollection().getSearchEntriesWithChildren(), 10, null, personalController);
                    ComixCliUtils.printMessage("Would you like to sort the results using a custom order? [Y/n] (Sorted by Series Title, Volume, then Issue Number by default)");
                    String doSort = ComixCliUtils.userInput(userInputScanner);

                    //custom sort
                    if (doSort.equalsIgnoreCase("y")) {
                        //remove menu for a single custom sort?
                        ComixCliUtils.printMessage("Supported custom sort orders:\n1. Publication Date");
                        String sorting = ComixCliUtils.userInput(userInputScanner);
                        ComixCliUtils.sort(sorting, personalController, this);
                    }
                    //default sort
                    else{
                        ComixCliUtils.sort(personalController, this);
                    }
                    
                    if (currentList != null) {
                        ComixCliUtils.printMessage("Would you like to view the first 10? [Y/n]");
                        String answer = ComixCliUtils.userInput(userInputScanner);
                        if (answer.equalsIgnoreCase("y")) {
                            ComixCliUtils.printEntries(userInputScanner, currentList);
                        }
                    }
                    
                    ComixCliUtils.printMessage("Select a comic to edit");
                    String editing = ComixCliUtils.userInput(userInputScanner);
                    selectEntry(editing);
            
                    Publisher publisher = this.selected.getPublisher();
                    Series series = this.selected.getSeries();
                    Volume volume = this.selected.getVolume();
                    ComicBook comicBook = this.selected.getComicBook();

                    String response = "";

                    System.out.println("Current Publisher Name: " + publisher.getPublisherName());
                    ComixCliUtils.printMessage("Would you like to edit the publisher name? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new publisher name:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        publisher.setPublisherName(response);
                    }

                    System.out.println("Current Series Number: " + series.getSeriesNumber());
                    ComixCliUtils.printMessage("Would you like to edit the series number? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new series number:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        series.setSeriesNumber(response);
                    }

                    System.out.println("Current Volume Number: " + volume.getVolumeNumber());
                    ComixCliUtils.printMessage("Would you like to edit the volume number? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new volume number:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        volume.setVolumeNumber(Integer.parseInt(response));
                    }

                    System.out.println("Current ComicBook Title: " + comicBook.getTitle());
                    ComixCliUtils.printMessage("Would you like to edit the title? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new title:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        comicBook.setTitle(response);

                    }

                    System.out.println("Current ComicBook Issue Number: " + comicBook.getIssueNumber());
                    ComixCliUtils.printMessage("Would you like to edit the issue number? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new issue number:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        comicBook.setIssueNumber(response);
                    }

                    System.out.println("Current ComicBook Publication Date: " + comicBook.getPublicationDate());
                    ComixCliUtils.printMessage("Would you like to edit the publication date? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new publication date (yyyy-MM-dd):");
                        response = ComixCliUtils.userInput(userInputScanner);
                        try {
                            LocalDate newDate = LocalDate.parse(response, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            comicBook.setPublicationDate(newDate);
                        } catch (DateTimeParseException e) {
                            ComixCliUtils.printMessage("Invalid input, please enter a date in the format yyyy-MM-dd. Date not changed.");
                        }
                    }

                    System.out.println("Current ComicBook Creators: " + Arrays.toString(comicBook.getCreators()));
                    ComixCliUtils.printMessage("Would you like to edit the creators? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a comma-separated list of new creators:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        comicBook.setCreators(response.split(","));
                    }

                    System.out.println("Current ComicBook Principle Characters: " + Arrays.toString(comicBook.getPrincipleCharacters()));
                    ComixCliUtils.printMessage("Would you like to edit the principle characters? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a comma-separated list of new principle characters:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        comicBook.setPrincipleCharacters(response.split(","));
                    }

                    System.out.println("Current ComicBook Description: " + comicBook.getDescription());
                    ComixCliUtils.printMessage("Would you like to edit the description? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new description:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        comicBook.setDescription(response);
                    }

                    System.out.println("Current ComicBook Base Value: " + comicBook.getBaseValue());
                    ComixCliUtils.printMessage("Would you like to edit the base value? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new base value:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        try {
                            float newValue = Float.parseFloat(response);
                            comicBook.setBaseValue(newValue);
                        } catch (NumberFormatException e) {
                            ComixCliUtils.printMessage("Invalid input, please enter a valid number.");
                        }
                    }

                    System.out.println("Current ComicBook Grade: " + comicBook.getGrade());
                    ComixCliUtils.printMessage("Would you like to edit the grade? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new grade:");
                        response = ComixCliUtils.userInput(userInputScanner);
                        try {
                            int newGrade = Integer.parseInt(response);
                            comicBook.setGrade(newGrade);
                        } catch (NumberFormatException e) {
                            ComixCliUtils.printMessage("Invalid input, please enter a valid number.");
                        }
                    }

                    System.out.println("Is the ComicBook Slabbed? " + comicBook.getIsSlabbed());
                    ComixCliUtils.printMessage("Would you like to edit the slabbed status? [Y/n]");
                    response = ComixCliUtils.userInput(userInputScanner);
                    if (response.equalsIgnoreCase("y")) {
                        ComixCliUtils.printMessage("Please provide a new slabbed status (true/false):");
                        response = ComixCliUtils.userInput(userInputScanner);
                        if (response.equalsIgnoreCase("true") || response.equalsIgnoreCase("false")) {
                            boolean newSlabbedStatus = Boolean.parseBoolean(response);
                            comicBook.setIsSlabbed(newSlabbedStatus);
                        } else {
                            ComixCliUtils.printMessage("Invalid input, please enter 'true' or 'false'.");
                        }
                    }

                    comicBook.setParentNode(volume);
                    volume.add(comicBook);
                    volume.setParentNode(series);
                    series.add(volume);
                    series.setParentNode(publisher);
                    publisher.add(series);

                    //creating new instance to ensure the object mapper properly functions
                    SearchEntry edited = new SearchEntry(comicBook, publisher, series, volume);

                    updateEntry(edited);
                }
                // Remove A Comic in Personal Collection
                 else if (personalCInput1.equals("5")) {
                    ComixCliUtils.printMessage("Great! Lets search for a comic to delete from your Personal Collection!");
                    //rename getSearchEntiresWithChildren to Entries
                    ComixCliUtils.printSearchList(userInputScanner, this, personalController.getComixCollection().getSearchEntriesWithChildren(), 10, null, personalController);
                    ComixCliUtils.printMessage("Would you like to sort the results using a custom order? [Y/n] (Sorted by Series Title, Volume, then Issue Number by default)");
                    String doSort = ComixCliUtils.userInput(userInputScanner);

                    //custom sort
                    if (doSort.equalsIgnoreCase("y")) {
                        //remove menu for a single custom sort?
                        ComixCliUtils.printMessage("Supported custom sort orders:\n1. Publication Date");
                        String sorting = ComixCliUtils.userInput(userInputScanner);
                        ComixCliUtils.sort(sorting, personalController, this);
                    }
                    //default sort
                    else{
                        ComixCliUtils.sort(personalController, this);
                    }
                    
                    if (currentList != null) {
                        ComixCliUtils.printMessage("Would you like to view the first 10? [Y/n]");
                        String answer = ComixCliUtils.userInput(userInputScanner);
                        if (answer.equalsIgnoreCase("y")) {
                            ComixCliUtils.printEntries(userInputScanner, currentList);
                        }
                    }
                    
                    ComixCliUtils.printMessage("Select a comic to delete:");
                    String deleting = ComixCliUtils.userInput(userInputScanner);
                    selectEntry(deleting);
                    removeEntry(selected);
                }

                else if(personalCInput1.equals("exit")){
                    userInputScanner.close();
                    return;
                }

                else if(personalCInput1.equals("back")){
                    continue;
                }
            } else if (input.equals("3")) {
                // Comic Statistics
                ComixCollection personal = personalController.getComixCollection();
                ComixCliUtils.printMessage("Would you like to view stats for: \n1. Your entire collection\n2. A publisher in your collection\n3. A series in your collection\n4. A volume in your collection\n5. A comicbook in your collection\n\n" + 
                "Type 'exit' to quit the program or 'back' to go back to the main menu.");
                String choice = getStatsForTiers(personal);
                if(choice!= null && choice.equals("exit")){
                    userInputScanner.close();
                    return;
                }

                else if(choice == null || choice.equals("back")){
                    continue;
                }
            }
        }
    }

    protected void printFullCollection(List<SearchEntry> list, int entriesPerPage) {
        this.currentList = this.personalController.getComixCollection().getSearchEntriesWithChildren();

        for (SearchEntry e: currentList)
            System.out.println(e);
    }

    protected void selectEntry(String name) {
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

    protected String getStatsForTiers(ComixCollection collection) {
        // Scanner ComixCliUtils.userInput = new Scanner(System.in);
        String stats = ComixCliUtils.userInput(userInputScanner);
        if(stats.equals("exit") || stats.equals("back")){
            return stats;
        }
        Integer comicNum;
        Double value;
        if (Integer.parseInt(stats) == 1){
            comicNum = collection.getNumIssues();
            value = collection.getTotalValue();
            ComixCliUtils.printMessage("Total number of comics in your personal collection: " + comicNum);
            ComixCliUtils.printMessage("The total value of comics in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 2) {
            ComixCliUtils.printMessage("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                ComixCliUtils.printMessage(i + ". " + pub.getPublisherName());
                i++;
            }
            ComixCliUtils.printMessage("Which Publisher would you like to get statistics on?");
            String publisher = ComixCliUtils.userInput(userInputScanner);
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            comicNum = pubNode.getNumIssues();
            value = pubNode.getTotalValue();
            ComixCliUtils.printMessage("Total number of comics by " + pubNode.getPublisherName() + " in your personal collection: " + comicNum);
            ComixCliUtils.printMessage("The total value of comics " + pubNode.getPublisherName() + " in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 3) {
            ComixCliUtils.printMessage("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                ComixCliUtils.printMessage(i + ". " + pub.getPublisherName());
                i++;
            }
            ComixCliUtils.printMessage("Which Publisher would you like to get statistics on?");
            String publisher = ComixCliUtils.userInput(userInputScanner);
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            int p = 1;
            for (Series series: pubNode.getChildren()){
                ComixCliUtils.printMessage(p + ". " + series.getSeriesNumber());
                p++;
            }
            ComixCliUtils.printMessage("Which Series would you like to get statistics on?");
            String seri = ComixCliUtils.userInput(userInputScanner);
            Series seriNode = pubNode.getChildNode(Integer.parseInt(seri)-1);
            comicNum = seriNode.getNumIssues();
            value = seriNode.getTotalValue();
            ComixCliUtils.printMessage("Total number of comics in " + seriNode.getSeriesNumber() + " in your personal collection: " + comicNum);
            ComixCliUtils.printMessage("The total value of comics in " + seriNode.getSeriesNumber() + " in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 4) {
            ComixCliUtils.printMessage("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                ComixCliUtils.printMessage(i + ". " + pub.getPublisherName());
                i++;
            }
            ComixCliUtils.printMessage("Which Publisher would you like to get statistics on?");
            String publisher = ComixCliUtils.userInput(userInputScanner);
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            int p = 1;
            for (Series series: pubNode.getChildren()){
                ComixCliUtils.printMessage(p + ". " + series.getSeriesNumber());
                p++;
            }
            ComixCliUtils.printMessage("Which Series would you like to get statistics on?");
            String seri = ComixCliUtils.userInput(userInputScanner);
            Series seriNode = pubNode.getChildNode(Integer.parseInt(seri)-1);
            int s = 1;
            for (Volume volume: seriNode.getChildren()){
                ComixCliUtils.printMessage(s + ". Volume #" + volume.getVolumeNumber());
                s++;
            }
            ComixCliUtils.printMessage("Which Volume would you like to get statistics on?");
            String vol = ComixCliUtils.userInput(userInputScanner);
            Volume volumeNode = seriNode.getChildNode(Integer.parseInt(vol)-1);
            comicNum = volumeNode.getNumIssues();
            value = volumeNode.getTotalValue();
            ComixCliUtils.printMessage("Total number of comics in Volume #" + volumeNode.getVolumeNumber() + " in your personal collection: " + comicNum);
            ComixCliUtils.printMessage("The total value of comics in Volume #" + volumeNode.getVolumeNumber() + " in your personal collection is: $" + value + "\n");
        } else if (Integer.parseInt(stats) == 5) {
            ComixCliUtils.printMessage("Let's select a Publisher:");
            int i = 1;
            for (Publisher pub: collection.getChildren()){
                ComixCliUtils.printMessage(i + ". " + pub.getPublisherName());
                i++;
            }
            ComixCliUtils.printMessage("Which Publisher would you like to get statistics on?");
            String publisher = ComixCliUtils.userInput(userInputScanner);
            Publisher pubNode = collection.getChildNode(Integer.parseInt(publisher)-1);
            int p = 1;
            for (Series series: pubNode.getChildren()){
                ComixCliUtils.printMessage(p + ". " + series.getSeriesNumber());
                p++;
            }
            ComixCliUtils.printMessage("Which Series would you like to get statistics on?");
            String seri = ComixCliUtils.userInput(userInputScanner);
            Series seriNode = pubNode.getChildNode(Integer.parseInt(seri)-1);
            int s = 1;
            for (Volume volume: seriNode.getChildren()){
                ComixCliUtils.printMessage(s + ". Volume #" + volume.getVolumeNumber());
                s++;
            }
            ComixCliUtils.printMessage("Which Volume would you like to get statistics on?");
            String vol = ComixCliUtils.userInput(userInputScanner);
            Volume volumeNode = seriNode.getChildNode(Integer.parseInt(vol)-1);
            int v = 1;
            for (ComicBook comic: volumeNode.getChildren()){
                ComixCliUtils.printMessage(v + ". " + comic.getTitle());
                v++;
            }
            ComixCliUtils.printMessage("Which Comic would you like to get statistics on?");
            String comi = ComixCliUtils.userInput(userInputScanner);
            ComicBook comicNode = volumeNode.getChildNode(Integer.parseInt(comi)-1);
            comicNum = comicNode.getNumIssues();
            value = comicNode.getTotalValue();
            ComixCliUtils.printMessage("Total number of comics in " + comicNode.getTitle() + " in your personal collection: " + comicNum);
            ComixCliUtils.printMessage("The total value of the comic " + comicNode.getTitle() + " in your personal collection is: $" + value + "\n");
        }
        return null;
    }
}

