package me.fusiondev.fusionpixelmon.modules.pokemodifiers.types;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;

public class GenderSwitcherModifier extends BaseModifier {

    public GenderSwitcherModifier() {
        super("gender", "Switches your Pokemon's gender", "icicle_badge");
    }

    @Override
    public boolean execute(AbstractPlayer player, Pokemon pokemon) {
        if (pokemon.getGender() == Gender.None) return false;
        pokemon.setGender(pokemon.getGender() == Gender.Male ? Gender.Female : Gender.Male);
        return true;
    }
}
