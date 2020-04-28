package fuellogger;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.logic.Logic;
import fuellogger.domain.Refueling;
import fuellogger.gui.GUI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
    GUI.main(args);
    }

}
