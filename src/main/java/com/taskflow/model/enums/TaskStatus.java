package com.taskflow.model.enums;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    // Bonus: human-readable display label
    public String getLabel() {
        return switch (this) {
            case TODO        -> "To Do";
            case IN_PROGRESS -> "In Progress";
            case DONE        -> "Done";
        };
    }
}
