
package fuellogger.gui;

import fuellogger.dao.Database;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Database db = new Database();
        
        primaryStage.setTitle("Fuel logger");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
