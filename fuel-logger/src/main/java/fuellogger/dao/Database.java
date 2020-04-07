package fuellogger.dao;

import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
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
                    + " fuel_capacity INTEGER NOT NULL)");
            s.execute("CREATE TABLE Refueling (id INTEGER PRIMARY KEY, "
                    + "car_id INTEGER, odometer INTEGER UNIQUE, volume REAL"
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
            s.setInt(4, refueling.date.getDayOfMonth());
            s.setInt(5, refueling.date.getMonthValue());
            s.setInt(6, refueling.date.getYear());
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

    public Car getCar(int id) throws SQLException {
        PreparedStatement carQuery = db.prepareStatement("SELECT * FROM Car WHERE id=?");
        carQuery.setInt(1, id);
        ResultSet carQresults = carQuery.executeQuery();
        if (carQresults.next()) {
            return new Car(carQresults.getString("name"), carQresults.getInt("fuel_capacity"));
        } else {
            return null;
        }

    }

    public ArrayList<Refueling> getRefuelings() throws SQLException {
        ArrayList<Refueling> refuelings = new ArrayList<>();
        PreparedStatement refQuery = db.prepareStatement("SELECT * FROM Refueling");
        ResultSet refResults = refQuery.executeQuery();
        while (refResults.next()) {
            LocalDate d = LocalDate.of(refResults.getInt("year"),
                    refResults.getInt("month"), refResults.getInt("day"));
            refuelings.add(new Refueling(getCar(refResults.getInt("car_id")),
                    refResults.getInt("odometer"),
                    refResults.getDouble("volume"), d));
        }
        return refuelings;
    }

    public ArrayList<Refueling> getRefuelings(Car car) throws SQLException {
        ArrayList<Refueling> refuelings = new ArrayList<>();
        PreparedStatement refQuery = db.prepareStatement("SELECT * FROM Refueling WHERE car_id=?");
        refQuery.setInt(1, getCarId(car));
        ResultSet refResults = refQuery.executeQuery();
        while (refResults.next()) {
            LocalDate d = LocalDate.of(refResults.getInt("year"),
                    refResults.getInt("month"), refResults.getInt("day"));
            Refueling r = new Refueling(car, refResults.getInt("odometer"), refResults.getDouble("volume"), d);
            refuelings.add(r);
        }

        return refuelings;
    }

    public void clear() throws SQLException {
        // used for tests
        Statement s = db.createStatement();
        s.execute("DELETE FROM Car");
        s.execute("DELETE FROM Refueling");

    }

}
