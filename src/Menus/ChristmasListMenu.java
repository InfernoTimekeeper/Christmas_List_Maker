package Menus;

import mariadb.Database;
import mariadb.SQLCommands;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class ChristmasListMenu {

    Database connector;
    Scanner userInput;
    SQLCommands sqlCommands;
    String tableName;

    //Constructor
    public ChristmasListMenu(Database connector) {
        this.connector = connector;
        userInput = new Scanner(System.in);
        sqlCommands = new SQLCommands(connector);
    }

    //Choose which list to edit. If there are no lists or user enters bad list, return false and return to main menu.
    public boolean setUpMenu() {
        if (sqlCommands.getTables() == 0) {
            return false;
        }
        System.out.println("What list would you like to modify? Enter '0' to cancel.");
        tableName = userInput.nextLine();
        if (Objects.equals(tableName, "0")) {
            return false;
        }
        else if (sqlCommands.checkIfTableExists(tableName) == 0) {
            System.out.println("Let's modify some Christmas Magic!");
        }
        else {
            System.out.println("Error: List not found.\n");
            return false;
        }
        return true;
    }

    public void OpenMenu() {
        int count;
        int option = 0;
        String[] sectionNames = {"Clothing", "Entertainment, Movies, and Games", "Toys"};


        while (option != 6) {
            count = sqlCommands.getTableCount(tableName);
            System.out.println("There are " + count + " items in this list.");
            System.out.println("What would like to do?");
            System.out.println("1. Add item"); //Finished functionally, but link section needs some verification.
            System.out.println("2. Remove item"); //Finished.
            System.out.println("3. Remove all items in a section"); //Finished.
            System.out.println("4. Rename list"); //FINISHED.
            System.out.println("5. View current list"); //FINISHED, BUT COULD BE STYLED BETTER.
            System.out.println("6. Exit");
            try {
                option = userInput.nextInt();
                userInput.nextLine();
                switch (option) {
                    case 1:
                        addItem(sectionNames);
                        continue;
                    case 2:
                        removeItem(sectionNames);

                        continue;
                    case 3:
                        //Do Option 3
                        removeSection(sectionNames);
                        continue;
                    case 4:
                        //Change the name of the current list.
                        changeListName();

                        continue;
                    case 5:
                        //Print the contents of the current list.
                        System.out.println("Printing list...");
                        sqlCommands.printTable(tableName);
                        continue;
                    default:
                        if (option != 6) {
                            System.out.println("ERROR: Please enter a valid number.");
                        }
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Please enter a number.");
                userInput.nextLine();
            }
        }
    }


    //METHODS FOR MENU OPTIONS
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addItem(String[] sectionNames) {
        int j;
        double price;
        String link;
        String sectionName;
        String item;
        System.out.println("What item would you like to add? Enter '0' to cancel.");
        item = userInput.nextLine();
        if (!item.equals("0")) {
            do {
                System.out.println("What type of item is this?");
                for (int i = 1; i < sectionNames.length + 1; i++) {
                    System.out.println(i + ". " + sectionNames[i - 1]);
                }
                j = userInput.nextInt() - 1;
                userInput.nextLine();

            } while (j <= -1 || j >= sectionNames.length);
            sectionName = sectionNames[j];

            while (true) {
                try {
                    System.out.println("What is the price of this item?"); //FIXME
                    price = userInput.nextDouble();
                    userInput.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Error: Please enter a valid number.");
                    userInput.nextLine();
                }
            }

            System.out.println("What is the link for this item? Enter N/A if no link.");
            link = userInput.nextLine(); //FIXME Use https://www.geeksforgeeks.org/check-if-url-is-valid-or-not-in-java/

            sqlCommands.addItem(item, tableName, sectionName, price, link);

        }
    }

    private void changeListName() {
        String item;
        System.out.println("What name would you like this list to be called" +
                "? Enter 0 to cancel.");
        item = userInput.nextLine();
        if (!Objects.equals(item, "0")) {
            int confirmation = -1;
            while (confirmation != 1 && confirmation != 0) {
                System.out.println("Are you sure you want to change the name? Yes (0) or No (1)");
                confirmation = userInput.nextInt();
                userInput.nextLine();
                switch (confirmation) {
                    case 0 -> {
                        if(item.equalsIgnoreCase(tableName)) {
                            System.out.println("ERROR: You can't name your list the same thing! (ignoring case)");
                            return;
                        }
                        System.out.println("Changing...");
                        sqlCommands.changeListName(tableName, item);
                        tableName = item;
                        System.out.println("Change complete. Your list is now called '"
                                + tableName + ".'");
                    }
                    case 1 -> {
                    }
                    default -> System.out.println("Not a valid option.");
                }
            }
            System.out.println();
        }
    }

    private void removeItem(String[] sectionNames) {
        int j;
        String sectionName;
        String item;
        //Remove an item from the database. User must know which section the item belongs in.
        System.out.println("What item would you like to remove? Enter '0' to cancel.");
        item = userInput.nextLine();
        if (!item.equals("0")) {
            do {
                System.out.println("What type of item is this?");
                for (int i = 1; i < sectionNames.length + 1; i++) {
                    System.out.println(i + ". " + sectionNames[i - 1]);
                }
                j = userInput.nextInt() - 1;
                userInput.nextLine();

            } while (j <= -1 || j >= sectionNames.length);
            sectionName = sectionNames[j];

            int confirmation = -1;
            while (confirmation != 1 && confirmation != 0) {
                System.out.println("Are you sure you want to remove this item? Yes (0) or No (1)");
                confirmation = userInput.nextInt();
                userInput.nextLine();
                switch (confirmation) {
                    case 0 -> {
                        if (sqlCommands.checkIfItemExists(tableName, item, sectionName) == 0) {
                            System.out.println("Removing...");
                            sqlCommands.removeItem(tableName, item, sectionName);
                        } else {
                            System.out.println("Error: This item does not exist!");
                        }
                    }
                    case 1 -> {
                    }
                    default -> System.out.println("Not a valid option.");
                }
            }
            System.out.println();

        }
    }

    private void removeSection(String [] sectionNames) {
        int j;
        String sectionName;
            do {
                System.out.println("What section would you like to remove? Enter '0' to cancel.");
                for (int i = 1; i < sectionNames.length + 1; i++) {
                    System.out.println(i + ". " + sectionNames[i - 1]);
                }
                j = userInput.nextInt() - 1;
                userInput.nextLine();

            } while (j <= -1 || j >= sectionNames.length);
            if (j == 0) {
                return;
            }
            sectionName = sectionNames[j];

            int confirmation = -1;
            while (confirmation != 1 && confirmation != 0) {
                System.out.println("Are you sure you want to remove all items in this section? Yes (0) or No (1)");
                confirmation = userInput.nextInt();
                userInput.nextLine();
                switch (confirmation) {
                    case 0 -> {
                            System.out.println("Removing...");
                            sqlCommands.removeSection(tableName, sectionName);
                    }
                    case 1 -> {
                    }
                    default -> System.out.println("Not a valid option.");
                }
            }
            System.out.println();
    }


}
