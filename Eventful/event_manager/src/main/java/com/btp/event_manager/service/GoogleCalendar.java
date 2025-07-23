package com.btp.event_manager.service;

import com.btp.appfx.service.AppDataPath;
import com.btp.appfx.service.AppService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import javafx.scene.control.Alert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/* class to demonstrate use of Calendar events list API */
public class GoogleCalendar {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Eventful";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = AppDataPath.loadPath() + "/tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";

    private static Calendar service;
    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static boolean validMailArea(String mail, AppService appService) {
        String processedMail = mail.strip().toLowerCase();
        if(!processedMail.contains(";")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event Detail");
            alert.setHeaderText("Invalid Email Address Syntax");
            alert.setContentText("Each email address must be appended by a semicolon (;).");
            alert.showAndWait();
            return false;
        }

        appService.setGuests(List.of(processedMail.split(";")));
        return true;
    }

    public static void sendEventInvitation(AppService appService) throws Exception {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service =
                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        com.btp.event_manager.model.Event e = (com.btp.event_manager.model.Event) appService.getSelectedEvent();
        String description = getString(e);

        Event event = new Event()
                .setSummary("An Eventful Invitation: " + e.getEventName())
                .setDescription(description);

        LocalDateTime localStartDateTime = e.getStartDate().atTime(e.getStartTime());
        ZonedDateTime zonedStartDateTime = localStartDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedStartDateTime.toInstant());
        DateTime startDateTime = new DateTime(date);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Manila");
        event.setStart(start);

        LocalDateTime localEndDateTime = e.getEndDate().atTime(e.getEndTime());
        ZonedDateTime zonedEndDateTime = localEndDateTime.atZone(ZoneId.systemDefault());
        date = Date.from((zonedEndDateTime.toInstant()));
        DateTime endDateTime = new DateTime(date);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Manila");
        event.setEnd(end);

        ZonedDateTime utc = localEndDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        String until = utc.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;UNTIL=" + until};
        event.setRecurrence(Arrays.asList(recurrence));

        List<String> guests = e.getGuests();
        EventAttendee[] attendees = new EventAttendee[guests.size()];
        for (int i=0; i<guests.size(); i++) {
            attendees[i] = new EventAttendee().setEmail(guests.get(i));
        }
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).setSendUpdates("all").execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());

        appService.setEventID(event.getId());
    }

    private static String getString(com.btp.event_manager.model.Event e) throws Exception {
        String description;
        if (e.getDescription() != null || !e.getDescription().isEmpty()) {
            description = String.format("You received an invitation to attend \"%s\" from %s to %s. The organizer described this event as: \"%s\". Have an eventful day!",
                e.getEventName(),
                e.getStartDate().toString(),
                e.getEndDate().toString(),
                e.getDescription());
        } else {
            description = String.format("You received an invitation to attend \"%s\" from %s to %s. Have an eventful day!",
                    e.getEventName(),
                    e.getStartDate().toString(),
                    e.getEndDate().toString());
        }

        return description;
    }

    public static void printAttendeesResponse(String calendarId, String eventId) throws IOException {
        Event event = service.events().get(calendarId, eventId).execute();
        if (event.getAttendees() != null) {
            for (EventAttendee attendee : event.getAttendees()) {
                String email = attendee.getEmail();
                String responseStatus = attendee.getResponseStatus();
                System.out.println("Attendee: " + email + " Response: " + responseStatus);
            }
        }
    }
}