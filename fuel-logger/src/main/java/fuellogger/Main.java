
package fuellogger;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import fuellogger.gui.GUI;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
        
        // just some test code 
        /*
        Car c = new Car("Volvo", 80);
        Car b = new Car("Audi", 60);
        Refueling r = new Refueling(c, 100000, 75.5, 1, 12, 2020);
        Database d = new Database("asd.db");
        d.addRefill(c, r);
        
        ArrayList<Refueling> refs = d.getRefuelings(c);
        for (Refueling re: refs) {
            System.out.println(re);
        }
*/
        GUI.main(args);
    }
    
}
