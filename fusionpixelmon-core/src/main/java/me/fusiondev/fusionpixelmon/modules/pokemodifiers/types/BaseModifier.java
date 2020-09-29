package me.fusiondev.fusionpixelmon.modules.pokemodifiers.types;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;

public abstract class BaseModifier {

    private final String name;
    private final String modifyWhat;
    private final String itemStack;

    //rainbow nick
    //ability modifier

    public BaseModifier(String name, String modifyWhat, String itemStack) {
        this.name = getFullName(name);
        this.modifyWhat = Color.WHITE + modifyWhat;
        this.itemStack = itemStack;
        PokeModifiers.setModifier(name, this);
    }

    public String getName() {
        return name;
    }

    public static String getFullName(String s) {
        return Color.GREEN + GrammarUtils.cap(s) + " Modifier";
    }

    public static String cleanFullName(String s) {
        return s.replace(Color.GREEN.toString(), "").replace(" Modifier", "").toLowerCase();
    }

    public String getModifyWhat() {
        return modifyWhat;
    }

    public String getItemStack() {
        return itemStack;
    }

    public abstract boolean execute(AbstractPlayer player, Pokemon pokemon);

    public void action(AbstractPlayer player, Runnable consume) {
        new PokeSelectorUI(player, "Apply to", "apply-to", pokemon -> {
            player.closeInventory();
            pokemon.retrieve();
            if (execute(player, pokemon)) {
                consume.run();
            }
        });
    }
}
