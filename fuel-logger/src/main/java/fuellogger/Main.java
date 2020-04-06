
package fuellogger;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Logic;
import fuellogger.domain.Refueling;
import fuellogger.gui.GUI;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
        
        // just some test code 
        Logic l = new Logic();
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        Refueling r1 = new Refueling(c, 373773, 70.13, 28, 2, 2020);
        Refueling r2 = new Refueling(c, 374545, 71.61, 13, 3, 2020);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        System.out.println(l.avgConsumption(c));
        
        
        /*
        ArrayList<Refueling> refs = d.getRefuelings(c);
        for (Refueling re: refs) {
            System.out.println(re);
        }
        */

        GUI.main(args);

    }
    
}
