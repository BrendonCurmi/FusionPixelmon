package me.fusiondev.fusionpixelmon.modules.pokemodifiers.types;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;

public class GrowthModifier extends BaseModifier {

    public GrowthModifier() {
        super("growth", "randomly switch growths", "icicle_badge");
    }

    @Override
    public boolean execute(AbstractPlayer player, Pokemon pokemon) {
        pokemon.setGrowth(EnumGrowth.getRandomGrowth());
        return true;
    }
}
