package com.btp.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppState {
    protected User currUser;
    protected LocalDateTime currDateTime;
}