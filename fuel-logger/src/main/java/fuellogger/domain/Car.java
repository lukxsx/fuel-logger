
package fuellogger.domain;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.name);
        hash = 43 * hash + this.fuelcapacity;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (this.fuelcapacity != other.fuelcapacity) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public int getFuelcapacity() {
        return fuelcapacity;
    }
}
