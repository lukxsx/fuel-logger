package fuellogger.domain;

import fuellogger.dao.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Logic {

    private Database db;
    public ArrayList<Car> cars;
    public HashMap<Car, ArrayList<Refueling>> refuelings;

    public Logic(String database) throws SQLException {
        this.db = new Database(database);
        this.cars = db.getCars();
        this.refuelings = new HashMap<>();
        for (Car c: this.cars) {
            this.refuelings.put(c, getRefuelingsFromDB(c));
        }
        
    }

    public void addCar(Car car) {
        if (!this.cars.contains(car)) {
            this.cars.add(car);
            this.db.addCar(car);
            if (!this.refuelings.containsKey(car)) {
                this.refuelings.put(car, new ArrayList<>());
            }
        }
    }
    
    public void clearDB() throws SQLException {
        // used for tests
        db.clear();
    }

    public void addRefueling(Car car, Refueling refueling) throws SQLException {
        this.refuelings.get(car).add(refueling);
        this.db.addRefill(car, refueling);
        /*if (!this.refuelings.contains(refueling)) {
            this.refuelings.add(refueling);
            
        }
         */
    }

    public ArrayList<Refueling> getRefuelingsFromDB(Car car) throws SQLException {
        return db.getRefuelings(car);
    }

    public double avgConsumption(Car car) throws SQLException {
        ArrayList<Refueling> refuelings = this.refuelings.get(car);
        if (refuelings.size() == 0 || refuelings.size() == 1) {
            return 0;
        }
        double litres = 0;
        for (int i = 1; i < refuelings.size(); i++) {
            litres = litres + refuelings.get(i).volume;
        }

        Collections.sort(refuelings);
        for (int i = 0; i < refuelings.size(); i++) {
        }

        int kms = refuelings.get(refuelings.size() - 1).odometer - refuelings.get(0).odometer;

        return litres / kms * 100;
    }
}
