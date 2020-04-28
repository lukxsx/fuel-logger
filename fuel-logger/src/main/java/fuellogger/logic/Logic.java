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
        this.db.addRefill(refueling);
    }

    public ArrayList<Refueling> getRefuelingsFromDB(Car car) throws SQLException {
        return db.getRefuelings(car);
    }

    public ArrayList<Refueling> getRefuelings(Car car) {
        return this.refuelings.get(car);
    }

    public double avgConsumption(Car car) throws SQLException {
        ArrayList<Refueling> refs = this.refuelings.get(car);
        if (refs.isEmpty() || refs.size() == 1) {
            return 0;
        }
        double litres = 0;
        for (int i = 1; i < refs.size(); i++) {
            litres = litres + refs.get(i).volume;
        }

        Collections.sort(refs);

        int kms = refs.get(refs.size() - 1).odometer - refs.get(0).odometer;

        return litres / kms * 100;
    }

    public double getConsumption(Car car, Refueling refueling) {
        ArrayList<Refueling> refs = this.refuelings.get(car);
        Collections.sort(refs);
        int index = refs.indexOf(refueling);
        if (index == refs.size() - 1) {
            return 0;
            // can't count consumption from the latest refueling
            // because we don't know about the next refueling yet
        }

        Refueling next = refs.get(index + 1);

        int kms = next.odometer - refueling.odometer;
        return next.volume / kms * 100;
    }

    public double monthAvg(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = refuelingsInMonth(car, month, year);

        double sum = 0;
        int counter = 0;
        for (Refueling r : monthlyrefuelings) {
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
    
    public ArrayList<Refueling> refuelingsInMonth(Car car, int month, int year) {
        ArrayList<Refueling> allrefuelings = getRefuelings(car);
        ArrayList<Refueling> valid = new ArrayList<>();
        
        for (Refueling r : allrefuelings) {
            if (r.getDate().getMonthValue() == month && r.getDate().getYear() == year) {
                valid.add(r);
            }
        }
        return valid;
    }
    
    public int kmsInMonth(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = refuelingsInMonth(car, month, year);
        int km1 = monthlyrefuelings.get(0).getOdometer();
        int km2 = monthlyrefuelings.get(monthlyrefuelings.size() - 1).getOdometer();
        return Math.abs(km2 - km1);
    }
    
    public int numberOfRefuelings(Car c) {
        return this.refuelings.get(c).size();
    }
    
    public double totalVolume(Car c) {
        double volume = 0;
        for (Refueling r: this.refuelings.get(c)) {
            volume = volume + r.getVolume();
        }
        return volume;
    }
}
