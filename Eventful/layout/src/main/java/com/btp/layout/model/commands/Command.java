package com.btp.layout.model.commands;

public interface Command {
    void execute();
    boolean undo();
} 