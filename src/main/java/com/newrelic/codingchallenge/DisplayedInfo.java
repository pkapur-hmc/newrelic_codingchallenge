package com.newrelic.codingchallenge;

import java.util.concurrent.atomic.AtomicInteger;

public class DisplayedInfo {
    private AtomicInteger totalUnique; // Thread-safe total number of unique numbers
    private AtomicInteger latestUnique; // Thread-safe number of unique numbers in last 10s
    private AtomicInteger latestDuplicates; // Thread-safe number of duplicates in last 10s

    public DisplayedInfo()
    {
        // All member variables initialized to 0
        totalUnique = new AtomicInteger(0);
        latestUnique = new AtomicInteger(0);
        latestDuplicates = new AtomicInteger(0);
    }

    /*
    Method that increments the number of unique numbers
     */
    public void incrementUnique()
    {
        totalUnique.incrementAndGet();
        latestUnique.incrementAndGet();
    }

    /*
    Method that increments the number of duplicates
     */
    public void incrementDuplicates()
    {
        latestDuplicates.incrementAndGet();
    }

    /*
    Method that is called every 10s that displays necessary information and resets 10s interval values to 0
     */
    public void displayAndResetInfo()
    {
        System.out.println("Unique numbers received: " + latestUnique.intValue());
        System.out.println("Duplicate numbers received: " + latestDuplicates.intValue());
        System.out.println("Total unique numbers received: " + totalUnique.intValue());
        System.out.println("------------------------------------------------------");
        latestUnique.set(0);
        latestDuplicates.set(0);
    }


}
