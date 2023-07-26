package com.example.thetelephoneappbe.enums;

public enum Status {
    IN_PROGRESS("IN_PROGRESS"),
    KNOCK_OFF("KNOCK_OFF"),
    DRAW("DRAW"),
    WRITE("WRITE"),
    AGAIN("AGAIN"),
    MAX("MAX"),
    NEW("NEW"),
    NORMAL("NORMAL");


    private final String value;

    private Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
