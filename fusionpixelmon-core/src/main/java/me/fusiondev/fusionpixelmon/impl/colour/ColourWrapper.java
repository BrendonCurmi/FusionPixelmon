package me.fusiondev.fusionpixelmon.impl.colour;

import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.colour.IColourWrapper;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;

public class ColourWrapper implements IColourWrapper {

    private Color color;
    private Color style;

    @Override
    public boolean hasColour() {
        return color != null;
    }

    @Override
    public void setColor(Color color) {
        if (color.isStyle()) throw new IllegalArgumentException("Colour must not be a style");
        this.color = color;
    }

    @Override
    public boolean hasStyle() {
        return style != null;
    }

    @Override
    public void setStyle(Color style) {
        if (!style.isStyle()) throw new IllegalArgumentException("Colour is not a style");
        this.style = style;
    }

    @Override
    public String getFullCode() {
        StringBuilder s = new StringBuilder();
        if (color != null) s.append("§").append(color.getCode());
        if (style != null) s.append("§").append(style.getCode());
        return s.toString();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (color != null) s.append(color.name());
        if (style != null) {
            if (s.length() > 0) s.append(" & ");
            s.append(style.name());
        }
        return GrammarUtils.cap(s.toString());
    }
}
