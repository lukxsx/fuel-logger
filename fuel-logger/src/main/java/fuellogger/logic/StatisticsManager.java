package fuellogger.logic;

import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class calculates and generates statistics to be used in charts and GUI.
 */
public class StatisticsManager {

    private RefuelManager rm;

    public StatisticsManager(RefuelManager rm) {
        this.rm = rm;
    }

    public double avgConsumption(Car car) {
        ArrayList<Refueling> refs = rm.getRefuelings(car);
        if (refs.isEmpty() || refs.size() == 1) {
            return 0;
        }
        double liters = 0;
        for (int i = 1; i < refs.size(); i++) {
            liters = liters + refs.get(i).volume;
        }

        Collections.sort(refs);

        int kms = refs.get(refs.size() - 1).odometer - refs.get(0).odometer;

        return liters / kms * 100;
    }

    /**
     * Returns the last consumption before a specified refueling
     *
     * @param refueling refueling to get the consumption
     * @return consumption
     */
    public double getConsumption(Refueling refueling) {
        ArrayList<Refueling> refs = rm.getRefuelings(refueling.car);
        Collections.sort(refs);
        int index = refs.indexOf(refueling);
        if (index == refs.size() - 1) {
            return 0;
            // can't count consumption from the latest refueling
            // because we don't know about the next refueling yet
        }

        Refueling next = refs.get(index + 1);

        int kms = next.odometer - refueling.odometer;
        return next.volume / kms * 100;
    }

    /**
     * Returns the average consumption of a specified month
     *
     * @param car car to get consumption from
     * @param month month
     * @param year year
     * @return average consumption by month
     */
    public double monthAvg(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = rm.refuelingsInMonth(car, month, year);

        double sum = 0;
        int counter = 0;
        for (Refueling r : monthlyrefuelings) {
            double cons = getConsumption(r);
            if (cons != 0) {
                sum = sum + cons;
                counter++;
            }
        }

        if (sum == 0) {
            return 0;
        }

        return (double) sum / (double) counter;
    }

    /**
     * Returns all driven kilometers from a specified month
     *
     * @param car car connected to the refuelings
     * @param month month
     * @param year year
     * @return driven kilometers by month
     */
    public int kmsInMonth(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = rm.refuelingsInMonth(car, month, year);
        if (monthlyrefuelings.isEmpty()) {
            return 0;
        }
        int km1 = monthlyrefuelings.get(0).getOdometer();
        int km2 = monthlyrefuelings.get(monthlyrefuelings.size() - 1).getOdometer();
        return Math.abs(km2 - km1);
    }

    /**
     * Returns all driven kilometers of a car
     *
     * @param c car
     * @return driven kilometers
     */
    public int totalKms(Car c) {
        ArrayList<Refueling> refuelings = rm.getRefuelings(c);
        if (refuelings.isEmpty()) {
            return 0;
        }
        Collections.sort(refuelings);
        int first = refuelings.get(0).getOdometer();
        int last = refuelings.get(refuelings.size() - 1).getOdometer();
        return last - first;
    }

    /**
     * Returns the total amount of fuel consumed in liters
     *
     * @param c car
     * @return consumed fuel in liters
     */
    public double totalVolume(Car c) {
        double volume = 0;
        for (Refueling r : rm.getRefuelings(c)) {
            volume = volume + r.getVolume();
        }
        return volume;
    }

    /**
     * Returns the total cost of all refuelings
     *
     * @param c car
     * @return total cost
     */
    public double totalCost(Car c) {
        double cost = 0;
        for (Refueling r : rm.getRefuelings(c)) {
            cost = cost + r.getCost();
        }
        return cost;
    }

    /**
     * Returns the cost of refuelings by month
     *
     * @param car car
     * @param month month
     * @param year year
     * @return cost per month
     */
    public double costPerMonth(Car car, int month, int year) {
        ArrayList<Refueling> monthlyrefuelings = rm.refuelingsInMonth(car, month, year);
        double totalcost = 0;
        for (Refueling r : monthlyrefuelings) {
            totalcost = totalcost + r.getCost();
        }
        return totalcost;
    }
}
