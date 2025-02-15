package com.btp.dashboard.service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DateTimeService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final DateTimeListener listener;

    public DateTimeService(DateTimeListener listener) {
        this.listener = listener;
        startUpdateDateTime();
    }

    private void startUpdateDateTime() {
        scheduler.scheduleAtFixedRate(() -> {
            LocalDateTime currentDateTime = LocalDateTime.now();
            listener.updateDateTime(currentDateTime);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stopUpdateDateTime() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)){
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}