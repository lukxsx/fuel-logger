package fuellogger.logic;

import fuellogger.logic.Logic;
import fuellogger.domain.*;
import fuellogger.dao.*;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;

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
        Refueling r = new Refueling(c, 100000, 70, 1.5, d);
        l.addCar(c);
        l.addRefueling(c, r);
        assertEquals(true, l.avgConsumption(c) == 0);
    }
    
    @Test
    public void avgConsumptionCorrectWhenTwoRefuelings() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 28);
        Refueling r1 = new Refueling(c, 373773, 70.13, 1.5, d1);
        LocalDate d2 = LocalDate.of(2020, 3, 13);
        Refueling r2 = new Refueling(c, 374545, 71.61, 1.5, d2);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        assertEquals(true, l.avgConsumption(c) == 9.275906735751295);
    }
    
    @Test
    public void avgConsumptionCorrectWhenMultipleRefuelings() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 1.5, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 1.5, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 1.5, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        assertEquals(true, l.avgConsumption(c) == 9.294426229508197);
    }
    
    @Test
    public void refuelingConsumptionTest1() throws SQLException {
        // return 0 if can't count consumption
        
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 0, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        assertEquals(true, l.getConsumption(c, r3) == 0);
    }
    
    @Test
    public void refuelingConsumptionTest2() throws SQLException {
        // tests if method counts the consumption correctly
        
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 0, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        assertEquals(true, l.getConsumption(c, r2) == 9.275906735751295);
    }
    
    @Test
    public void monthAvgReturnsZeroIfNoRefuelingsInMonth() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 0, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        assertEquals(true, l.monthAvg(c, 1, 2020) == 0);
    }
    
    @Test
    public void monthAvgTest() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2019, 5, 24);
        Refueling r1 = new Refueling(c, 363619, 69.02, 0, d1);
        LocalDate d2 = LocalDate.of(2019, 5, 9);
        Refueling r2 = new Refueling(c, 362919, 68.05, 0, d2);
        LocalDate d3 = LocalDate.of(2019, 4, 23);
        Refueling r3 = new Refueling(c, 362268, 66.75, 0, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        assertEquals(true, l.monthAvg(c, 5, 2019) == 9.86);
        assertEquals(true, l.monthAvg(c, 4, 2019) == 10.453149001536097);
    }
    
    @Test
    public void monthAvgMultipleYearTest() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 1, 31);
        Refueling r1 = new Refueling(c, 372358, 66.63, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 1, 15);
        Refueling r2 = new Refueling(c, 371707, 70.23, 0, d2);
        LocalDate d3 = LocalDate.of(2019, 12, 18);
        Refueling r3 = new Refueling(c, 371014, 66.53, 0, d3);
        LocalDate d4 = LocalDate.of(2019, 11, 22);
        Refueling r4 = new Refueling(c, 370426, 70.59, 0, d4);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        l.addRefueling(c, r4);
        assertEquals(true, l.monthAvg(c, 1, 2020) == 10.235023041474655);
        assertEquals(true, l.monthAvg(c, 12, 2019) == 10.134199134199134);
    }
    
    @Test
    public void monthAvgReturnsZeroIfNoRefuelingsOnYear() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 1, 31);
        Refueling r1 = new Refueling(c, 372358, 66.63, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 1, 15);
        Refueling r2 = new Refueling(c, 371707, 70.23, 0, d2);

        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        assertEquals(true, l.monthAvg(c, 12, 2019) == 0);
    }
    
    @Test
    public void gettingRefuelingsFromDBWorks() throws SQLException {
        Car c = new Car("Volvo", 80);
        l.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 0, d3);
        l.addRefueling(c, r1);
        l.addRefueling(c, r2);
        l.addRefueling(c, r3);
        
        Logic l2 = new Logic("logictest.db");
        assertEquals(true, l2.refuelings.get(c).size() == 3);
    }

}