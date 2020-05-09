package fuellogger.logic;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class StatisticsManagerTest {

    RefuelManager rm;
    StatisticsManager sm;

    public StatisticsManagerTest() {
        Database test = new Database("smtest.db");
        rm = new RefuelManager(test);
        sm = new StatisticsManager(rm);
    }

    @Before
    public void setUp() {
        rm.clearDB();
    }

    @After
    public void tearDown() {
        File dbfile = new File("smtest.db");
        dbfile.delete();
    }

    @Test
    public void avgConsumptionReturnsZeroIfOnlyOneRefueling() {
        Car c = new Car("Volvo", 80);
        LocalDate d = LocalDate.of(2020, 1, 1);
        Refueling r = new Refueling(c, 100000, 70, 1.5, d);
        rm.addCar(c);
        rm.addRefueling(r);
        assertEquals(true, sm.avgConsumption(c) == 0);
    }
    
    @Test
    public void avgConsumptionReturnsZeroIfNoRefuelings() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        assertEquals(true, sm.avgConsumption(c) == 0);
    }

    @Test
    public void avgConsumptionCorrectWhenTwoRefuelings() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 28);
        Refueling r1 = new Refueling(c, 373773, 70.13, 1.5, d1);
        LocalDate d2 = LocalDate.of(2020, 3, 13);
        Refueling r2 = new Refueling(c, 374545, 71.61, 1.5, d2);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        assertEquals(true, sm.avgConsumption(c) == 9.275906735751295);
    }

    @Test
    public void avgConsumptionCorrectWhenMultipleRefuelings() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, 1.5, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, 1.5, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, 1.5, d3);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        assertEquals(true, sm.avgConsumption(c) == 9.294426229508197);
    }

    @Test
    public void refuelingConsumptionTest1() {
        // return 0 if can't count consumption

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
        assertEquals(true, sm.getConsumption(r3) == 0);
    }

    @Test
    public void refuelingConsumptionTest2() {
        // tests if method counts the consumption correctly

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
        assertEquals(true, sm.getConsumption(r2) == 9.275906735751295);
    }

    @Test
    public void monthAvgReturnsZeroIfNoRefuelingsInMonth() {
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
        assertEquals(true, sm.monthAvg(c, 1, 2020) == 0);
    }

    @Test
    public void monthAvgTest() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2019, 5, 24);
        Refueling r1 = new Refueling(c, 363619, 69.02, 0, d1);
        LocalDate d2 = LocalDate.of(2019, 5, 9);
        Refueling r2 = new Refueling(c, 362919, 68.05, 0, d2);
        LocalDate d3 = LocalDate.of(2019, 4, 23);
        Refueling r3 = new Refueling(c, 362268, 66.75, 0, d3);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        assertEquals(true, sm.monthAvg(c, 5, 2019) == 9.86);
        assertEquals(true, sm.monthAvg(c, 4, 2019) == 10.453149001536097);
    }

    @Test
    public void monthAvgMultipleYearTest() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 1, 31);
        Refueling r1 = new Refueling(c, 372358, 66.63, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 1, 15);
        Refueling r2 = new Refueling(c, 371707, 70.23, 0, d2);
        LocalDate d3 = LocalDate.of(2019, 12, 18);
        Refueling r3 = new Refueling(c, 371014, 66.53, 0, d3);
        LocalDate d4 = LocalDate.of(2019, 11, 22);
        Refueling r4 = new Refueling(c, 370426, 70.59, 0, d4);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        rm.addRefueling(r4);
        assertEquals(true, sm.monthAvg(c, 1, 2020) == 10.235023041474655);
        assertEquals(true, sm.monthAvg(c, 12, 2019) == 10.134199134199134);
    }

    @Test
    public void monthAvgReturnsZeroIfNoRefuelingsOnYear() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 1, 31);
        Refueling r1 = new Refueling(c, 372358, 66.63, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 1, 15);
        Refueling r2 = new Refueling(c, 371707, 70.23, 0, d2);

        rm.addRefueling(r1);
        rm.addRefueling(r2);
        assertEquals(true, sm.monthAvg(c, 12, 2019) == 0);
    }

    @Test
    public void kmsInMonthReturnsZeroIfNoRefuelingsInMonth() {
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
        assertEquals(true, sm.kmsInMonth(c, 4, 2020) == 0);
    }

    @Test
    public void kmsInMonthTest1() {
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
        assertEquals(true, sm.kmsInMonth(c, 2, 2020) == 753);
    }

    @Test
    public void kmsInMonthTest2() {
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
        assertEquals(true, sm.kmsInMonth(c, 3, 2020) == 0);
    }

    @Test
    public void totalKmsWorksCorrectly() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        LocalDate d1 = LocalDate.of(2020, 1, 31);
        Refueling r1 = new Refueling(c, 372358, 66.63, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 1, 15);
        Refueling r2 = new Refueling(c, 371707, 70.23, 0, d2);
        LocalDate d3 = LocalDate.of(2019, 12, 18);
        Refueling r3 = new Refueling(c, 371014, 66.53, 0, d3);
        LocalDate d4 = LocalDate.of(2019, 11, 22);
        Refueling r4 = new Refueling(c, 370426, 70.59, 0, d4);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        rm.addRefueling(r4);

        assertEquals(true, sm.totalKms(c) == 1932);
    }

    @Test
    public void totalKmsReturnsZeroIfNoRefuelings() {
        Car c = new Car("Volvo", 80);
        rm.addCar(c);
        assertEquals(true, sm.totalKms(c) == 0);
    }

    @Test
    public void totalDoubleReturnsCorrectAmount() {
        Car c1 = new Car("Volvo", 80);
        Car c2 = new Car("BMW", 70);
        rm.addCar(c1);
        rm.addCar(c2);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c1, 373020, 69.92, 0, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c1, 373773, 70.13, 0, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c1, 374545, 71.61, 0, d3);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        LocalDate d4 = LocalDate.of(2020, 1, 15);
        Refueling r4 = new Refueling(c2, 371707, 70.23, 0, d4);
        LocalDate d5 = LocalDate.of(2019, 12, 18);
        Refueling r5 = new Refueling(c2, 371014, 66.53, 0, d5);
        LocalDate d6 = LocalDate.of(2019, 11, 22);
        Refueling r6 = new Refueling(c2, 370426, 70.59, 0, d6);
        rm.addRefueling(r4);
        rm.addRefueling(r5);
        rm.addRefueling(r6);
        assertEquals(true, sm.totalVolume(c1) == 211.66000000000003);
        assertEquals(true, sm.totalVolume(c2) == 207.35);
    }

    @Test
    public void totalCostReturnsCorrectAmount() {
        Car c1 = new Car("Volvo", 80);
        Car c2 = new Car("BMW", 70);
        rm.addCar(c1);
        rm.addCar(c2);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c1, 373020, 69.92, 1.54, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c1, 373773, 70.13, 1.56, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c1, 374545, 71.61, 1.49, d3);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        LocalDate d4 = LocalDate.of(2020, 1, 15);
        Refueling r4 = new Refueling(c2, 371707, 70.23, 1.34, d4);
        LocalDate d5 = LocalDate.of(2019, 12, 18);
        Refueling r5 = new Refueling(c2, 371014, 66.53, 1.28, d5);
        LocalDate d6 = LocalDate.of(2019, 11, 22);
        Refueling r6 = new Refueling(c2, 370426, 70.59, 1.42, d6);
        rm.addRefueling(r4);
        rm.addRefueling(r5);
        rm.addRefueling(r6);
        assertEquals(true, sm.totalCost(c1) == 323.7785);
        assertEquals(true, sm.totalCost(c2) == 279.50440000000003);
    }
    
    @Test
    public void costPerMonthTest() {
         Car c1 = new Car("Volvo", 80);
        Car c2 = new Car("BMW", 70);
        rm.addCar(c1);
        rm.addCar(c2);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c1, 373020, 69.92, 1.54, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c1, 373773, 70.13, 1.56, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c1, 374545, 71.61, 1.49, d3);
        rm.addRefueling(r1);
        rm.addRefueling(r2);
        rm.addRefueling(r3);
        LocalDate d4 = LocalDate.of(2020, 1, 15);
        Refueling r4 = new Refueling(c2, 371707, 70.23, 1.34, d4);
        LocalDate d5 = LocalDate.of(2019, 12, 18);
        Refueling r5 = new Refueling(c2, 371014, 66.53, 1.28, d5);
        LocalDate d6 = LocalDate.of(2019, 11, 22);
        Refueling r6 = new Refueling(c2, 370426, 70.59, 1.42, d6);
        rm.addRefueling(r4);
        rm.addRefueling(r5);
        rm.addRefueling(r6);
        assertEquals(true, sm.costPerMonth(c1, 2, 2020) == 217.0796);
        assertEquals(true, sm.costPerMonth(c2, 12, 2019) == 85.1584);
    }

}
