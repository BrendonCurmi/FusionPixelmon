package me.fusiondev.fusionpixelmon.modules.pokemodifiers.types;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;

public class NatureModifier extends BaseModifier {

    public NatureModifier() {
        super("nature", "Use to randomly switch your Pokemon's nature", "icicle_badge");
    }

    @Override
    public boolean execute(AbstractPlayer player, Pokemon pokemon) {
        pokemon.setNature(EnumNature.getRandomNature());
        return true;
    }
}
