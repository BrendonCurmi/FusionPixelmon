package me.fusiondev.fusionpixelmon.spigot;

import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;
import me.fusiondev.fusionpixelmon.spigot.api.pixelmon.PixelmonUtils;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotItemTypes;

public class SpigotRegistry extends Registry {
    @Override
    public AbstractItemStack getItemStack() {
        return null;
    }

    @Override
    public InvInventory getInvInventory() {
        return new SpigotInvInventory();
    }

    @Override
    public AbstractItemTypes getItemTypesRegistry() {
        return new SpigotItemTypes();
    }

    @Override
    public AbstractPixelmonUtils getPixelmonUtils() {
        return new PixelmonUtils();
    }
}
