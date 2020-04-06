
package fuellogger.domain;

import fuellogger.dao.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Logic {
    private Database db;
    public ArrayList<Car> cars;
    
    public Logic() throws SQLException {
        this.db = new Database("database.db");
        this.cars = db.getCars();
    }
    
    public void addCar(Car car) {
        if (!this.cars.contains(car)) {
            this.cars.add(car);
            this.db.addCar(car);
        }
    }
    
    public void addRefueling(Car car, Refueling refueling) throws SQLException {
        this.db.addRefill(car, refueling);
        /*if (!this.refuelings.contains(refueling)) {
            this.refuelings.add(refueling);
            
        }
        */
    }
    
    public ArrayList<Refueling> getRefuelings(Car car) throws SQLException {
        return db.getRefuelings(car);
    }
    
    public double avgConsumption(Car car) throws SQLException {
        ArrayList<Refueling> refuelings = db.getRefuelings(car);
        if (refuelings.size() == 0 || refuelings.size() == 1) return 0;
        double litres = 0;
        for (int i = 1; i < refuelings.size(); i++) {
            litres = litres + refuelings.get(i).volume;
        }
        
        
        Collections.sort(refuelings);
        for (int i = 0; i < refuelings.size(); i++) {
            System.out.println(refuelings.get(i));
        }
        
        int kms = refuelings.get(refuelings.size() - 1).odometer - refuelings.get(0).odometer;
        
        
        return litres / kms * 100;
    }
}