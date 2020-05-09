package fuellogger.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * This class reads the config file and creates it if needed
 *
 */
public class ConfigFile {

    private String dbname;
    private Properties p;

    public ConfigFile(String filename) {
        p = new Properties();
        this.dbname = "database.db"; // default name if config file fails 

        File config = new File(filename);

        // load properties if file exists
        if (config.exists()) {
            try {
                p.load(new FileInputStream(config));
                this.dbname = p.getProperty("dbname");
            } catch (FileNotFoundException ex) {
                // can't fail because file exists, if fails it uses the default name
            } catch (IOException ex) {
                // in case it fails, it will use the default value
            }
        } else {

            // if config file doesn't exist, create one with default values
            try {
                PrintWriter writer = new PrintWriter(config);
                writer.println("dbname=database.db");
                writer.close();
            } catch (FileNotFoundException ex) {
                // if fails in any case, use the default value specified 
            }
        }

    }

    public String getDbname() {
        return dbname;
    }

}
