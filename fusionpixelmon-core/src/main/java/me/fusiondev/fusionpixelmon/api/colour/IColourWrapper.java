package me.fusiondev.fusionpixelmon.api.color;

public interface IColourWrapper {
    boolean hasColour();

    void setColor(Color color);

    boolean hasStyle();

    void setStyle(Color style);

    String getFullCode();
}
