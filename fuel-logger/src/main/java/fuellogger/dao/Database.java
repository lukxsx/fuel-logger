package fuellogger.dao;

import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
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
            s.execute("CREATE TABLE Car (id INTEGER PRIMARY KEY, name TEXT UNIQUE,"
                    + " fuel_capacity INTEGER)");
            s.execute("CREATE TABLE Refueling (id INTEGER PRIMARY KEY, "
                    + "car_id INTEGER, odometer INTEGER, volume REAL"
                    + ", day INTEGER, month INTEGER, year INTEGER)");
        }

    }

    public boolean addCar(Car car) {
        try (PreparedStatement s = db.prepareStatement("INSERT INTO Car (name, fuel_capacity) VALUES (?, ?)")) {
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
    
    public boolean addRefill(Car car, Refueling refueling) throws SQLException {
        int carid = getCarId(car);
        if (carid == 0) {
            return false;
        }
        try (PreparedStatement s = db.prepareStatement("INSERT INTO Refueling "
                + "(car_id, odometer, volume, day, month, year) VALUES "
                + "(?, ?, ?, ?, ?, ?)")) {
            s.setInt(1, getCarId(car));
            s.setInt(2, refueling.odometer);
            s.setDouble(3, refueling.volume);
            s.setInt(4, refueling.day);
            s.setInt(5, refueling.month);
            s.setInt(6, refueling.year);
            s.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public int getCarId(Car car) throws SQLException {
        PreparedStatement carIdQuery = db.prepareStatement("SELECT id FROM Car WHERE name=?");
        carIdQuery.setString(1, car.getName());
        ResultSet idQueryResults = carIdQuery.executeQuery();
        if (idQueryResults.next()) {
            return idQueryResults.getInt("id");
        } else {
            return 0;
        }
        
    }
    
    public void clear() throws SQLException {
        // used for tests
        Statement s = db.createStatement();
        s.execute("DELETE FROM Car");
        s.execute("DELETE FROM Refill");
        
    }
    

}
