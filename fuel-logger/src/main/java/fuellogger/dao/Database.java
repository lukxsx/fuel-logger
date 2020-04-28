package fuellogger.dao;

import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {

    Connection db;

    public Database(String filename) {

        try {
            db = DriverManager.getConnection("jdbc:sqlite:" + filename);

            Statement s = db.createStatement();
            s.execute("CREATE TABLE IF NOT EXISTS Car (id INTEGER PRIMARY KEY, name TEXT UNIQUE,"
                    + " fuel_capacity INTEGER NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS Refueling (id INTEGER PRIMARY KEY, "
                    + "car_id INTEGER, odometer INTEGER UNIQUE, volume REAL"
                    + ", price REAL, day INTEGER, month INTEGER, year INTEGER)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    public boolean addRefill(Refueling refueling) throws SQLException {
        int carid = getCarId(refueling.car);
        if (carid == 0) {
            return false;
        }
        try (PreparedStatement s = db.prepareStatement("INSERT INTO Refueling "
                + "(car_id, odometer, volume, price, day, month, year) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)")) {
            s.setInt(1, carid);
            s.setInt(2, refueling.odometer);
            s.setDouble(3, refueling.volume);
            s.setDouble(4, refueling.price);
            s.setInt(5, refueling.date.getDayOfMonth());
            s.setInt(6, refueling.date.getMonthValue());
            s.setInt(7, refueling.date.getYear());
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
                    refResults.getDouble("volume"), 
                    refResults.getDouble("price"), d));
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
            Refueling r = new Refueling(car, refResults.getInt("odometer"), 
                    refResults.getDouble("volume"), refResults.getDouble("price"), d);
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
