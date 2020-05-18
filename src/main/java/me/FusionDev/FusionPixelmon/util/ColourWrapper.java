package me.FusionDev.FusionPixelmon.util;

import me.FusionDev.FusionPixelmon.api.colour.Colour;
import me.FusionDev.FusionPixelmon.api.colour.IColourWrapper;

public class ColourWrapper implements IColourWrapper {

    private Colour colour;
    private Colour style;

    @Override
    public boolean hasColour() {
        return colour != null;
    }

    @Override
    public void setColour(Colour colour) {
        if (colour.isStyle()) throw new IllegalArgumentException("Colour must not be a style");
        this.colour = colour;
    }

    @Override
    public boolean hasStyle() {
        return style != null;
    }

    @Override
    public void setStyle(Colour style) {
        if (!style.isStyle()) throw new IllegalArgumentException("Colour is not a style");
        this.style = style;
    }

    @Override
    public String getFullCode() {
        StringBuilder s = new StringBuilder();
        if (colour != null) s.append("§").append(colour.getCode());
        if (style != null) s.append("§").append(style.getCode());
        return s.toString();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (colour != null) s.append(colour.name());
        if (style != null) {
            if (s.length() > 0) s.append(" & ");
            s.append(style.name());
        }
        return Grammar.cap(s.toString());
    }
}
