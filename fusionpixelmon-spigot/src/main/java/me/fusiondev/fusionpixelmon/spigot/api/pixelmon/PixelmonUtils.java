package me.fusiondev.fusionpixelmon.spigot.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Toolkit class that provides helper methods for accessing the Pixelmon mod.
 */
public class PixelmonUtils extends AbstractPixelmonUtils {

    @Override
    public AbstractItemStack getPokeSprite(Pokemon pokemon, boolean createNewPokemon) {
        if (!createNewPokemon)
            return SpigotAdapter.adapt(CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) (Object) ItemPixelmonSprite.getPhoto(pokemon)));;
        Pokemon newPokemon = fromPokemon(pokemon, true).create();
        newPokemon.setCustomTexture("");
        return SpigotAdapter.adapt(CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) (Object) ItemPixelmonSprite.getPhoto(newPokemon)));
    }

    @Override
    public AbstractItemType getPixelmonItemType(String id) {
        return SpigotAdapter.adapt(Material.matchMaterial("pixelmon_" + id));
    }
}
