
package fuellogger;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import fuellogger.gui.GUI;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Car c = new Car("Volvo", 80);
        Car b = new Car("Audi", 60);
        Refueling r = new Refueling(c, 100000, 75.5, 1, 12, 2020);
        Database d = new Database("asd.db");
        
        System.out.println(d.addCar(c));
        System.out.println(d.getCarId(c));
        //GUI.main(args);
    }
    
}
