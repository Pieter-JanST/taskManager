package com.taskmanager.model;

import java.sql.Timestamp;

/**
 * Interface for the clock observer
 */
public interface clockObserver {
    /**
     * Update the time of the observer
     * @param original the original system time
     * @param variation the variation on the original system time
     */
    void updateTime(Timestamp original, Timestamp variation);
}
