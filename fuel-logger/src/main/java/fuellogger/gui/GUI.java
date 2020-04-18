package fuellogger.gui;

import fuellogger.domain.Car;
import fuellogger.domain.Logic;
import fuellogger.domain.Refueling;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    private Logic l;
    Scene refuelingsScene;
    Scene carSelectScene;
    Scene graphScene;
    Car currentCar;

    @Override
    public void start(Stage primaryStage) throws Exception {

        l = new Logic("database.db");
        this.currentCar = null;
        // window settings
        primaryStage.setHeight(1024);
        primaryStage.setWidth(800);
        primaryStage.setTitle("Fuel logger");

        
        carSelectScene = carSScene(primaryStage);
        refuelingsScene = null;
        graphScene = null;

        primaryStage.setScene(carSelectScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public Scene carSScene(Stage primaryStage) {
        /*
        *
        *   Car selection view
        *
         */
        VBox carSelectLayout = new VBox();

        Label carSelectInfo = new Label("Select a car to use");
        carSelectLayout.getChildren().add(carSelectInfo);

        // tableview for car selection
        TableView carSelect = new TableView();
        carSelect.setEditable(true);

        TableColumn<String, Car> csNameColumn = new TableColumn("Car");
        csNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //TableColumn csOdometerColumn = new TableColumn("Odometer");
        carSelect.getColumns().addAll(csNameColumn);

        // date for table
        ObservableList<Car> carData = FXCollections.observableArrayList();

        // get cars from the database
        ArrayList<Car> cars = l.cars;
        for (Car c : cars) {
            carData.add(c);
        }
        carSelect.setItems(carData);

        // car selection button
        TableViewSelectionModel<Car> CSselectionModel = carSelect.getSelectionModel();
        CSselectionModel.setSelectionMode(SelectionMode.SINGLE);
        Button csSelectButton = new Button("Select");
        csSelectButton.setOnAction((ActionEvent e) -> {
            ObservableList<Car> selectedCar = CSselectionModel.getSelectedItems();
            if (!(selectedCar.size() == 0)) {
                System.out.println(selectedCar.get(0));
                currentCar = selectedCar.get(0);
                System.out.println(currentCar);
                try {
                    refuelingsScene = refuelScene(primaryStage);
                } catch (SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                primaryStage.setScene(refuelingsScene);
            }
        });

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

        Label csAddInfo = new Label("Add a new car");
        HBox csAddLayout = new HBox();
        csAddLayout.getChildren().addAll(csNameField, csFuelCField, csAddButton);
        csAddLayout.setSpacing(5);

        // car select layout
        carSelectLayout.setSpacing(10);
        carSelectLayout.getChildren().add(carSelect);
        carSelectLayout.getChildren().add(csSelectButton);
        carSelectLayout.getChildren().add(csAddInfo);
        carSelectLayout.getChildren().add(csAddLayout);
        
        Scene css = new Scene(carSelectLayout);
        return css;
    }

    public Scene refuelScene(Stage primaryStage) throws SQLException {
        /*
        *
        *   Refuelings view
        *
        */

        VBox refuelLayout = new VBox();

        HBox refuelTopLayout = new HBox();
        refuelTopLayout.setSpacing(20);

        Label rfCarLabel = new Label();
        rfCarLabel.setText("Selected car: " + currentCar.getName());

        Label rfAvgConsumption = new Label();
        double avg = l.avgConsumption(currentCar);
        DecimalFormat df = new DecimalFormat("#.##");
        rfAvgConsumption.setText("Average consumption: " + df.format(avg) + " l/100km");
        refuelTopLayout.getChildren().add(rfCarLabel);
        refuelTopLayout.getChildren().add(rfAvgConsumption);

        TableView refills = new TableView();
        refills.setEditable(true);

        TableColumn<Integer, Refueling> rfOdometerColumn = new TableColumn("Odometer");
        rfOdometerColumn.setCellValueFactory(new PropertyValueFactory<>("odometer"));
        TableColumn<Double, Refueling> rfVolumeColumn = new TableColumn("Volume");
        rfVolumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        TableColumn<LocalDate, Refueling> rfDateColumn = new TableColumn("Date");
        rfDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        refills.getColumns().addAll(rfOdometerColumn, rfVolumeColumn, rfDateColumn);

        // date for table
        ObservableList<Refueling> refuelingData = FXCollections.observableArrayList();

        // get refuelings from the database
        ArrayList<Refueling> ref = l.refuelings.get(currentCar);
        for (Refueling r : ref) {
            refuelingData.add(r);
        }
        refills.setItems(refuelingData);

        // refueling adding layout
        HBox rfAddLayout = new HBox();
        TextField odField = new TextField();
        odField.setPromptText("Odometer");
        TextField volField = new TextField();
        volField.setPromptText("Volume");
        DatePicker dateField = new DatePicker();
        dateField.setPromptText("Date");
        Button rfAddButton = new Button("Add");
        rfAddLayout.setSpacing(10);
        rfAddLayout.getChildren().addAll(odField, volField, dateField, rfAddButton);
        
        Button rfGraphsButton = new Button("Graphs");
        
        refuelLayout.getChildren().add(refuelTopLayout);
        refuelLayout.getChildren().add(refills);
        refuelLayout.getChildren().add(rfAddLayout);
        refuelLayout.getChildren().add(rfGraphsButton);
        refuelLayout.setSpacing(10);

        rfAddButton.setOnAction((ActionEvent e) -> {
            int odo = Integer.valueOf(odField.getText());
            double vol = Double.valueOf(volField.getText());
            LocalDate date = dateField.getValue();
            Refueling r = new Refueling(currentCar, odo, vol, date);
            try {
                l.addRefueling(currentCar, r);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            refuelingData.add(r);
            odField.clear();
            volField.clear();
            dateField.getEditor().clear();
            rfAvgConsumption.setText("Average consumption: " + df.format(avg) + " l/100km");
            try {
                this.refuelingsScene = refuelScene(primaryStage);
                primaryStage.setScene(refuelingsScene);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        rfGraphsButton.setOnAction((ActionEvent e) -> {
            graphScene = graphsScene();
            primaryStage.setScene(graphScene);
        });
       
        
        Scene rfs = new Scene(refuelLayout);
        return rfs;
    }

    public Scene graphsScene() {
        /*
        *
        *   Graphs view
        *
        */
        
        ChoiceBox cb = new ChoiceBox();
        
        VBox grLayout = new VBox();
        grLayout.getChildren().add(cb);
        
        Scene grs = new Scene(grLayout);
        return grs;
    }

}
