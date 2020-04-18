
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
        Logic l = new Logic("database.db");
        
        Car c = l.cars.get(0);
        
        System.out.println(l.monthAvg(c, 3));
        
       
        

        //GUI.main(args);

    }
    
}
