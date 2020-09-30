package me.fusiondev.fusionpixelmon.modules.pokemodifiers;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.BaseModifier;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.GenderSwitcherModifier;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.ShinyModifier;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.GrowthModifier;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.PokeballModifier;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.NatureModifier;

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
        new GenderSwitcherModifier();
        new ShinyModifier();
        new GrowthModifier();
        new PokeballModifier();
        new NatureModifier();
    }
}
