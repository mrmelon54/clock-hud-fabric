package net.onpointcoding.clockhud.enums;

public enum ClockPosition {
    LEFT("Left"),
    RIGHT("Right");

    private final String name;

    ClockPosition(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
