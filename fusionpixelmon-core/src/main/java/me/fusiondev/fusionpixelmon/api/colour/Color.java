package me.fusiondev.fusionpixelmon.api.colour;

public enum Color {
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
    GRAY('7'),
    DARK_GRAY('8'),
    BLACK('0'),

    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r', true);

    private final char CODE;
    private final boolean STYLE;

    private static final String S = "§";

    Color(char code, boolean style) {
        this.CODE = code;
        this.STYLE = style;
    }

    Color(char code) {
        this(code, false);
    }

    public char getCode() {
        return CODE;
    }

    public boolean isStyle() {
        return STYLE;
    }

    @Override
    public String toString() {
        return S + CODE;
    }
}
