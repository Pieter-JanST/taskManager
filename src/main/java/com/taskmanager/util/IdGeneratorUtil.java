package com.taskmanager.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGeneratorUtil {
    private static final AtomicLong atomicCounter = new AtomicLong();
    public static long createId() {
        return atomicCounter.getAndIncrement()+1;
    }
}