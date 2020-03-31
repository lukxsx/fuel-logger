
package fuellogger.domain;

public class Car {
    private String name;
    private int fuelcapacity;
    
    public Car(String name, int fuelcapacity) {
        this.name = name;
        this.fuelcapacity = fuelcapacity;
    }

    public String getName() {
        return name;
    }

    public int getFuelcapacity() {
        return fuelcapacity;
    }
    
    @Override
    public String toString() {
        return this.name + " " + this.fuelcapacity + " l";
    }
}
