package fuellogger.dao;

import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {

    Database d;
    
    public DatabaseTest() throws SQLException {
        d = new Database("dbtest.db");
    }

    @Before
    public void setUp() throws SQLException {
        d.clear();
    }

    @After
    public void tearDown() {
        File dbfile = new File("dbtest.db");
        dbfile.delete();
    }

    @Test
    public void addingNewCarReturnsTrue() throws SQLException {
        d.clear();
        Car c = new Car("Volvo", 80);
        assertEquals(true, d.addCar(c));
    }
    
    @Test
    public void cantAddSameCarMultipleTimes() throws SQLException {
        Car c = new Car("Volvo", 80);
        d.addCar(c);
        assertEquals(false, d.addCar(c));
    }

    @Test
    public void addingNewCarWorks() throws SQLException {
        Car c = new Car("Volvo", 80);
        d.addCar(c);
        ArrayList<Car> cars = d.getCars();
        assertEquals(1, cars.size());

        assertEquals("Volvo", cars.get(0).getName());
        assertEquals(80, cars.get(0).getFuelcapacity());
    }

    @Test
    public void carByIdWorks() throws SQLException {
        Car c1 = new Car("Volvo", 80);
        Car c2 = new Car("BMW", 70);
        d.addCar(c1);
        d.addCar(c2);
        assertEquals(true, d.getCar(2).equals(c2));
    }

    @Test
    public void carByIdReturnsNullIfNoCar() throws SQLException {
        Car c1 = new Car("Volvo", 80);
        Car c2 = new Car("BMW", 70);
        d.addCar(c1);
        d.addCar(c2);
        assertEquals(true, d.getCar(3) == null);
        assertEquals(true, d.getCar(0) == null);
    }
    
    @Test
    public void getCarIdReturnsZeroIfNoCarInDB() throws SQLException {
        Car c = new Car("Volvo", 80);
        assertEquals(true, d.getCarId(c) == 0);
    }
    
    @Test
    public void cantAddRefuelingIfNoCarInDB() throws SQLException { 
        Car c = new Car("Volvo", 80);
        LocalDate date = LocalDate.of(2019, 5, 24);
        Refueling r = new Refueling(c, 363619, 69.02, 1.5, date);
        assertEquals(false, d.addRefueling(r));
    }
    
    @Test
    public void cantAddSameRefuelingMultipleTimes() throws SQLException {
        Car c = new Car("Volvo", 80);
        LocalDate d1 = LocalDate.of(2019, 5, 24);
        Refueling r1 = new Refueling(c, 363619, 69.02, 1.5, d1);
        d.addCar(c);
        d.addRefueling(r1);
        assertEquals(false, d.addRefueling(r1));
    }
    
    @Test
    public void getRefuelingsReturnsEmptyArrayListIfNoRefuels() throws SQLException {
        assertEquals(true, d.getRefuelings().size() == 0);
    }
    
    @Test
    public void getRefuelingsWorks() throws SQLException {
        Car c = new Car("Volvo", 80);
        LocalDate d1 = LocalDate.of(2019, 5, 24);
        Refueling r1 = new Refueling(c, 363619, 69.02, 1.5, d1);
        LocalDate d2 = LocalDate.of(2019, 5, 9);
        Refueling r2 = new Refueling(c, 362919, 68.05, 1.5, d2);
        LocalDate d3 = LocalDate.of(2019, 4, 23);
        Refueling r3 = new Refueling(c, 362268, 66.75, 1.5, d3);
        d.addCar(c);
        d.addRefueling(r1);
        d.addRefueling(r2);
        d.addRefueling(r3);
        assertEquals(true, d.getRefuelings().size() == 3);
        assertEquals(true, d.getRefuelings().get(1).equals(r2));
    }
}
