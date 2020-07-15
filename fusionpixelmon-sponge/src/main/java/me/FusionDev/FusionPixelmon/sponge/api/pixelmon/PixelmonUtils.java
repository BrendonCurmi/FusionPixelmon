package me.FusionDev.FusionPixelmon.sponge.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import me.FusionDev.FusionPixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

/**
 * Toolkit class that provides helper methods for accessing the Pixelmon mod.
 */
public class PixelmonUtils extends AbstractPixelmonUtils {

    private static ItemType fallbackItemType = ItemTypes.REDSTONE;

    @Override
    public AbstractItemStack getPokeSprite(Pokemon pokemon, boolean createNewPokemon) {
        if (!createNewPokemon) return SpongeAdapter.adapt(ItemStackUtil.fromNative(ItemPixelmonSprite.getPhoto(pokemon)));
        Pokemon newPokemon = fromPokemon(pokemon, true).create();
        newPokemon.setCustomTexture("");
        return SpongeAdapter.adapt(ItemStackUtil.fromNative(ItemPixelmonSprite.getPhoto(newPokemon)));
    }

    @Override
    public AbstractItemType getPixelmonItemType(String id) {
        return SpongeAdapter.adapt(Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + id).orElse(fallbackItemType));
    }
}
