package fuellogger;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.logic.Logic;
import fuellogger.domain.Refueling;
import fuellogger.gui.GUI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
        /*
        // just some test code 
        Car c = new Car("Volvo", 80);
        Database d = new Database("asd.db");
        Logic l = new Logic("asd.db");
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2019, 5, 24);
        Refueling r1 = new Refueling(c, 363619, 69.02, d1);
        LocalDate d2 = LocalDate.of(2019, 5, 9);
        Refueling r2 = new Refueling(c, 362919, 68.05, d2);
        LocalDate d3 = LocalDate.of(2019, 4, 23);
        Refueling r3 = new Refueling(c, 362268, 66.75, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        System.out.println(l.monthAvg(c, 4));
         */

        GUI.main(args);

    }

}
