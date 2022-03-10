package mariadb;

import java.sql.*;

public class Database {

        static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
        static final String DB_URL = "jdbc:mariadb://localhost:3306/";

        //  Database credentials
        static final String USER = ""; //FIXME ADD IN YOUR USERNAME FOR MARIADB.
        static final String PASS = ""; //FIXME ADD IN YOUR PASSWORD FOR MARIADB.

        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        String dbName = "CL_Lists"; //NOTE: This is the default name of the database created and used. You can rename it if you wish.

        public void connect() throws SQLException, ClassNotFoundException {
            try {
                //Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //Open Connection
                System.out.println("Connecting...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("CONNECTED!");

                //If connection is made, check and see if database exists. If so, connect. If not, create it.
                //If connection not made, then we cannot connect.
                if (conn != null) {
                    rs = conn.getMetaData().getCatalogs();
                    while(rs.next()){
                        String catalogs = rs.getString(1);
                        if(dbName.equalsIgnoreCase(catalogs)){
                            //Connect to database.
                            changeDB(conn, dbName);
                            return;
                        }
                    }

                    //Create database, connect, and break.
                    stmt = conn.createStatement();
                    String sql = "CREATE DATABASE " + dbName;
                    stmt.executeUpdate(sql);
                    changeDB(conn, dbName);
                }
                else{
                    System.out.println("Unable to create database connection1.");
                    throw new SQLException();
                }
            }
            catch (SQLException e) {
                System.out.println("Unable to create database connection.");
                //e.printStackTrace();
                throw e;
            }
            catch (Exception e) {

                //Handle errors for Class.forName
                System.out.println("Other exception encountered.");
                throw e;
            }
        }

        public void changeDB(Connection conn, String databaseName) {
            try {
                conn.setCatalog(databaseName.toLowerCase());
            }
            catch (SQLException e) {
                System.out.println("ERROR: Unable to connect to database right now.");
            }
        }

    public void closer() {
            try { //WE MAY NEED TO CHECK IF stmt does not equal NULL.
                System.out.println("Closing lists...");
                conn.close();
                System.out.println("CLOSED.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
}
