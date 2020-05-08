package fuellogger.logic;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class manages the Car and Refueling classes.
 */
public class RefuelManager {

    private Database db;

    /**
     * A list of all locally stored cars
     */
    public ArrayList<Car> cars;

    /**
     * Stores refuelings locally
     */
    public HashMap<Car, ArrayList<Refueling>> refuelings;

    public RefuelManager(Database d) {
        this.db = d;
        this.cars = db.getCars();
        this.refuelings = new HashMap<>();
        for (Car c : this.cars) {
            this.refuelings.put(c, getRefuelingsFromDB(c));
        }
    }
    
    /**
     * Adds a car locally and to the database
     *
     * @param car car to be added
     */
    public boolean addCar(Car car) {
        if (!this.cars.contains(car)) {
            if (this.db.addCar(car)) {
                this.cars.add(car);
                if (!this.refuelings.containsKey(car)) {
                    this.refuelings.put(car, new ArrayList<>());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the database (used for tests)
     */
    public void clearDB() {
        db.clear();
    }

    /**
     * Adds a refueling to locally and to the db
     *
     * @param refueling refueling to be added
     */
    public void addRefueling(Refueling refueling) {
        this.refuelings.get(refueling.car).add(refueling);
        this.db.addRefueling(refueling);
    }

    /**
     * Get all refuelings of a specified car
     *
     * @param car
     * @return a list of refuelings
     */
    private ArrayList<Refueling> getRefuelingsFromDB(Car car) {
        return db.getRefuelings(car);
    }

    /**
     * Returns all refuelings of a specified car
     *
     * @param car car to get refuelings from
     * @return a list of refuelings
     */
    public ArrayList<Refueling> getRefuelings(Car car) {
        return this.refuelings.get(car);
    }
    
    /**
     * Returns all refuelings from a specified month
     *
     * @param car car connected to the refuelings
     * @param month month
     * @param year year
     * @return list of refuelings by specified month
     */
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
    
      /**
     * Returns the amount of refuelings in the database per car
     *
     * @param c car
     * @return amount of refuelings
     */
    public int numberOfRefuelings(Car c) {
        return getRefuelings(c).size();
    }
}
