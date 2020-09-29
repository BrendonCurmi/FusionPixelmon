package me.fusiondev.fusionpixelmon.modules.pokemodifiers.types;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;

public class ShinyModifier extends BaseModifier {

    public ShinyModifier() {
        super("shiny", "switch shininess", "icicle_badge");
    }

    @Override
    public boolean execute(AbstractPlayer player, Pokemon pokemon) {
        pokemon.setShiny(!pokemon.isShiny());
        return true;
    }
}
