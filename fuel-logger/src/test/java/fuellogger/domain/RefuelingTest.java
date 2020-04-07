/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuellogger.domain;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luks
 */
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
        refs.add(new Refueling(c, 373020, 69.92, 16, 2, 2020));
        refs.add(new Refueling(c, 373773, 70.13, 28, 2, 2020));
        refs.add(new Refueling(c, 374545, 71.61, 13, 3, 2020));
        Collections.sort(refs);
        assertEquals(true, refs.get(0).odometer == 373020);
    }
}
