package fuellogger.logic;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
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
        for (Car c : this.cars) {
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
    }

    public ArrayList<Refueling> getRefuelingsFromDB(Car car) throws SQLException {
        return db.getRefuelings(car);
    }

    public ArrayList<Refueling> getRefuelings(Car car) {
        return this.refuelings.get(car);
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

        int kms = refuelings.get(refuelings.size() - 1).odometer - refuelings.get(0).odometer;

        return litres / kms * 100;
    }

    public double getConsumption(Car car, Refueling refueling) {
        ArrayList<Refueling> refuelings = this.refuelings.get(car);
        Collections.sort(refuelings);
        int index = refuelings.indexOf(refueling);
        if (index == refuelings.size() - 1) {
            return 0;
            // can't count consumption from the latest refueling
            // because we don't know about the next refueling yet
        }

        Refueling next = refuelings.get(index + 1);

        int kms = next.odometer - refueling.odometer;
        return next.volume / kms * 100;
    }

    public double monthAvg(Car car, int month) {
        ArrayList<Refueling> refuelings = getRefuelings(car);
        ArrayList<Refueling> valid = new ArrayList<>();
        
        for (Refueling r : refuelings) {
            if (r.getDate().getMonthValue() == month) {
                valid.add(r);
            }
        }

        double sum = 0;
        int counter = 0;
        for (Refueling r : valid) {
            double cons = getConsumption(car, r);
            if (cons != 0) {
                sum = sum + cons;
                counter++;
            }
        }
        
        if (sum == 0) {
            return 0;
        }
        
        return (double) sum / (double) counter;
    }
}
