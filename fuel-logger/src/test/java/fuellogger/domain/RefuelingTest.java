package fuellogger.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RefuelingTest {
    
    public RefuelingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void comparationWorks() {
        ArrayList<Refueling> refs = new ArrayList<>();
        Car c = new Car("Volvo", 80);
        LocalDate d1 = LocalDate.of(2020, 2, 16);
        Refueling r1 = new Refueling(c, 373020, 69.92, d1);
        LocalDate d2 = LocalDate.of(2020, 2, 28);
        Refueling r2 = new Refueling(c, 373773, 70.13, d2);
        LocalDate d3 = LocalDate.of(2020, 3, 13);
        Refueling r3 = new Refueling(c, 374545, 71.61, d3);
        refs.add(r1);
        refs.add(r2);
        refs.add(r3);
        Collections.sort(refs);
        assertEquals(true, refs.get(0).odometer == 373020);
    }
}
