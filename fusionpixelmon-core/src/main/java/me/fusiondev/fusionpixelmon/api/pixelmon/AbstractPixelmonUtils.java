package me.fusiondev.fusionpixelmon.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;

public abstract class AbstractPixelmonUtils {

    /**
     * Creates a Pokemon Photo Sprite based on the specified pokemon.
     *
     * @param pokemon the Pokemon to take a photo of.
     * @return the AbstractItemStack of the Pokemon photo.
     */
    public AbstractItemStack getPokeSprite(Pokemon pokemon) {
        return getPokeSprite(pokemon, false);
    }

    /**
     * Creates a Pokemon Photo Sprite based on a copy of the specified pokemon.
     *
     * @param pokemon          the Pokemon to take a photo of.
     * @param createNewPokemon if a copy of the pokemon should be taken to make the photo.
     * @return the AbstractItemStack of the Pokemon photo.
     */
    public abstract AbstractItemStack getPokeSprite(Pokemon pokemon, boolean createNewPokemon);

    /**
     * Gets the {@link PokemonSpec} from the specified {@link Pokemon}.
     *
     * @param pokemon  the Pokemon.
     * @param detailed if the spec should be detailed. If false, the spec will
     *                 be limited to the Pokemon's species and shininess only.
     * @return the PokemonSpec of the Pokemon
     */
    public PokemonSpec fromPokemon(Pokemon pokemon, boolean detailed) {
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

    /**
     * Retrieves an ItemStack from the Pixelmon Mod, using the specified item ID.
     *
     * @param id the Pixelmon item ID.
     * @return the AbstractItemStack of the specified item ID from the Pixelmon Mod.
     */
    public AbstractItemStack getPixelmonItemStack(String id) {
        return getPixelmonItemType(id).to();
    }

    /**
     * Retrieves an ItemType from the Pixelmon Mod, using the specified item ID.
     *
     * @param id the Pixelmon item ID.
     * @return the AbstractItemType of the specified item ID from the Pixelmon Mod.
     */
    public abstract AbstractItemType getPixelmonItemType(String id);
}
