package fuellogger.domain;

import fuellogger.domain.*;
import fuellogger.dao.*;

import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*; 
import java.time.LocalDate;
import java.time.Month;

public class LogicTest {
    
    Logic l;
    
    public LogicTest() throws SQLException {
        l = new Logic("logictest.db");
        l.clearDB();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() throws SQLException {
        l.clearDB();
    }
    
    @After
    public void tearDown() throws SQLException {
        File dbfile = new File("logictest.db");
        dbfile.delete();
    }
    
    @Test
    public void addingCarAddsToList() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        assertEquals(true, l.cars.contains(c));
        
        Car c2 = new Car("Toyota", 60);
        l.addCar(c2);
        assertEquals(true, l.cars.get(1).equals(c2));
    }
    
    @Test
    public void avgConsumptionReturnsZeroIfOnlyOneRefueling() throws SQLException {
        Car c = new Car("Volvo", 80);
        LocalDate d = LocalDate.of(2020, 1, 1);
        Refueling r = new Refueling(c, 100000, 70, d);
        l.addCar(c);
        l.addRefueling(c, r);
        assertEquals(true, l.avgConsumption(c) == 0);
    }
    
    @Test
    public void avgConsumptionCorrectWhenTwoRefuelings() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 28);
        Refueling r1 = new Refueling(c, 373773, 70.13, d1);
        LocalDate d2 = LocalDate.of(2020, 3, 13);
        Refueling r2 = new Refueling(c, 374545, 71.61, d2);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        assertEquals(true, l.avgConsumption(c) == 9.275906735751295);
    }
    
    @Test
    public void avgConsumptionCorrectWhenMultipleRefuelings() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        assertEquals(true, l.avgConsumption(c) == 9.294426229508197);
    }

}