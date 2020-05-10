package fuellogger.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class reads the configuration file and creates it if needed
 *
 */
public class ConfigFile {

    private String dbname;
    private Properties p;
    private String filename;

    public ConfigFile(String filename) {
        this.filename = filename;
        p = new Properties();
        this.dbname = "database.db"; // default name if config file fails 

        // load properties if file exists, create new file if needed
        try {
            p.load(new FileInputStream(filename));
            this.dbname = p.getProperty("dbname");
        } catch (FileNotFoundException ex) {
            newConfigFile(); // if file not found, create a new config file
        } catch (IOException ex) {
            newConfigFile(); // if file not found, create a new config file
        }

    }

    /**
     * Returns the database name to be used
     *
     * @return name of the database file
     */
    public String getDbname() {
        return this.dbname;
    }

    /**
     * Creates a new configuration file with default value
     */
    public void newConfigFile() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(filename);
            writer.println("dbname=database.db");
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
