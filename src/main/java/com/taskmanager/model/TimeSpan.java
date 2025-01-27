package com.taskmanager.model;

import java.sql.Timestamp;

/**
 * A class representing a time span between two timestamps
 */
public class TimeSpan implements clockObserver{
    /**
     * The start time of the time span
     */
    private Timestamp startTime;
    /**
     * The end time of the time span
     */
    private Timestamp endTime;

    /**
     * The system time of the time span
     */
    private Timestamp tsSystemTime;

    /**
     * Initialize a blank TimeSpan and adds the time span  as an observer to the clock
     */
    public TimeSpan(Timestamp tsSystemTime){
        setTsSystemTime(tsSystemTime);
        Clock.addObserver(this);
    }

    /**
     * Gets the start time of the time span
     * @return The start time of the time span
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the time span
     * @return The end time of the time span
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Gets the system time of the time span
     * @return The system time of the time span
     */
    public Timestamp getTsSystemTime() {
        return tsSystemTime;
    }

    /**
     * Sets the system time of the time span
     * @param tsSystemTime The new system time of the time span
     */
    public void setTsSystemTime(Timestamp tsSystemTime) {
        this.tsSystemTime = tsSystemTime;
    }

    /**
     * Starts the time span
     */
    public void startTime() {
        this.startTime = getTsSystemTime();
    }

    /**
     * Ends the time span
     */
    public void endTime() {
        this.endTime = getTsSystemTime();
    }

    /**
     * Updates the system time of the time span
     * @param original the original system time
     * @param variation the variation on the original system time
     */
    @Override
    public void updateTime(Timestamp original, Timestamp variation) {
        Timestamp t = new Timestamp(0);
        t.setTime(original.getTime() + variation.getTime());
        tsSystemTime = t;
    }
}