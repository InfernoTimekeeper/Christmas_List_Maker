package Menus;
import mariadb.Database;
import mariadb.SQLCommands;

import java.util.InputMismatchException;
import java.util.Scanner;

/*When the program starts up successfully, this is the first menu that greets the user.
* First, the menu prints out the options for the user to see. Then, a Scanner object will take the user's input
* and perform a particular action based on the result; if the user enters bad input, an error occurs and reprints
* the menu options.*/

public class MainMenu {

    public void OpenMenu(Database connector) {
        Scanner userInput = new Scanner(System.in);
        SQLCommands sqlCommands = new SQLCommands(connector); //FIXME May want to pass this into menus.
        String tableName;
        int option = 0;
        System.out.println("Welcome to the Christmas List Maker!");
        while(option != 5) {
            System.out.println("What would like to do?");
            System.out.println("1. Make new list"); //Finished
            System.out.println("2. Modify existing list"); //Finished here, but finish in object file.
            System.out.println("3. Send list"); //FIXME NOT DONE
            System.out.println("4. Delete list"); //Finished
            System.out.println("5. Exit"); //Finished
            try {
                option = userInput.nextInt();
                userInput.nextLine();
                switch (option) {
                    case 1: //Prints current lists, asks user for table name, checks if it already exists, and adds.
                        sqlCommands.getTables();
                        System.out.println("What would you like to name this list? Type '0' to cancel.");
                        tableName = userInput.nextLine();
                        if (!tableName.equals("0")) {
                            if(sqlCommands.checkIfTableExists(tableName) != 0) {
                                sqlCommands.createList(tableName);
                                String cap = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
                                System.out.println(cap + " successfully created!");
                            }
                            else {
                                System.out.println(tableName + " already exists!");
                            }
                        }
                        continue;

                    case 2: //Go to Christmas List Menu. It will ask for a list to edit there.
                        ChristmasListMenu cMenu = new ChristmasListMenu(connector);
                        //cMenu.setUpMenu(connector);
                        if (cMenu.setUpMenu()) {
                            cMenu.OpenMenu();
                        }
                        continue;

                    case 3:
                        /*FIXME
                        *  1. Download necessary APIs for Java Emails.
                        *  2. Ask which list to send or 0 to exit.
                        *  3. Get the email(s) for the list to be sent or 0 to exit.
                        *  4. Verify one last time if the user wants to send the list to said emails.
                        *  5. Send emails.*/
                        System.out.println("Feature not available yet. (Wait until next X-Mas!)");
                        continue;

                    case 4: //Print current lists. Ask what list user wants to be dropped, verify, and delete.
                        if (sqlCommands.getTables() == 0) {
                            continue;
                        }
                        System.out.println("Which list do you want to remove? Type '0' to cancel.");
                        tableName = userInput.nextLine();
                        if (!tableName.equals("0")) {
                            int confirmation = -1;
                            while(confirmation != 1 && confirmation != 0) {
                                System.out.println("Are you sure you want to delete this list? Yes (0) or no (1)");
                                confirmation = userInput.nextInt();
                                userInput.nextLine();
                                switch (confirmation) {
                                    case 0 -> {
                                        if(sqlCommands.checkIfTableExists(tableName) == 0) {
                                            System.out.println("Deleting...");
                                            sqlCommands.dropTable(tableName);
                                        }
                                        else {
                                            System.out.println("Error: This list does not exist!");
                                        }
                                    }
                                    case 1 -> {}
                                    default -> System.out.println("Not a valid option.");
                                }
                            }
                        }
                            continue;
                    default:
                        if (option != 5) {
                            System.out.println("ERROR: Please enter a valid number.");
                        }
                }
            }
            catch(InputMismatchException e) {
                System.out.println("ERROR: Please enter a number.");
                userInput.nextLine();
            }

        }

    }
}
