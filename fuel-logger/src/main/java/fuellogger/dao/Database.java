package fuellogger.dao;

import fuellogger.domain.Car;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    Connection db;
    
    public Database(String filename) throws SQLException {
        
        // check if database file already exists
        File check = new File(filename);

        if (check.exists()) {
            // if exists, establish a connection
            db = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } else {
            // if not, establish a connection and create tables
            db = DriverManager.getConnection("jdbc:sqlite:" + filename);

            Statement s = db.createStatement();
            s.execute("CREATE TABLE Car (id INTEGER PRIMARY KEY, name TEXT,"
                    + " fuel_capacity INTEGER)");
            s.execute("CREATE TABLE Refill (id INTEGER PRIMARY KEY, "
                    + "car_id INTEGER, odometer INTEGER, day INTEGER,"
                    + " month INTEGER, year INTEGER)");
        }

    }

    public boolean addCar(Car car) {
        try (PreparedStatement s = db.prepareStatement("INSERT INTO Car(name, fuel_capacity) VALUES (?, ?)")) {
            s.setString(1, car.getName());
            s.setInt(2, car.getFuelcapacity());
            s.executeUpdate();
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
    
    public void clear() throws SQLException {
        // used for tests
        Statement s = db.createStatement();
        s.execute("DELETE FROM Car");
        s.execute("DELETE FROM Refill");
        
    }
    

}
