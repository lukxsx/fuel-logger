package fuellogger.dao;

import java.sql.*;

public class Database {

    Connection db;

    public Database() throws SQLException {
        db = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

}
