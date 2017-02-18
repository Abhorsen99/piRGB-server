package com.dibbledos.piRGB;

public enum ColorPin {
    BLUE(1, "blue"), GREEN(2, "green"), RED(3, "red");

    private final int pin;
    public final String name;

    ColorPin(int pin, String name) {
        this.pin = pin;
        this.name = name;
    }

    public int getValue() {
        return pin;
    }
}
