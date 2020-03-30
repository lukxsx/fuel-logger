package fuellogger.gui;

import fuellogger.dao.Database;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Database db = new Database();
        
        // window settings
        primaryStage.setTitle("Fuel logger");

        // car selection view
        VBox carSelectLayout = new VBox();

        Label carSelectInfo = new Label("Select a car to use");
        carSelectLayout.getChildren().add(carSelectInfo);

        TableView carSelect = new TableView();
        TableColumn csNameColumn = new TableColumn("Car");
        TableColumn csOdometerColumn = new TableColumn("Odometer");
        TableColumn csAvgColumn = new TableColumn("Avg. consumption");
        carSelect.getColumns().addAll(csNameColumn, csOdometerColumn, csAvgColumn);
        
        carSelectLayout.getChildren().add(carSelect);
        
        Scene carSelectScene = new Scene(carSelectLayout);

        primaryStage.setScene(carSelectScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
