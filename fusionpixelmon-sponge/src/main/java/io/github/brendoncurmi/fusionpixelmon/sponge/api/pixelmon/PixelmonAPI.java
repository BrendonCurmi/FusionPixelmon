package io.github.brendoncurmi.fusionpixelmon.sponge.api.pixelmon;

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
     * Creates a Pokemon Photo Sprite based on the specified pokemon.
     *
     * @param pokemon the Pokemon to take a photo of.
     * @return the ItemStack of the Pokemon photo.
     */
    public static ItemStack getPokeSprite(Pokemon pokemon) {
        return getPokeSprite(pokemon, false);
    }

    /**
     * Creates a Pokemon Photo Sprite based on a copy of the specified pokemon.
     *
     * @param pokemon          the Pokemon to take a photo of.
     * @param createNewPokemon if a copy of the pokemon should be taken to make the photo.
     * @return the ItemStack of the Pokemon photo.
     */
    public static ItemStack getPokeSprite(Pokemon pokemon, boolean createNewPokemon) {
        if (!createNewPokemon) return ItemStackUtil.fromNative(ItemPixelmonSprite.getPhoto(pokemon));
        Pokemon newPokemon = fromPokemon(pokemon, true).create();
        newPokemon.setCustomTexture("");
        return ItemStackUtil.fromNative(ItemPixelmonSprite.getPhoto(newPokemon));
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
            if (pokemon.getPokerus() != null) pokemonSpec.pokerusType = (byte) pokemon.getPokerus().type.ordinal();
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
}
