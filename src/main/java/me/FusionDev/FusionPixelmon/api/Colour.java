package me.FusionDev.FusionPixelmon.api;

import me.FusionDev.FusionPixelmon.util.Grammar;

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

    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r');

    private char code;

    Colour(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    @Override
    public String toString() {
        return Grammar.cap(name());
    }
}
