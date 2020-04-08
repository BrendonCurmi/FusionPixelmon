package me.FusionDev.FusionPixelmon.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import org.spongepowered.api.Sponge;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

/**
 * Toolkit class that provides helper methods for accessing the Pixelmon mod.
 */
public class PixelmonAPI {

    /**
     * Creates a Pokemon Photo Sprite based on the
     * specified pokemon.
     *
     * @param pokemon the Pokemon to take a photo of.
     * @return the ItemStack of the Pokemon Sprite photo.
     */
    public static ItemStack getPokeSprite(Pokemon pokemon) {
        return ItemStackUtil.fromNative(ItemPixelmonSprite.getPhoto(pokemon));
    }

    /**
     * Gets the {@link PokemonSpec} from the specified {@link Pokemon}.
     *
     * @param pokemon  the Pokemon.
     * @param detailed if the spec should be detailed. If false, the spec will
     *                 be limited to the Pokemon's species and shininess only.
     * @return the PokemonSpec of the Pokemon
     */
    public static PokemonSpec fromPokemon(Pokemon pokemon, boolean detailed) {
        PokemonSpec pokemonSpec = PokemonSpec.from(pokemon.getSpecies().name);
        if (detailed) {
            pokemonSpec.level = pokemon.getLevel();
            pokemonSpec.gender = (byte) pokemon.getGender().ordinal();
            pokemonSpec.growth = (byte) pokemon.getGrowth().index;
            pokemonSpec.nature = (byte) pokemon.getNature().index;
            pokemonSpec.ability = pokemon.getAbility().getName();
            pokemonSpec.form = (byte) pokemon.getForm();
            pokemonSpec.ball = (byte) pokemon.getCaughtBall().getIndex();
            pokemonSpec.pokerusType = (byte) pokemon.getPokerus().type.ordinal();
            pokemonSpec.egg = pokemon.isEgg();
        }
        pokemonSpec.boss = null;
        pokemonSpec.shiny = pokemon.isShiny();
        return pokemonSpec;
    }

    private static ItemType fallbackItemType = ItemTypes.REDSTONE;

    /**
     * Retrieves an ItemStack from the Pixelmon Mod, using the specified
     * item ID. If it fails, will return {@link #fallbackItemType} instead.
     *
     * @param id the Pixelmon item ID.
     * @return the ItemStack of the specified item ID from the Pixelmon
     * Mod; or {@link #fallbackItemType} if the item isn't found.
     */
    public static ItemStack getPixelmonItemStack(String id) {
        return ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + id).orElse(fallbackItemType)).build();
    }

    /**
     * Retrieves an ItemType from the Pixelmon Mod, using the specified
     * item ID. If it fails, will return {@link #fallbackItemType} instead.
     *
     * @param id the Pixelmon item ID.
     * @return the ItemType of the specified item ID from the Pixelmon
     * Mod; or {@link #fallbackItemType} if the item isn't found.
     */
    public static ItemType getPixelmonItemType(String id) {
        return Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + id).orElse(fallbackItemType);
    }

    /**
     * Checks if the specified Pokemon is an UltraBeast.
     *
     * @param pokemon the Pokemon to check.
     * @return true if the Pokemon is an UltraBeast; otherwise false.
     */
    public static boolean isUltraBeast(Pokemon pokemon) {
        return EnumSpecies.ultrabeasts.contains(pokemon.getSpecies().getPokemonName());
    }

    /**
     * Gets the Pokemon IVs as an array in the order of:
     * HP, Attack, Defence, SpecialAttack, SpecialDefence, Speed
     *
     * @param pokemon the Pokemon.
     * @return an array of the Pokemon's IVs.
     */
    public static int[] getIVArray(Pokemon pokemon) {
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
    public static int[] getEVArray(Pokemon pokemon) {
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
    public static String beautifyTally(int val, int max) {
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
    public static String beautifyIV(int val) {
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
    public static String beautifyEV(int val) {
        return beautifyTally(val, EVStore.MAX_EVS);
    }
}
