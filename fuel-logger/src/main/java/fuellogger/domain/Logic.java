
package fuellogger.domain;

import fuellogger.dao.Database;
import java.sql.SQLException;
import java.util.ArrayList;

public class Logic {
    private Database db;
    public ArrayList<Car> cars;
    public ArrayList<Refueling> refuels;
    
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
}