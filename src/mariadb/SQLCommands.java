package mariadb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLCommands {
    Database connection;
    Connection conn;
    ResultSet rs = null;
    Statement stmt = null;

    public SQLCommands(Database connector) {
        connection = connector;
        conn = connection.conn;

    }

    //Table-related commands
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void changeListName(String oldTableName, String newTableName) {
        String sql = "ALTER TABLE " + oldTableName +
                " RENAME TO " + newTableName + ";";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int checkIfTableExists(String tableName) {
        //tableName = tableName.toLowerCase();
        String sql = "SHOW TABLES;";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(tableName)) {
                    //System.out.print(tableName + " already exists!");
                    return 0;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void createList(String listName) { //GOOD TO GO!!!
        String sql = "CREATE TABLE " + listName
                + "(item VARCHAR(255) NOT NULL,"
                + " section VARCHAR(255),"
                + " price DECIMAL(10,2),"
                + " link VARCHAR(255),"
                + " PRIMARY KEY (item, section));";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //getTableCount will find the number of items in a given list.
    public int getTableCount(String tableName) { //GOOD TO GO!!!
        int count = 0;
        String sql = "SELECT COUNT(*) AS rowcount"
        + " FROM "+ tableName + ";";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();
            count = rs.getInt("rowcount");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    //getTables will show all the lists in the database.
    public int getTables() {
        String sql = "SHOW TABLES;";
        String str;
        String cap;
        int count = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if(count == 0) {
                    System.out.println("Here are your current lists:");
                }
                str = rs.getString(1);
                cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                System.out.print("List " + (count+1) + ": " + cap);
                System.out.println();
                count++;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            System.out.println("You don't have any current Christmas lists.");
        }
        return count;
    }

    //printTable prints out the contents of a list. FIXME Need to print it out better. Perhaps store in 2D vector first.
    public void printTable(String tableName) {
        String sql = "SELECT * FROM " + tableName + ";";
        int count = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                if (count == 0) {
                    System.out.println();
                    System.out.println("Item,\tSection,\tPrice,\tLink");
                    System.out.println("_____________________________________________________________________");
                }
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",\t");
                    String columnValue = rs.getString(i);
                    //System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    System.out.print(columnValue);
                }
                System.out.println();
                count++;
            }
            System.out.println();

            /*while(rs.next()) {
                str = rs.getString(1);
                System.out.println();
                count++;
            }*/
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Item-related commands
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Creates a new list in the Christmas List database.

    public int checkIfItemExists(String tableName, String item, String sectionName) {
        //tableName = tableName.toLowerCase();
        tableName = tableName.toLowerCase();
        item = item.toLowerCase();
        sectionName = sectionName.toLowerCase();
        String sql = "SELECT * FROM " + tableName
                + " WHERE item = '" + item + "' AND section = '" + sectionName + "';";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(item)) {
                    //System.out.print(item + " already exists!");
                    return 0;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void dropTable(String tableName) {
        String sql = "DROP TABLE " + tableName + ";";
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
            System.out.println("List successfully deleted.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItem(String item, String tableName, String sectionName, double price, String link) {
        String sql = "INSERT INTO " + tableName + " (item, section, price, link)" +
                " Values('" + item + "', '" + sectionName + "', " + price + ", '" + link + "');";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(String tableName, String item, String sectionName) {
        String sql = "DELETE FROM " + tableName + " WHERE item = '" + item
                + "' AND section = '" + sectionName + "';";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSection(String tableName, String sectionName) { //FIXME VERIFY
        String sql = "DELETE FROM " + tableName + " WHERE section = '" + sectionName + "';";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DELETE FROM table_name WHERE condition;

    //ADD method
    //REMOVE method
    //DELETE LIST method
}
