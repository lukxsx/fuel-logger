package fuellogger.domain;

import java.util.Objects;

public class Refueling implements Comparable<Refueling> {
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

    @Override
    public String toString() {
        return car + " " + odometer + " " + volume + " " + day + " " + month +
                " " + year;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.car);
        hash = 37 * hash + this.odometer;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.volume) ^ (Double.doubleToLongBits(this.volume) >>> 32));
        hash = 37 * hash + this.day;
        hash = 37 * hash + this.month;
        hash = 37 * hash + this.year;
        return hash;
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
        if (this.day != other.day) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if (!Objects.equals(this.car, other.car)) {
            return false;
        }
        return true;
    }

    public int getOdometer() {
        return odometer;
    }

    public Car getCar() {
        return car;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getVolume() {
        return volume;
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
    
    
}
