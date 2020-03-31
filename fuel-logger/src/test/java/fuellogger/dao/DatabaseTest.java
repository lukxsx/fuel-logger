package fuellogger.dao;

import fuellogger.domain.Car;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {

    Database d;

    @Before
    public void setUp() throws SQLException {
        d = new Database("test.db");
    }

    @Test
    public void addingNewCar() throws SQLException {
        Car c = new Car("Volvo", 80);
        assertEquals(true, d.addCar(c));

        ArrayList<Car> cars = d.getCars();
        assertEquals(1, cars.size());

        assertEquals("Volvo", cars.get(0).getName());
        assertEquals(80, cars.get(0).getFuelcapacity());
    }
}
