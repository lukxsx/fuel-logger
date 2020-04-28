package fuellogger.logic;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class handles all logic behind management of cars and refuelings. It handles also
 * calculations for statistics and charts. 
 * 
 */
public class Logic {

    private Database db;
    
    /**
     * A list of all locally stored cars
     */
    public ArrayList<Car> cars;
    
    /**
     * Stores refuelings locally
     */
    public HashMap<Car, ArrayList<Refueling>> refuelings;

    public Logic(String database) {
        this.db = new Database(database);
        this.cars = db.getCars();
        this.refuelings = new HashMap<>();
        for (Car c : this.cars) {
            this.refuelings.put(c, getRefuelingsFromDB(c));
        }

    }

    /**
     * Adds a car locally and to the database
     * @param car car to be added
     */
    public void addCar(Car car) {
        if (!this.cars.contains(car)) {
            this.cars.add(car);
            this.db.addCar(car);
            if (!this.refuelings.containsKey(car)) {
                this.refuelings.put(car, new ArrayList<>());
            }
        }
    }

    /**
     * Clears the database (used for tests)
     */
    public void clearDB() {
        db.clear();
    }

    /**
     * Adds a refueling to locally and to the db
     * @param refueling refueling to be added
     */
    public void addRefueling(Refueling refueling) {
        this.refuelings.get(refueling.car).add(refueling);
        this.db.addRefueling(refueling);
    }
    
    /**
     * Get all refuelings of a specified car
     * @param car
     * @return a list of refuelings
     */
    private ArrayList<Refueling> getRefuelingsFromDB(Car car) {
        return db.getRefuelings(car);
    }

    /**
     * Returns all refuelings of a specified car
     * @param car car to get refuelings from
     * @return a list of refuelings
     */
    public ArrayList<Refueling> getRefuelings(Car car) {
        return this.refuelings.get(car);
    }

    /**
     * Returns the average fuel consumption of a specified car
     * @param car car to get the avg. consumption
     * @return average consumption of a car
     */
    public double avgConsumption(Car car) {
        ArrayList<Refueling> refs = this.refuelings.get(car);
        if (refs.isEmpty() || refs.size() == 1) {
            return 0;
        }
        double liters = 0;
        for (int i = 1; i < refs.size(); i++) {
            liters = liters + refs.get(i).volume;
        }

        Collections.sort(refs);

        int kms = refs.get(refs.size() - 1).odometer - refs.get(0).odometer;

        return liters / kms * 100;
    }

    /**
     * Returns the last consumption before a specified refueling
     * @param refueling refueling to get the consumption
     * @return consumption
     */
    public double getConsumption(Refueling refueling) {
        ArrayList<Refueling> refs = this.refuelings.get(refueling.car);
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

    /**
     * Returns the average consumption of a specified month
     * @param car car to get consumption from
     * @param month month
     * @param year year
     * @return average consumption by month
     */
    public double monthAvg(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = refuelingsInMonth(car, month, year);

        double sum = 0;
        int counter = 0;
        for (Refueling r : monthlyrefuelings) {
            double cons = getConsumption(r);
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

    /**
     * Returns all refuelings from a specified month
     * @param car car connected to the refuelings
     * @param month month
     * @param year year
     * @return  list of refuelings by specified month
     */
    private ArrayList<Refueling> refuelingsInMonth(Car car, int month, int year) {
        ArrayList<Refueling> allrefuelings = getRefuelings(car);
        ArrayList<Refueling> valid = new ArrayList<>();

        for (Refueling r : allrefuelings) {
            if (r.getDate().getMonthValue() == month && r.getDate().getYear() == year) {
                valid.add(r);
            }
        }
        return valid;
    }

    /**
     * Returns all driven kilometers from a specified month
     * @param car car connected to the refuelings
     * @param month month
     * @param year year
     * @return driven kilometers by month
     */
    public int kmsInMonth(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = refuelingsInMonth(car, month, year);
        if (monthlyrefuelings.isEmpty()) {
            return 0;
        }
        int km1 = monthlyrefuelings.get(0).getOdometer();
        int km2 = monthlyrefuelings.get(monthlyrefuelings.size() - 1).getOdometer();
        return Math.abs(km2 - km1);
    }

    /**
     * Returns all driven kilometers of a car
     * @param c car
     * @return driven kilometers
     */
    public int totalKms(Car c) {
        if (refuelings.get(c).isEmpty()) {
            return 0;
        }
        Collections.sort(refuelings.get(c));
        int first = refuelings.get(c).get(0).getOdometer();
        int last = refuelings.get(c).get(refuelings.get(c).size() - 1).getOdometer();
        return last - first;
    }

    /**
     * Returns the amount of refuelings in the database per car
     * @param c car
     * @return amount of refuelings
     */
    public int numberOfRefuelings(Car c) {
        return this.refuelings.get(c).size();
    }

    /**
     * Returns the total amount of fuel consumed in liters
     * @param c car
     * @return consumed fuel in liters
     */
    public double totalVolume(Car c) {
        double volume = 0;
        for (Refueling r : this.refuelings.get(c)) {
            volume = volume + r.getVolume();
        }
        return volume;
    }

    /**
     * Returns the total cost of all refuelings
     * @param c car
     * @return total cost
     */
    public double totalCost(Car c) {
        double cost = 0;
        for (Refueling r : this.refuelings.get(c)) {
            cost = cost + r.getCost();
        }
        return cost;
    }

    /**
     * Returns the cost of refuelings by month
     * @param car car
     * @param month month
     * @param year year
     * @return cost per month
     */
    public double costPerMonth(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = refuelingsInMonth(car, month, year);
        double totalcost = 0;
        for (Refueling r : monthlyrefuelings) {
            totalcost = totalcost + r.getCost();
        }
        return totalcost;
    }
}
