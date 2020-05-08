package fuellogger.logic;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.io.File;
import java.time.LocalDate;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class RefuelManagerTest {

    RefuelManager rm;
    Database db;

    public RefuelManagerTest() {
        db = new Database("rmtest.db");
        rm = new RefuelManager(db);
    }

    @Before
    public void setUp() {
        rm.clearDB();
    }

    @After
    public void tearDown() {
        File dbfile = new File("rmtest.db");
        dbfile.delete();
    }

    @Test
    public void addingCarAddsToList() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        assertEquals(true, rm.cars.contains(c));

        Car c2 = new Car("Toyota", 60);
        rm.addCar(c2);
        assertEquals(true, rm.cars.get(1).equals(c2));
    }

    @Test
    public void cantAddSameCarMultipleTimes() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        rm.addCar(c);
        assertEquals(true, rm.cars.size() == 1);
    }
    
    @Test
    public void gettingRefuelingsFromDBWorks() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 0, d3);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        
        RefuelManager rm2 = new RefuelManager(db);
        assertEquals(true, rm2.refuelings.get(c).size() == 3);
    }
    
    @Test
    public void refuelingAmountIsCorrect() {
        Car c1 = new Car("Volvo", 80);
        Car c2 = new Car ("BMW", 70);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c1, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c2, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c1, 374545, 71.61, 0, d3);
        rm.addCar(c1);
        rm.addCar(c2);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        assertEquals(true, rm.numberOfRefuelings(c1) == 2);
        assertEquals(true, rm.numberOfRefuelings(c2) == 1);
    }
}
