package com.adaptionsoft.games.uglytrivia;

public enum Category {
    POP("Pop"),
    SCIENCE("Science"),
    ROCK("Rock"),
    SPORTS("Sports");

    public final String label;

    private Category(String label) {
        this.label = label;
    }
}
