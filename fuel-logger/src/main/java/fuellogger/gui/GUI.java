package fuellogger.gui;

import fuellogger.dao.ConfigFile;
import fuellogger.dao.Database;
import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import fuellogger.logic.RefuelManager;
import fuellogger.logic.StatisticsManager;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUI extends Application {

    private RefuelManager rm;
    private StatisticsManager sm;
    private Scene refuelingsScene;
    private Scene carSelectScene;
    private Scene graphScene;
    private Car currentCar;
    private Chart chart;

    private TextFormatter<Double> decimalFormatter() {
        Pattern decimalPattern = Pattern.compile("-?\\d*(\\.\\d{0,3})?");
        return new TextFormatter<>(c -> (decimalPattern.matcher(c.getControlNewText()).matches()) ? c : null);
    }

    private TextFormatter<Double> integerFormatter() {
        Pattern integerPattern = Pattern.compile("[0-9]*");
        return new TextFormatter<>(c -> (integerPattern.matcher(c.getControlNewText()).matches()) ? c : null);
    }

    @Override
    public void start(Stage primaryStage) {
        ConfigFile config = new ConfigFile("fuellogger.conf");
        String dbname = config.getDbname();

        Database d = new Database(dbname);
        if (d.isError()) {
            Alert dbError = new Alert(AlertType.ERROR);
            dbError.setTitle("Database error");
            dbError.setContentText("An error occurred in the SQLite database."
                    + " Check the database file \"" + dbname + "\". If the"
                            + " problem persists, try deleting the file and"
                            + " check the SQLite configuration. You can continue"
                            + " to use the application, but there may be"
                            + " problems with storing data.");
            dbError.show();
        }
        
        this.rm = new RefuelManager(d);
        this.sm = new StatisticsManager(rm);

        this.currentCar = null;

        // window settings
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
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
        carSelectLayout.setPadding(new Insets(10));

        Label carSelectInfo = new Label("Select a car to use");
        carSelectInfo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        carSelectLayout.getChildren().add(carSelectInfo);

        // tableview for car selection
        TableView carSelect = new TableView();
        carSelect.setEditable(true);

        TableColumn<String, Car> csNameColumn = new TableColumn("Car");
        csNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        carSelect.getColumns().addAll(csNameColumn);

        // date for table
        ObservableList<Car> carData = FXCollections.observableArrayList();

        // get cars from the database
        ArrayList<Car> cars = rm.cars;
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
                currentCar = selectedCar.get(0);
                refuelingsScene = refuelScene(primaryStage);

                primaryStage.setScene(refuelingsScene);
            }
        });

        // car adding dialog
        TextField csNameField = new TextField();
        csNameField.setPromptText("Name");
        TextField csFuelCField = new TextField();
        csFuelCField.setPromptText("Fuel capacity");
        Button csAddButton = new Button("Add");

        csFuelCField.setTextFormatter(integerFormatter());

        csAddButton.setOnAction((ActionEvent e) -> {
            if (!(csNameField.getText().isBlank()) && !(csFuelCField.getText().isBlank())) {

                Car c = new Car(csNameField.getText(), Integer.valueOf(csFuelCField.getText()));
                if (rm.addCar(c)) {
                    carData.add(c);
                    csNameField.clear();
                    csFuelCField.clear();
                }
            } else {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Invalid or blank input");
                error.showAndWait();
            }

        });

        Label csAddInfo = new Label("Add a new car");
        csAddInfo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        HBox csAddLayout = new HBox();
        csAddLayout.getChildren().addAll(csNameField, csFuelCField, csAddButton);
        csAddLayout.setSpacing(5);

        // car select layout
        carSelectLayout.setSpacing(10);
        carSelectLayout.getChildren().add(carSelect);
        carSelectLayout.getChildren().add(csSelectButton);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        carSelectLayout.getChildren().add(separator);
        carSelectLayout.getChildren().add(csAddInfo);
        carSelectLayout.getChildren().add(csAddLayout);

        Scene css = new Scene(carSelectLayout);
        return css;
    }

    public Scene refuelScene(Stage primaryStage) {
        /*
        *
        *   Refuelings view
        *
         */

        VBox refuelLayout = new VBox();
        refuelLayout.setPadding(new Insets(10));

        HBox refuelTopLayout = new HBox();
        refuelTopLayout.setSpacing(20);
        Label heading = new Label("Refuelings");
        heading.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        refuelTopLayout.getChildren().add(heading);
        Label rfCarLabel = new Label();
        rfCarLabel.setText("Selected car: " + currentCar.getName());
        rfCarLabel.setFont(Font.font("verdana", FontPosture.REGULAR, 14));
        Button back = new Button("Car selection");

        refuelTopLayout.getChildren().add(rfCarLabel);
        Separator s1 = new Separator(Orientation.VERTICAL);
        refuelTopLayout.getChildren().add(s1);
        refuelTopLayout.getChildren().add(back);
        Button rfGraphsButton = new Button("Charts");
        refuelTopLayout.getChildren().add(rfGraphsButton);

        TableView refills = new TableView();
        refills.setEditable(true);

        TableColumn<Integer, Refueling> rfOdometerColumn = new TableColumn("Odometer");
        rfOdometerColumn.setCellValueFactory(new PropertyValueFactory<>("odometer"));
        TableColumn<Double, Refueling> rfVolumeColumn = new TableColumn("Volume");
        rfVolumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        TableColumn<Double, Refueling> rfPriceColumn = new TableColumn("Price");
        rfPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<LocalDate, Refueling> rfDateColumn = new TableColumn("Date");
        rfDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        refills.getColumns().addAll(rfOdometerColumn, rfVolumeColumn, rfPriceColumn, rfDateColumn);

        // date for table
        ObservableList<Refueling> refuelingData = FXCollections.observableArrayList();

        // get refuelings from the database
        ArrayList<Refueling> ref = rm.getRefuelings(currentCar);
        for (Refueling r : ref) {
            refuelingData.add(r);
        }
        refills.setItems(refuelingData);

        // refueling adding layout        
        HBox rfAddLayout = new HBox();
        TextField odField = new TextField();
        odField.setPromptText("Odometer");
        odField.setTextFormatter(integerFormatter());
        TextField volField = new TextField();
        volField.setPromptText("Volume");
        volField.setTextFormatter(decimalFormatter());
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setTextFormatter(decimalFormatter());
        DatePicker dateField = new DatePicker();
        dateField.setPromptText("Date");
        Button rfAddButton = new Button("Add");
        rfAddLayout.setSpacing(10);
        rfAddLayout.getChildren().addAll(odField, volField, priceField, dateField, rfAddButton);

        refuelLayout.getChildren().add(refuelTopLayout);
        refuelLayout.getChildren().add(refills);
        Separator s2 = new Separator(Orientation.HORIZONTAL);
        refuelLayout.getChildren().add(rfAddLayout);
        refuelLayout.getChildren().add(s2);
        refuelLayout.getChildren().add(carStats(currentCar));
        refuelLayout.setSpacing(10);

        rfAddButton.setOnAction((ActionEvent e) -> {
            if (!odField.getText().isBlank() && !volField.getText().isBlank()
                    && !priceField.getText().isBlank()) {
                if (dateField.getValue() != null) {
                    int odo = Integer.valueOf(odField.getText());
                    double vol = Double.valueOf(volField.getText());
                    double pr = Double.valueOf(priceField.getText());
                    LocalDate date = dateField.getValue();

                    Refueling r = new Refueling(currentCar, odo, vol, pr, date);
                    rm.addRefueling(r);
                    refuelingData.add(r);
                } else {
                    Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Invalid date");
                    error.showAndWait();
                }

            } else {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Invalid or blank input");
                error.showAndWait();
            }

            this.refuelingsScene = refuelScene(primaryStage);
            primaryStage.setScene(refuelingsScene);

        });

        rfGraphsButton.setOnAction((ActionEvent e) -> {
            graphScene = graphsScene(primaryStage);
            primaryStage.setScene(graphScene);
        });

        back.setOnAction((ActionEvent e) -> {
            currentCar = null;
            primaryStage.setScene(carSelectScene);
        });

        Scene rfs = new Scene(refuelLayout);
        return rfs;
    }

    public Scene graphsScene(Stage primaryStage) {
        /*
        *
        *   Graphs view
        *
         */

        HBox graphsTop = new HBox();
        graphsTop.setSpacing(15);
        graphsTop.setPadding(new Insets(5));

        ChoiceBox cb = new ChoiceBox();
        Label cbLabel = new Label("Select graph:");
        cb.getItems().add("Consumption by month");
        cb.getItems().add("Cost by month");
        cb.getItems().add("Kilometres by month");
        cb.setValue("Consumption by month");

        Button back = new Button("Back");

        LocalDate today = LocalDate.now();
        ChoiceBox yearselect = new ChoiceBox();
        for (int i = 0; i < 10; i++) {
            yearselect.getItems().add(today.getYear() - i);
        }
        yearselect.setValue(today.getYear());
        Button yearSelectButton = new Button("Select");

        VBox grLayout = new VBox();
        grLayout.setPadding(new Insets(10));
        graphsTop.getChildren().addAll(back, cbLabel, cb, yearselect, yearSelectButton);

        back.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(refuelingsScene);
        });

        grLayout.getChildren().add(graphsTop);
        Separator s2 = new Separator(Orientation.HORIZONTAL);
        grLayout.getChildren().add(s2);

        VBox graphPane = new VBox();

        chart = monthChart((int) yearselect.getValue());
        graphPane.getChildren().add(chart);
        grLayout.getChildren().add(graphPane);

        VBox grBottomLayout = carStats(currentCar);
        Separator s = new Separator(Orientation.HORIZONTAL);
        grLayout.getChildren().add(s);
        grLayout.getChildren().add(grBottomLayout);

        yearSelectButton.setOnAction((ActionEvent e) -> {
            String value = (String) cb.getValue();
            switch (value) {
                case "Consumption by month":
                    chart = monthChart((int) yearselect.getValue());
                    break;
                case "Cost by month":
                    chart = costChart((int) yearselect.getValue());
                    break;
                case "Kilometres by month":
                    chart = kmsChart((int) yearselect.getValue());
                    break;
            }

            graphPane.getChildren().clear();
            graphPane.getChildren().add(chart);
        });

        Scene grs = new Scene(grLayout);
        return grs;
    }

    private Chart monthChart(int year) {

        CategoryAxis consXAxis = new CategoryAxis();
        NumberAxis consYAxis = new NumberAxis();
        consXAxis.setLabel("Month");
        consYAxis.setLabel("l/100 km");

        BarChart<String, Number> consChart = new BarChart<>(consXAxis, consYAxis);
        consChart.setTitle("Fuel consumption by month");

        XYChart.Series consData = new XYChart.Series();

        for (int i = 1; i <= 12; i++) {
            consData.getData().add(new XYChart.Data(getMonthName(i), sm.monthAvg(currentCar, i, year)));
        }

        consChart.getData().add(consData);
        consChart.setLegendVisible(false);
        return consChart;
    }

    private Chart costChart(int year) {
        CategoryAxis costXAxis = new CategoryAxis();
        NumberAxis costYAxis = new NumberAxis();
        costXAxis.setLabel("Month");
        costYAxis.setLabel("Cost");

        BarChart<String, Number> costChart = new BarChart<>(costXAxis, costYAxis);
        costChart.setTitle("Cost by month");

        XYChart.Series costData = new XYChart.Series();

        for (int i = 1; i <= 12; i++) {
            costData.getData().add(new XYChart.Data(getMonthName(i), sm.costPerMonth(currentCar, i, year)));
        }

        costChart.getData().add(costData);
        costChart.setLegendVisible(false);
        return costChart;
    }

    private Chart kmsChart(int year) {
        CategoryAxis kmsXAxis = new CategoryAxis();
        NumberAxis kmsYAxis = new NumberAxis();
        kmsXAxis.setLabel("Month");
        kmsYAxis.setLabel("Driven kilometers");

        BarChart<String, Number> kmsChart = new BarChart<>(kmsXAxis, kmsYAxis);
        kmsChart.setTitle("Driven kilometers");

        XYChart.Series kmsData = new XYChart.Series();

        for (int i = 1; i <= 12; i++) {
            kmsData.getData().add(new XYChart.Data(getMonthName(i), sm.kmsInMonth(currentCar, i, year)));
        }

        kmsChart.getData().add(kmsData);
        kmsChart.setLegendVisible(false);
        return kmsChart;
    }

    private VBox carStats(Car car) {
        VBox statsLayout = new VBox();
        Label infoLabel = new Label("Statistics");
        infoLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));

        Label carNameLabel = new Label();
        carNameLabel.setText("Car: " + car.getName());

        Label carTankLabel = new Label();
        carTankLabel.setText("Fuel tank capacity: " + car.getFuelcapacity() + " liters");

        Label avgConsLabel = new Label();
        double avg = sm.avgConsumption(car);
        DecimalFormat df = new DecimalFormat("#.##");
        avgConsLabel.setText("Average consumption: " + df.format(avg) + " l/100km");

        Label totalVolumeLabel = new Label();
        totalVolumeLabel.setText("Fuel consumed: " + df.format(sm.totalVolume(car)) + " liters");

        Label totalCostLabel = new Label();
        totalCostLabel.setText("Total fuel cost: " + df.format(sm.totalCost(car)) + " €");

        Label totalKmsLabel = new Label();
        totalKmsLabel.setText("Total driven kilometers: " + sm.totalKms(car) + " km");

        Label numOfRefuelingsLabel = new Label();
        numOfRefuelingsLabel.setText("Refuelings: " + rm.numberOfRefuelings(car));

        statsLayout.setSpacing(5);
        statsLayout.getChildren().addAll(infoLabel, carNameLabel, carTankLabel,
                avgConsLabel, totalVolumeLabel, totalCostLabel, totalKmsLabel,
                numOfRefuelingsLabel);
        return statsLayout;
    }

    public String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

}
