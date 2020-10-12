package me.fusiondev.fusionpixelmon.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.List;

public interface IPokemonWrapper {

    String getTitle();

    String getIfShiny();

    String getSpeciesName();

    String getName();

    String getAbility();

    String getNature(boolean showMint);

    String getGender();

    String getSize();

    String getForm();

    String getCaughtBall();

    /**
     * Checks if the pokemon has a special texture.
     *
     * @return true if pokemon has a special texture.
     */
    boolean hasSpecialTexture();

    /**
     * Checks if the pokemon has a special or custom texture.
     *
     * @return true if pokemon has a special or custom texture.
     */
    boolean hasTexture();

    String getTexture();

    String getPokerus();

    List<String> getIVs();

    List<String> getEVs();

    /**
     * Checks if the specified Pokemon is an UltraBeast.
     *
     * @param pokemon the Pokemon to check.
     * @return true if the Pokemon is an UltraBeast; otherwise false.
     */
    static boolean isUltraBeast(Pokemon pokemon) {
        return EnumSpecies.ultrabeasts.contains(pokemon.getSpecies().getPokemonName());
    }

    /**
     * Gets the Pokemon IVs as an array in the order of:
     * HP, Attack, Defence, SpecialAttack, SpecialDefence, Speed
     *
     * @param pokemon the Pokemon.
     * @return an array of the Pokemon's IVs.
     */
    static int[] getIVArray(Pokemon pokemon) {
        StatsType[] statsTypes = StatsType.getStatValues();
        int[] array = new int[statsTypes.length];
        for (int i = 0; i < statsTypes.length; i++)
            array[i] = pokemon.getIVs().get(statsTypes[i]);
        return array;
    }

    /**
     * Gets the Pokemon EVs as an array in the order of:
     * HP, Attack, Defence, SpecialAttack, SpecialDefence, Speed
     *
     * @param pokemon the Pokemon.
     * @return an array of the Pokemon's EVs.
     */
    static int[] getEVArray(Pokemon pokemon) {
        StatsType[] statsTypes = StatsType.getStatValues();
        int[] array = new int[statsTypes.length];
        for (int i = 0; i < statsTypes.length; i++)
            array[i] = pokemon.getEVs().get(statsTypes[i]);
        return array;
    }

    /**
     * Shows a pretty looking tally, in the style of:
     * <pre>5/10 (50%)</pre>
     *
     * @param val the current value.
     * @param max the maximum value.
     * @return a pretty formatted tally.
     */
    static String beautifyTally(int val, int max) {
        return "§e" + val + "§8/§e" + max + " §8(§a" + (Math.round((float) val / max * 100)) + "%§8)";
    }

    /**
     * Shows a pretty looking tally for IV stats.
     *
     * @param val the current IV value.
     * @return a pretty formatted IV tally.
     * @see #beautifyTally(int, int)
     * @see IVStore#MAX_IVS
     */
    static String beautifyIV(int val) {
        return beautifyTally(val, IVStore.MAX_IVS);
    }

    /**
     * Shows a pretty looking tally for EV stats.
     * Note that this is for the max of 1 EV stat, not the max of all EVs.
     *
     * @param val the current EV value.
     * @return a pretty formatted EV tally.
     * @see #beautifyTally(int, int)
     * @see EVStore#MAX_EVS
     */
    static String beautifyEV(int val) {
        return beautifyTally(val, EVStore.MAX_EVS);
    }
}
