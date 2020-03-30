package fuellogger.dao;

import java.io.File;
import java.sql.*;

public class Database {

    Connection db;

    public Database() throws SQLException {
        // check if database file already exists
        File check = new File("database.db");
        
        if (check.exists()) {
            // if exists, establish a connection
            db = DriverManager.getConnection("jdbc:sqlite:database.db");
        } else {
            // if not, establish a connection and create tables
            db = DriverManager.getConnection("jdbc:sqlite:database.db");
            
            Statement s = db.createStatement();
            s.execute("CREATE TABLE Car (id INTEGER PRIMARY KEY, name TEXT, fuel_capacity INTEGER)");
            s.execute("CREATE TABLE Refill (id INTEGER PRIMARY KEY, car_id INTEGER, odometer INTEGER, day INTEGER, month INTEGER, year INTEGER)");
        }

    }
}
