package io.github.brendoncurmi.fusionpixelmon.api.colour;

public enum Colour {
    DARK_RED('4'),
    RED('c'),
    GOLD('6'),
    YELLOW('e'),
    DARK_GREEN('2'),
    GREEN('a'),
    AQUA('b'),
    DARK_AQUA('3'),
    DARK_BLUE('1'),
    BLUE('9'),
    LIGHT_PURPLE('d'),
    DARK_PURPLE('5'),
    WHITE('f'),
    GREY('7'),
    DARK_GREY('8'),
    BLACK('0'),

    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r', true);

    private char code;
    private boolean style;

    Colour(char code, boolean style) {
        this.code = code;
        this.style = style;
    }

    Colour(char code) {
        this(code, false);
    }

    public char getCode() {
        return code;
    }

    public boolean isStyle() {
        return style;
    }
}
