
package fuellogger.dao;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConfigFileTest {
    
    public ConfigFileTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
        File testconf = new File("test.conf");
        testconf.delete();
    }
    
    @After
    public void tearDown() {
        File testconf = new File("test.conf");
        testconf.delete();
    }
    
    @Test
    public void newFileIsCreatedIfFileMissing() {
        File test = new File("test.conf");
        assertEquals(false, test.exists());
        ConfigFile c = new ConfigFile("test.conf");
        assertEquals(true, test.exists());
    }
    
    @Test
    public void correctDefaultValue() {
        ConfigFile c = new ConfigFile("test.conf");
        ConfigFile c2 = new ConfigFile("test.conf");
        assertEquals(true, c2.getDbname().equals("database.db"));
    }
    
    
}
