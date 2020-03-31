package fuellogger.dao;

import fuellogger.domain.Car;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
            s.execute("CREATE TABLE Car (id INTEGER PRIMARY KEY, name TEXT,"
                    + " fuel_capacity INTEGER)");
            s.execute("CREATE TABLE Refill (id INTEGER PRIMARY KEY, "
                    + "car_id INTEGER, odometer INTEGER, day INTEGER,"
                    + " month INTEGER, year INTEGER)");
        }

    }

    public boolean addCar(Car car) {
        try ( Statement s = db.createStatement()) {
            s.execute("INSERT INTO Car (name, fuel_capacity) VALUES "
                    + "(" + car.getName() + ", " + car.getFuelcapacity() + ")");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ArrayList<Car> getCars() throws SQLException {
        ArrayList<Car> cars = new ArrayList<>();
        PreparedStatement carQuery = db.prepareStatement("SELECT * FROM Car");
        ResultSet carResults = carQuery.executeQuery();
        while (carResults.next()) {
            cars.add(new Car(carResults.getString("name"),
                    carResults.getInt("fuel_capacity")));
        }
        return cars;
    }
}
