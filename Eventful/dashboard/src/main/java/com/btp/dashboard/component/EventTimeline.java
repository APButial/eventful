package com.btp.dashboard.component;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventTimeline {
    private HBox component;
    private CalendarView calendarView;
    private Calendar events;

    public EventTimeline() throws Exception {
        component = new HBox();
        component.setPadding(new Insets(10,0,0,0));
        component.setStyle("-fx-background-color: #FFFFFF;");

        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowDeveloperConsole(false);
        calendarView.setShowPageToolBarControls(false);

        calendarView.setPrefWidth(800);
        calendarView.setPrefHeight(480);

        events = new Calendar("Events");

        CalendarSource calendarSource = new CalendarSource("My Calendars");
        calendarSource.getCalendars().addAll(events);

        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        events.setStyle(Calendar.Style.STYLE1);

        component.getChildren().add(calendarView);
    }

    public HBox getComponent() {
        return component;
    }

    public Calendar getEvents() {
        return events;
    }
}
