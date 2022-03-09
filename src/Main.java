import Menus.MainMenu;
import mariadb.Database;

import java.sql.SQLException;

/*The Main class holds the start-up procedure for the program. A connection with the Database is established and
* stored as an object that will be passed through different classes. If the connection fails, the program exits.
* Once a connection is open, the program transfers over to the MainMenu class for further action. When the program
* exits the MainMenu class, the database connection is closed and the program exits.*/

public class Main {

    public static void main(String [] args) {
        try {
            Database connector = new Database();
            connector.connect();
            MainMenu menu = new MainMenu();
            menu.OpenMenu(connector);
            connector.closer();
            System.out.println("Merry Christmas!");
        }
        catch(SQLException e) {
            System.out.println("HO HO NO!");
            //e.printStackTrace();
        }
        catch(Exception e) {
            System.out.println("Another problem occurred.");
        }
    }
}
