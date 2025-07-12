package com.btp.layout.model.commands;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private static final int MAX_UNDO_STACK_SIZE = 20;
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private static CommandManager instance;

    private CommandManager() {}

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        
        // Keep stack size within limit
        while (undoStack.size() > MAX_UNDO_STACK_SIZE) {
            undoStack.removeLast();
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.peek();
            boolean undoSuccessful = command.undo();
            if (undoSuccessful) {
                undoStack.pop();
            }
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void clearUndoStack() {
        undoStack.clear();
    }
} 