package fuellogger.gui;

import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Logic;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
    
    private Logic l;

    @Override
    public void start(Stage primaryStage) throws Exception {
        l = new Logic();
        
        // window settings
        primaryStage.setTitle("Fuel logger");

        // car selection view
        VBox carSelectLayout = new VBox();

        Label carSelectInfo = new Label("Select a car to use");
        carSelectLayout.getChildren().add(carSelectInfo);

        // tableview for car selection
        TableView carSelect = new TableView();
        carSelect.setEditable(true);
        
        TableColumn csNameColumn = new TableColumn("Car");
        csNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn csOdometerColumn = new TableColumn("Odometer");
        TableColumn csAvgColumn = new TableColumn("Avg. consumption");
        carSelect.getColumns().addAll(csNameColumn, csOdometerColumn, csAvgColumn);

        // date for table
        ObservableList<Car> carData = FXCollections.observableArrayList();
        
        // get cars from the database
        ArrayList<Car> cars = l.cars;
        for (Car c: cars) {
            carData.add(c);
        }
        carSelect.setItems(carData);

        // car adding dialog
        TextField csNameField = new TextField();
        csNameField.setPromptText("Name");
        TextField csFuelCField = new TextField();
        csFuelCField.setPromptText("Fuel capacity");
        Button csAddButton = new Button("Add");

        csAddButton.setOnAction((ActionEvent e) -> {
            Car c = new Car(csNameField.getText(), Integer.valueOf(csFuelCField.getText()));
            System.out.println(c);
            l.addCar(c);
            carData.add(c);
            csNameField.clear();
            csFuelCField.clear();
        });
        
        HBox csAddLayout = new HBox();
        csAddLayout.getChildren().addAll(csNameField, csFuelCField, csAddButton);
        csAddLayout.setSpacing(5);
        
        // car select layout
        carSelectLayout.getChildren().add(carSelect);
        carSelectLayout.getChildren().add(csAddLayout);

        Scene carSelectScene = new Scene(carSelectLayout);

        primaryStage.setScene(carSelectScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
