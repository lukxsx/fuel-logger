package fuellogger.domain;

public class Refueling {
    public Car car;
    public int odometer;
    public double volume;
    public int day;
    public int month;
    public int year;

    public Refueling(Car car, int odometer, double volume, int day, int month, int year) {
        this.car = car;
        this.odometer = odometer;
        this.volume = volume;
        this.day = day;
        this.month = month;
        this.year = year;
    }
    
    
}
