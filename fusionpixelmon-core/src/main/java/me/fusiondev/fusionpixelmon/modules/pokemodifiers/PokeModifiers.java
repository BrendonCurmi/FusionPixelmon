package me.fusiondev.fusionpixelmon.modules.pokemodifiers;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.*;

import java.util.HashMap;

public class PokeModifiers {

    private static HashMap<String, BaseModifier> modifiers = new HashMap<>();

    public static boolean hasModifier(String s, boolean clean) {
        return modifiers.containsKey(clean ? BaseModifier.cleanFullName(s) : s);
    }

    public static BaseModifier getModifier(String s, boolean clean) {
        return modifiers.get(clean ? BaseModifier.cleanFullName(s) : s);
    }

    public static void setModifier(String s, BaseModifier modifier) {
        modifiers.put(s, modifier);
    }

    public static void iterate(AbstractPlayer player) {
        player.sendMessage(Color.GREEN + "Can use the below modifiers:");
        for (String name : modifiers.keySet())
            player.sendMessage(Color.WHITE + "  " + name);
    }

    /**
     * Initialises the Modifiers.
     */
    public static void init() {
    }
}
