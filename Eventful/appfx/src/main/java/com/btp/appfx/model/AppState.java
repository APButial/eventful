package com.btp.appfx.model;

import javafx.scene.Scene;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppState {
    protected List<BaseEvent> currEvents;
    protected BaseEvent currSelectedEvent;
    protected Scene currentScene;
    protected Scene prevScene;
    protected LocalDateTime currDateTime;
}