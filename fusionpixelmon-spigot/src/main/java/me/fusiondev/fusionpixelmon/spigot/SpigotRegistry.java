package me.fusiondev.fusionpixelmon.spigot;

import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotItemTypes;

public class SpigotRegistry extends Registry {
    @Override
    public AbstractItemStack getItemStack() {
        return null;
    }

    @Override
    public AbstractInventory getInventory() {
        return null;
    }

    @Override
    public AbstractItemTypes getItemTypesRegistry() {
        return new SpigotItemTypes();
    }

    @Override
    public AbstractPixelmonUtils getPixelmonUtils() {
        return null;
    }
}
