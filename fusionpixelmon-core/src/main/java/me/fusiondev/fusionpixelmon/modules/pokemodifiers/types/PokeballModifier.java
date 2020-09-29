package me.fusiondev.fusionpixelmon.modules.pokemodifiers.types;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;

public class PokeballModifier extends BaseModifier {

    public PokeballModifier() {
        super("pokeball", "Randomly switches your Pokemon's pokeball", "icicle_badge");
    }

    @Override
    public boolean execute(AbstractPlayer player, Pokemon pokemon) {
        pokemon.setCaughtBall(getRandomPokeball());
        return true;
    }

    public static EnumPokeballs getRandomPokeball() {
        int tot = 0;
        int rndm = RandomHelper.rand.nextInt(65);
        EnumPokeballs[] pokeballs = EnumPokeballs.values();
        for (EnumPokeballs p : pokeballs) {
            tot += p.quantityMade;
            if (rndm < tot) return p;
        }
        return EnumPokeballs.PokeBall;
    }
}
