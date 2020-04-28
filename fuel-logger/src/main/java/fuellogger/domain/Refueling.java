package fuellogger.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Refueling implements Comparable<Refueling> {

    public Car car;
    public int odometer;
    public double volume;
    public LocalDate date;
    public double price;

    public Refueling(Car car, int odometer, double volume, double price, LocalDate date) {
        this.car = car;
        this.odometer = odometer;
        this.volume = volume;
        this.date = date;
        this.price = price;
    }

    @Override
    public String toString() {
        return car + " " + odometer + " " + volume + " " + date;
    }

    public int getOdometer() {
        return odometer;
    }

    public Car getCar() {
        return car;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getVolume() {
        return volume;
    }
    
    public double getCost() {
        return this.volume * this.price;
    }

    @Override
    public int compareTo(Refueling t) {
        if (t.odometer < this.odometer) {
            return 1;
        }
        if (t.odometer > this.odometer) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        final Refueling other = (Refueling) obj;
        if (this.odometer != other.odometer) {
            return false;
        }
        if (!Objects.equals(this.car, other.car)) {
            return false;
        }
        return true;
    }

}
