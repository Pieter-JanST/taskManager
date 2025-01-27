package com.taskmanager.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for the clock
 */
public class Clock {
        private static final Timestamp originalSystemTime = new Timestamp(System.currentTimeMillis());

        private static final Timestamp variationSystemTime = new Timestamp(0);

        private static final Set<clockObserver> observers = new HashSet<>();

    /**
     * Set the system time to the given time
     * @param t the time to set the system time to
     */
    public static void setSystemTime(Timestamp t) {
            if (getSystemTime().after(t)){
                throw new IllegalArgumentException("systemTime cant be set in the past");
            }
            variationSystemTime.setTime(t.getTime()-originalSystemTime.getTime());
            notifyObservers();
        }

    /**
     * Get the current system time
      * @return the current system time
     */
    public static Timestamp getSystemTime(){
            Timestamp t = new Timestamp(0);
            t.setTime(originalSystemTime.getTime() + variationSystemTime.getTime());
            return t;
        }

    /**
     * Get the variation on the original system time
     * @return the variation on the original system time
     */
    public static Timestamp getVariationSystemTime() {
            return variationSystemTime;
        }

    /**
     * Get the original system time
     * @return the original system time
     */
    public static Timestamp getOriginalSystemTime() {
            return originalSystemTime;
        }

    /**
     * Add an observer to the clock
     * @param observer the observer to add
     */
    public static void addObserver(clockObserver observer) {
            observers.add(observer);
        }

    /**
     * Remove an observer from the clock
     * @param observer the observer to remove
     */
    public void removeObserver(clockObserver observer) {
            observers.remove(observer);
        }

    /**
     * Notify all observers of the clock
     */
    public static void notifyObservers() {
            Timestamp original = getOriginalSystemTime();
            Timestamp variation = getVariationSystemTime();
            for (clockObserver observer : observers) {
                observer.updateTime(original, variation);
            }
        }
}
