package io.github.brendoncurmi.fusionpixelmon.spigot.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class PixelmonAPI {

    public static ItemStack getPokeSprite(Pokemon pokemon) {
        return getPokeSprite(pokemon, false);
    }

    public static ItemStack getPokeSprite(Pokemon pokemon, boolean createNewPokemon) {
        if (!createNewPokemon)
            return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) (Object) ItemPixelmonSprite.getPhoto(pokemon));;
        Pokemon newPokemon = fromPokemon(pokemon, true).create();
        newPokemon.setCustomTexture("");
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) (Object) ItemPixelmonSprite.getPhoto(newPokemon));
    }

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

    public static ItemStack getPixelmonItemStack(String id) {
        return new ItemStack(getPixelmonItemType(id));
    }

    public static Material getPixelmonItemType(String id) {
        return Material.matchMaterial("pixelmon_" + id);
    }
}
