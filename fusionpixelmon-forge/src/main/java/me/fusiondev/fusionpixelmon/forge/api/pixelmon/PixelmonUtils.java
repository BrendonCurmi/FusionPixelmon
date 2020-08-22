package me.fusiondev.fusionpixelmon.forge.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class PixelmonUtils extends AbstractPixelmonUtils {

    @Override
    public AbstractItemStack getPokeSprite(Pokemon pokemon, boolean createNewPokemon) {
        if (!createNewPokemon)
            return ForgeAdapter.adapt(ItemPixelmonSprite.getPhoto(pokemon));
        Pokemon newPokemon = fromPokemon(pokemon, true).create();
        newPokemon.setCustomTexture("");
        return ForgeAdapter.adapt(ItemPixelmonSprite.getPhoto(newPokemon));
    }

    @Override
    public AbstractItemType getPixelmonItemType(String id) {
        return ForgeAdapter.adapt(Item.REGISTRY.getObject(new ResourceLocation("pixelmon", id)));
    }
}
